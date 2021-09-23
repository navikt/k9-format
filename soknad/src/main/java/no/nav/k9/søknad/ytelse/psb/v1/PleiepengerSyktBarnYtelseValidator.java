package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TidsserieUtils.tilPeriodeList;
import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.fpsak.tidsserie.StandardCombinators;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

public class PleiepengerSyktBarnYtelseValidator extends YtelseValidator {

    private final String YTELSE_FELT = "ytelse.";

    //PleiepengerSyktBarnValidator

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Override
    public List<Feil> valider(Ytelse ytelse) {
        return valider(ytelse, List.of());
    }

    public void forsikreValidert(Ytelse ytelse, List<Periode> gyldigeEndringsperioder) {
        List<Feil> feil = valider(ytelse, gyldigeEndringsperioder);
        if (!feil.isEmpty()) {
            throw new ValideringsFeil(feil);
        }
    }

    public List<Feil> valider(Ytelse ytelse, List<Periode> gyldigeEndringsperioder) {
        var psb = (PleiepengerSyktBarn) ytelse;
        var validate = VALIDATOR_FACTORY.getValidator().validate(psb);

        List<Feil> feil = validate.stream()
                .map(this::toFeil)
                .collect(Collectors.toList());


        //TODO endre getEndringsperioder til gyldigeEndringsperioder
        if (psb.getSøknadsperiodeList().isEmpty() && psb.getEndringsperiode().isEmpty()) {
            feil.add(lagFeil("søknadsperiode", "missingArgument", "Mangler søknadsperiode eller gyldigeEndringsperioder."));
        }
        feil.addAll(validerPerioderErLukketOgGyldig(psb.getBosteder().getPerioder(), "bosteder.perioder"));
        feil.addAll(validerPerioderErLukketOgGyldig(psb.getUtenlandsopphold().getPerioder(), "utenlandsopphold.perioder"));

        try {
            var søknadsperiodeTidslinje = lagTidslinjeOgValider(psb.getSøknadsperiodeList(), "søknadsperiode.perioder");
            var gyldigEndringsperiodeTidslinje = lagTidslinjeOgValider(gyldigeEndringsperioder, "gyldigeEndringsperioder.perioder");
            var endringsperiodeTidslinje = lagTidslinjeOgValider(psb.getEndringsperiode(),"endringsperiode.perioder");
            var intervalForEndringTidslinje = søknadsperiodeTidslinje.union(gyldigEndringsperiodeTidslinje, StandardCombinators::coalesceLeftHandSide);

            //TODO: Slette når endringerperioder utledes.
            intervalForEndringTidslinje = intervalForEndringTidslinje.union(endringsperiodeTidslinje, StandardCombinators::coalesceLeftHandSide);
            //TODO: Slette når endringerperioder utledes.

            var trekkKravPerioderTidslinje = lagTidslinjeOgValider(psb.getTrekkKravPerioder(), "trekkKravPerioder.perioder");

            // Validerer at trekkKravPerioder ikke er innenfor søknadsperioden
            feil.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, søknadsperiodeTidslinje, "søknadperiode.perioder"));

