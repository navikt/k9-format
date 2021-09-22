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
        feil.addAll(validerPerioderErLukketGyldigOgIkkeOverlapper(psb.getBosteder().getPerioder(), "bosteder"));
        feil.addAll(validerPerioderErLukketGyldigOgIkkeOverlapper(psb.getUtenlandsopphold().getPerioder(), "utenlandsopphold"));

        try {
            var søknadsperiodeTidslinje = lagTidslinjeOgValider(psb.getSøknadsperiodeList(), "søknadsperiode");
            var gyldigEndringsperiodeTidslinje = lagTidslinjeOgValider(gyldigeEndringsperioder, "gyldigeEndringsperioder");
            var endringsperiodeTidslinje = lagTidslinjeOgValider(psb.getEndringsperiode(),"endringsperiode");
            var intervalForEndringTidslinje = søknadsperiodeTidslinje.union(gyldigEndringsperiodeTidslinje, StandardCombinators::coalesceLeftHandSide);

            //TODO: Slette når endringerperioder utledes.
            intervalForEndringTidslinje = intervalForEndringTidslinje.union(endringsperiodeTidslinje, StandardCombinators::coalesceLeftHandSide);
            //TODO: Slette når endringerperioder utledes.

            var trekkKravPerioderTidslinje = lagTidslinjeOgValider(psb.getTrekkKravPerioder(), "trekkKravPerioder");

            // Validerer at trekkKravPerioder ikke er innenfor søknadsperioden
            feil.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, søknadsperiodeTidslinje, "søknadperiode"));

            for (var ytelsePeriode : PerioderMedEndringUtil.getAllePerioderSomMåVæreInnenforSøknadsperiode(psb)) {
                var ytelsePeriodeTidsserie = lagTidslinjeOgValider(ytelsePeriode.getPeriodeList(), ytelsePeriode.getFelt());
                feil.addAll(validerAtYtelsePerioderErInnenforIntervalForEndring(intervalForEndringTidslinje, ytelsePeriodeTidsserie, ytelsePeriode.getFelt()));
                feil.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, ytelsePeriodeTidsserie, ytelsePeriode.getFelt()));
            }

            validerAtYtelsePeriodenErKomplettMedSøknad(søknadsperiodeTidslinje, psb.getUttak().getPerioder(), "uttak");

        } catch (ValideringsAvbrytendeFeilException e) {
            feil.addAll(e.getFeilList());
        }

        return feil;
    }

    private List<Feil> validerAtYtelsePerioderErInnenforIntervalForEndring(LocalDateTimeline<Boolean> gyldigInterval,
                                                                           LocalDateTimeline<Boolean> testTidsserie,
                                                                           String felt) {
        return tilPeriodeList(
                testTidsserie.disjoint(gyldigInterval)).stream()
                .map(p -> toFeil(p, felt + ".perioder", "ugyldigPeriode", "Perioden er utenfor gyldig interval(" + gyldigInterval.toString() + ") : "))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Feil> validerAtYtelsePeriodenErKomplettMedSøknad(LocalDateTimeline<Boolean> søknadsperiode,
                                                                  Map<Periode, ?> ytelsePeriode,
                                                                  String felt) {
        return tilPeriodeList(søknadsperiode.disjoint(lagTidslinjeOgValider(new ArrayList<>(ytelsePeriode.keySet()), felt))).stream()
                .filter(this::periodeInneholderDagerSomIkkeErHelg)
                .map(p -> toFeil(p, felt + ".perioder", "ikkeKomplettPeriode", "Periodene er ikke komplett, periode som mangler er: "))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Feil> validerAtIngenPerioderOverlapperMedTrekkKravPerioder(LocalDateTimeline<Boolean> trekkKravPerioder,
                                                                            LocalDateTimeline<Boolean> test,
                                                                            String felt) {
        return tilPeriodeList(trekkKravPerioder.intersection(test)).stream()
                .map(p -> toFeil(p, felt + ".perioder", "ugyldigPeriodeInterval", "Overlapper med trekk krav periode: "))
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
        var feil = validerPerioderErLukketGyldigOgIkkeOverlapper(periodeList, felt);
        if (!feil.isEmpty()) {
            throw new ValideringsAvbrytendeFeilException(feil);
        }
        return toLocalDateTimeline(periodeList);
    }

    private List<Feil> validerPerioderErLukketGyldigOgIkkeOverlapper(Map<Periode, ?> perioder, String felt) {
        return validerPerioderErLukketGyldigOgIkkeOverlapper(new ArrayList<>(perioder.keySet()), felt);
    }

    private List<Feil> validerPerioderErLukketGyldigOgIkkeOverlapper(List<Periode> periodeList, String felt) {
        var feil = new ArrayList<Feil>();
        var tempPeriodeList = new ArrayList<Periode>();
        for (int i = 0; i < periodeList.size(); i++ ) {
            var periode = periodeList.get(i);
            if (periode != null) {
                validerPerioderErLukket(periode, felt + ".perioder[" + i + "]", feil);
                validerPerioderIkkeErInvertert(periode, felt + ".perioder[" + i + "]", feil);
                for (int j = 0; j < tempPeriodeList.size(); j++) {
                    if (overlapper(periode, periodeList.get(j))) {
                        feil.add(lagFeil(
                                felt + ".perioder[" + i + "]",
                                "overlappendePeriode",
                                "Periode i listen overlapper: " + periode.toString() + " med " + periodeList.get(j)));
                        throw new ValideringsAvbrytendeFeilException(feil);
                    }
                }
                tempPeriodeList.add(periode);
            }
        }
        return feil;
    }

    private static boolean overlapper(Periode periode1, Periode periode2) {

        // Hele dager; Kan ikke føre opp fler perioder samme dag
        if ((periode2.getFraOgMed().equals(periode1.getTilOgMed())) || (periode1.getFraOgMed().equals(periode2.getFraOgMed()))) {
            return true;
        }

        // Om ingen av periodene har en tilOgMed (Flere åpne perioder) vil de alltid overlappe
        if (periode1.getTilOgMed() == null && periode2.getTilOgMed() == null) {
            return true;
        }

        return (periode1.getTilOgMed() == null || !periode1.getTilOgMed().isBefore(periode2.getFraOgMed()))
                && (periode2.getTilOgMed() == null || !periode1.getFraOgMed().isAfter(periode2.getTilOgMed()));
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
