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

    List<Feil> validerOgLeggTilFeilene(PleiepengerSyktBarn psb,
                                       List<Periode> gyldigeEndringsperioder,
                                       boolean brukValideringMedUtledetEndringsperiode) {
        final List<Feil> feilene = new ArrayList<Feil>();

        feilene.addAll(validerLovligEndring(psb, gyldigeEndringsperioder, brukValideringMedUtledetEndringsperiode));
        feilene.addAll(validerPerioderErLukketOgGyldig(psb.getBosteder().getPerioder(), "bosteder.perioder"));
        feilene.addAll(validerPerioderErLukketOgGyldig(psb.getUtenlandsopphold().getPerioder(), "utenlandsopphold.perioder"));
        
        final LocalDateTimeline<Boolean> søknadsperiodeTidslinje = lagTidslinjeOgValider(psb.getSøknadsperiodeList(), "søknadsperiode.perioder", feilene);
        final LocalDateTimeline<Boolean> intervalForEndringTidslinje;
        
        if (brukValideringMedUtledetEndringsperiode) {
            final LocalDateTimeline<Boolean> gyldigEndringsperiodeTidslinje = lagTidslinjeOgValider(gyldigeEndringsperioder, "gyldigeEndringsperioder.perioder", feilene);
            intervalForEndringTidslinje = søknadsperiodeTidslinje.union(gyldigEndringsperiodeTidslinje, StandardCombinators::coalesceLeftHandSide);
        } else {
            final LocalDateTimeline<Boolean> endringsperiodeTidslinje = lagTidslinjeOgValider(psb.getEndringsperiode(), "endringsperiode.perioder", feilene);
            intervalForEndringTidslinje = søknadsperiodeTidslinje.union(endringsperiodeTidslinje, StandardCombinators::coalesceLeftHandSide);
        }

        final LocalDateTimeline<Boolean> trekkKravPerioderTidslinje = lagTidslinjeOgValider(psb.getTrekkKravPerioder(), "trekkKravPerioder.perioder", feilene);

        if (brukValideringMedUtledetEndringsperiode) {
            feilene.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, søknadsperiodeTidslinje, "trekkKravPerioder"));
        }

        for (var ytelsePeriode : PerioderMedEndringUtil.getAllePerioderSomMåVæreInnenforSøknadsperiode(psb)) {
            var ytelsePeriodeTidsserie = lagTidslinjeOgValider(ytelsePeriode.getPeriodeMap(), ytelsePeriode.getFelt() + ".perioder", feilene);
            feilene.addAll(validerAtYtelsePerioderErInnenforIntervalForEndring(intervalForEndringTidslinje, ytelsePeriodeTidsserie, ytelsePeriode.getFelt() + ".perioder"));
            feilene.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, ytelsePeriodeTidsserie, ytelsePeriode.getFelt() + ".perioder"));
        }

        validerAtYtelsePeriodenErKomplettMedSøknad(søknadsperiodeTidslinje, psb.getUttak().getPerioder(), "uttak", feilene);
        
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
                .filter(this::periodeInneholderDagerSomIkkeErHelg)
                .map(p -> toFeil(p, felt, "ugyldigPeriode", "Perioden er utenfor gyldig interval. Gyldig interva: (" + gyldigInterval.toString() + "), Periode: "))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void validerAtYtelsePeriodenErKomplettMedSøknad(LocalDateTimeline<Boolean> søknadsperiode,
                                                                  Map<Periode, ?> ytelsePeriode,
                                                                  String felt,
                                                                  List<Feil> feil) {
        feil.addAll( tilPeriodeList(søknadsperiode.disjoint(lagTidslinjeOgValider(new ArrayList<>(ytelsePeriode.keySet()), felt, feil))).stream()
                .filter(this::periodeInneholderDagerSomIkkeErHelg)
                .map(p -> toFeil(p, felt, "ikkeKomplettPeriode", "Periodene er ikke komplett, periode som mangler er: "))
                .collect(Collectors.toCollection(ArrayList::new)));
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

    private Feil lagFeil(String felt, String feilkode, String feilmelding) {
        return new Feil(YTELSE_FELT + felt, feilkode, feilmelding);
    }

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(List<Periode> periodeList, String felt, List<Feil> feil) throws ValideringsAvbrytendeFeilException {
        var nyFeil = validerPerioderErLukketOgGyldig(periodeList, felt);
        if (!nyFeil.isEmpty()) {
            feil.addAll(nyFeil);
            throw new ValideringsAvbrytendeFeilException(feil);
        }
        try {
            return toLocalDateTimeline(periodeList);
        } catch (IllegalArgumentException e) {
            feil.add(lagFeil(felt, "IllegalArgumentException", e.getMessage()));
            throw new ValideringsAvbrytendeFeilException(feil);
        }
    }

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(Map<Periode, ? > periodeMap, String felt, List<Feil> feil) throws ValideringsAvbrytendeFeilException {
        var nyFeil = validerPerioderErLukketOgGyldig(periodeMap, felt);
        if (!nyFeil.isEmpty()) {
            feil.addAll(nyFeil);
            throw new ValideringsAvbrytendeFeilException(feil);
        }
        try {
            return toLocalDateTimeline(new ArrayList<>(periodeMap.keySet()));
        } catch (IllegalArgumentException e) {
            feil.add(lagFeil(felt, "IllegalArgumentException", e.getMessage()));
            throw new ValideringsAvbrytendeFeilException(feil);
        }
    }

    private List<Feil> validerPerioderErLukketOgGyldig(Map<Periode, ?> perioder, String felt) {
        var feil = new ArrayList<Feil>();
        perioder.keySet().forEach(p -> {
            validerPerioderErLukket(p, felt + "['" + p + "']", feil);
            validerPerioderIkkeErInvertert(p, felt + "['" + p + "']", feil);});
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

    static class ValideringsAvbrytendeFeilException extends RuntimeException {

        private final List<Feil> feilList;

        public ValideringsAvbrytendeFeilException(List<Feil> feilList) {
            this.feilList = feilList;
        }

        public List<Feil> getFeilList() {
            return feilList;
        }
    }
}