            for (var ytelsePeriode : PerioderMedEndringUtil.getAllePerioderSomMåVæreInnenforSøknadsperiode(psb)) {
                var ytelsePeriodeTidsserie = lagTidslinjeOgValider(ytelsePeriode.getPeriodeList(), ytelsePeriode.getFelt() + ".perioder");
                feil.addAll(validerAtYtelsePerioderErInnenforIntervalForEndring(intervalForEndringTidslinje, ytelsePeriodeTidsserie, ytelsePeriode.getFelt() + ".perioder"));
                feil.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, ytelsePeriodeTidsserie, ytelsePeriode.getFelt() + ".perioder"));
            }

            validerAtYtelsePeriodenErKomplettMedSøknad(søknadsperiodeTidslinje, psb.getUttak().getPerioder(), "uttak");

        } catch (ValideringsAvbrytendeFeilException valideringsAvbrytendeFeilException) {
            feil.addAll(valideringsAvbrytendeFeilException.getFeilList());
        }

        return feil;
    }

    private List<Feil> validerAtYtelsePerioderErInnenforIntervalForEndring(LocalDateTimeline<Boolean> gyldigInterval,
                                                                           LocalDateTimeline<Boolean> testTidsserie,
                                                                           String felt) {
        return tilPeriodeList(
                testTidsserie.disjoint(gyldigInterval)).stream()
                .map(p -> toFeil(p, felt, "ugyldigPeriode", "Perioden er utenfor gyldig interval(" + gyldigInterval.toString() + ") : "))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Feil> validerAtYtelsePeriodenErKomplettMedSøknad(LocalDateTimeline<Boolean> søknadsperiode,
                                                                  Map<Periode, ?> ytelsePeriode,
                                                                  String felt) {
        return tilPeriodeList(søknadsperiode.disjoint(lagTidslinjeOgValider(new ArrayList<>(ytelsePeriode.keySet()), felt))).stream()
                .filter(this::periodeInneholderDagerSomIkkeErHelg)
                .map(p -> toFeil(p, felt, "ikkeKomplettPeriode", "Periodene er ikke komplett, periode som mangler er: "))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Feil> validerAtIngenPerioderOverlapperMedTrekkKravPerioder(LocalDateTimeline<Boolean> trekkKravPerioder,
                                                                            LocalDateTimeline<Boolean> test,
                                                                            String felt) {
        return tilPeriodeList(trekkKravPerioder.intersection(test)).stream()
                .map(p -> toFeil(p, felt, "ugyldigPeriodeInterval", "Overlapper med trekk krav periode: "))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean periodeInneholderDagerSomIkkeErHelg(Periode periode) {
        LocalDate testDag = periode.getFraOgMed();
        while (testDag.isBefore(periode.getTilOgMed()) || testDag.isEqual(periode.getTilOgMed())) {
            if (!((testDag.getDayOfWeek() == DayOfWeek.SUNDAY) || (testDag.getDayOfWeek() == DayOfWeek.SATURDAY))) {
                return true;
            }
            testDag = testDag.plusDays(1);
        }
        return false;
    }

    private Feil toFeil(Periode periode, String felt, String feilkode, String feilmelding) {
        return lagFeil(felt, feilkode, feilmelding + periode.toString());
    }

    private Feil toFeil(ConstraintViolation<PleiepengerSyktBarn> constraintViolation) {
        return lagFeil(
                constraintViolation.getPropertyPath().toString(),
                PÅKREVD,
                constraintViolation.getMessage());
    }

    private Feil lagFeil(String felt, String feilkode, String feilmelding) {
        return new Feil(YTELSE_FELT + felt, feilkode, feilmelding);
    }

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(List<Periode> periodeList, String felt)
            throws ValideringsAvbrytendeFeilException {
        var feil = validerPerioderErLukketOgGyldig(periodeList, felt);
        if (!feil.isEmpty()) {
            throw new ValideringsAvbrytendeFeilException(feil);
        }
        try {
            return toLocalDateTimeline(periodeList);
        } catch (IllegalArgumentException e) {
            throw new ValideringsAvbrytendeFeilException(List.of(lagFeil(felt, "IllegalArgumentException", e.getMessage())));
        }
    }

    private List<Feil> validerPerioderErLukketOgGyldig(Map<Periode, ?> perioder, String felt) {
        var feil = new ArrayList<Feil>();
        perioder.keySet().forEach(p -> {
            validerPerioderErLukket(p, felt + "[" + p + "]", feil);
            validerPerioderIkkeErInvertert(p, felt + "[" + p + "]", feil);});
        return feil;
    }

    private List<Feil> validerPerioderErLukketOgGyldig(List<Periode> periodeList, String felt) {
        var feil = new ArrayList<Feil>();
        for (int i = 0; i < periodeList.size(); i++ ) {
            var periode = periodeList.get(i);
            if (periode != null) {
                validerPerioderErLukket(periode, felt + "[" + i + "]", feil);
                validerPerioderIkkeErInvertert(periode, felt + "[" + i + "]", feil);
            }
        }
        return feil;
    }

    private void validerPerioderErLukket(Periode periode, String felt, List<Feil> feil) {
        if (periode.getTilOgMed() == null) {
            feil.add(lagFeil(felt, "påkrevd", "Til og med (TOM) må være satt."));
        }
        if (periode.getFraOgMed() == null) {
            feil.add(lagFeil(felt, "påkrevd", "Fra og med (FOM) må være satt."));
        }
    }

    private void validerPerioderIkkeErInvertert(Periode periode, String felt, List<Feil> feil) {
        if (periode.getFraOgMed() != null && periode.getTilOgMed() != null && periode.getTilOgMed().isBefore(periode.getFraOgMed())) {
            feil.add(lagFeil(felt, "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM)."));
        }
    }

    private static class ValideringsAvbrytendeFeilException extends RuntimeException {

        private final List<Feil> feilList;

        public ValideringsAvbrytendeFeilException(List<Feil> feilList) {
            this.feilList = feilList;
        }

        public List<Feil> getFeilList() {
            return feilList;
        }
    }
}
