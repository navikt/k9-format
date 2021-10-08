package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TidsserieUtils.tilPeriodeList;
import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.fpsak.tidsserie.StandardCombinators;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

class PleiepengerSyktBarnYtelseValidator extends YtelseValidator {

    private final String YTELSE_FELT = "ytelse.";

    @Override
    public List<Feil> valider(Ytelse ytelse) {
        return validerMedGyldigEndringsperodeHvisDenFinnes(ytelse, List.of(), false);
    }

    //For å overload forsikreValidert(Ytelse ytelse)
    public void forsikreValidert(Ytelse ytelse, List<Periode> gyldigeEndringsperioder) {
        Objects.requireNonNull(gyldigeEndringsperioder, "gyldigeEndringsperioder");
        
        List<Feil> feil = validerMedGyldigEndringsperodeHvisDenFinnes(ytelse, gyldigeEndringsperioder, true);
        if (!feil.isEmpty()) {
            throw new ValideringsFeil(feil);
        }
    }

    public List<Feil> valider(Ytelse ytelse, List<Periode> gyldigeEndringsperioder) {
        return validerMedGyldigEndringsperodeHvisDenFinnes(ytelse, gyldigeEndringsperioder, true);
    }
    
    List<Feil> validerMedGyldigEndringsperodeHvisDenFinnes(Ytelse ytelse,
            List<Periode> gyldigeEndringsperioder,
            boolean brukValideringMedUtledetEndringsperiode) {
        
        Objects.requireNonNull(gyldigeEndringsperioder, "gyldigeEndringsperioder");
        
        var psb = (PleiepengerSyktBarn) ytelse;
        var feilene = new ArrayList<Feil>();

        try {
            feilene.addAll(validerOgLeggTilFeilene(psb, gyldigeEndringsperioder, brukValideringMedUtledetEndringsperiode));
        } catch (ValideringsAvbrytendeFeilException valideringsAvbrytendeFeilException) {
            feilene.addAll(valideringsAvbrytendeFeilException.getFeilList());
        }

        return feilene;
    }

    private List<Feil> validerOgLeggTilFeilene(PleiepengerSyktBarn psb,
            List<Periode> gyldigeEndringsperioder,
            boolean brukValideringMedUtledetEndringsperiode) {
        final List<Feil> feilene = new ArrayList<Feil>();

        feilene.addAll(validerLovligEndring(psb, gyldigeEndringsperioder, brukValideringMedUtledetEndringsperiode));
        feilene.addAll(validerPerioderErLukketOgGyldig(psb.getBosteder().getPerioder(), "bosteder.perioder"));
        feilene.addAll(validerPerioderErLukketOgGyldig(psb.getUtenlandsopphold().getPerioder(), "utenlandsopphold.perioder"));
        
        final LocalDateTimeline<Boolean> søknadsperiodeTidslinje = lagTidslinjeOgValider(psb.getSøknadsperiodeList(), "søknadsperiode.perioder");
        final LocalDateTimeline<Boolean> intervalForEndringTidslinje;
        
        if (brukValideringMedUtledetEndringsperiode) {
            final LocalDateTimeline<Boolean> gyldigEndringsperiodeTidslinje = lagTidslinjeOgValider(gyldigeEndringsperioder, "gyldigeEndringsperioder.perioder");
            intervalForEndringTidslinje = søknadsperiodeTidslinje.union(gyldigEndringsperiodeTidslinje, StandardCombinators::coalesceLeftHandSide);
        } else {
            final LocalDateTimeline<Boolean> endringsperiodeTidslinje = lagTidslinjeOgValider(psb.getEndringsperiode(), "endringsperiode.perioder");
            intervalForEndringTidslinje = søknadsperiodeTidslinje.union(endringsperiodeTidslinje, StandardCombinators::coalesceLeftHandSide);
        }

        final LocalDateTimeline<Boolean> trekkKravPerioderTidslinje = lagTidslinjeOgValider(psb.getTrekkKravPerioder(), "trekkKravPerioder.perioder");

        if (brukValideringMedUtledetEndringsperiode) {
            feilene.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, søknadsperiodeTidslinje, "trekkKravPerioder"));
        }

        for (var ytelsePeriode : PerioderMedEndringUtil.getAllePerioderSomMåVæreInnenforSøknadsperiode(psb)) {
            var ytelsePeriodeTidsserie = lagTidslinjeOgValiderForYtelseperioder(ytelsePeriode.getPeriodeList(), ytelsePeriode.getFelt() + ".perioder");
            feilene.addAll(validerAtYtelsePerioderErInnenforIntervalForEndring(intervalForEndringTidslinje, ytelsePeriodeTidsserie, ytelsePeriode.getFelt() + ".perioder"));
            feilene.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, ytelsePeriodeTidsserie, ytelsePeriode.getFelt() + ".perioder"));
        }

        feilene.addAll(validerAtYtelsePeriodenErKomplettMedSøknad(søknadsperiodeTidslinje, psb.getUttak().getPerioder(), "uttak"));
        
        return feilene;
    }

    private List<Feil> validerLovligEndring(PleiepengerSyktBarn psb, List<Periode> gyldigeEndringsperioder,
            boolean brukValideringMedUtledetEndringsperiode) {
        if (psb.getSøknadsperiodeList().isEmpty()
                && (
                    !brukValideringMedUtledetEndringsperiode && psb.getEndringsperiode().isEmpty()
                    || brukValideringMedUtledetEndringsperiode && gyldigeEndringsperioder.isEmpty()
                )) {
            return List.of(lagFeil("søknadsperiode", "missingArgument", "Mangler søknadsperiode eller gyldigeEndringsperioder."));
        }
        return List.of();
    }

    private List<Feil> validerAtYtelsePerioderErInnenforIntervalForEndring(LocalDateTimeline<Boolean> gyldigInterval,
                                                                           LocalDateTimeline<Boolean> testTidsserie,
                                                                           String felt) {
        return tilPeriodeList(
                testTidsserie.disjoint(gyldigInterval)).stream()
                .map(p -> toFeil(p, felt, "ugyldigPeriode", "Perioden er utenfor gyldig interval. Gyldig interva: (" + gyldigInterval.toString() + "), Periode: "))
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

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(List<Periode> periodeList, String felt) throws ValideringsAvbrytendeFeilException {
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

    private LocalDateTimeline<Boolean> lagTidslinjeOgValiderForYtelseperioder(List<Periode> periodeList, String felt) throws ValideringsAvbrytendeFeilException {
        var feil = validerPerioderErLukketOgGyldigForYtelseperioder(periodeList, felt);
        if (!feil.isEmpty()) {
            throw new ValideringsAvbrytendeFeilException(feil);
        }
        try {
            return toLocalDateTimeline(periodeList);
        } catch (IllegalArgumentException e) {
            throw new ValideringsAvbrytendeFeilException(List.of(lagFeil(felt, "IllegalArgumentException", e.getMessage())));
        }
    }

    private List<Feil> validerPerioderErLukketOgGyldigForYtelseperioder(List<Periode> perioder, String felt) {
        var feil = new ArrayList<Feil>();
        perioder.forEach(p -> {
            validerPerioderErLukket(p, felt + "[" + p + "]", feil);
            validerPerioderIkkeErInvertert(p, felt + "[" + p + "]", feil);});
        return feil;
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
