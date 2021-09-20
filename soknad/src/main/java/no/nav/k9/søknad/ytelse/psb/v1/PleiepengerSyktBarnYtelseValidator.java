package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;
import static no.nav.k9.søknad.TidsserieUtils.toPeriodeList;

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
        feil.addAll(inneholderSøknadsperiodeEllerGyldigeEndringsperioder(psb, psb.getEndringsperiode()));
        feil.addAll(validerKomplettSøknad(psb));

        //Valider at alle perioder er lukket
        validerAtAllePerioderErLukket(gyldigeEndringsperioder, psb, feil);

        //Perioder -> tidslinje
        var tidslinjeList = new ArrayList<PerioderMedEndringTidslinje>();
        tidslinjeList.add(new PerioderMedEndringTidslinje().medP)

        var søknadsperiode = toLocalDateTimeline(psb.getSøknadsperiodeList(), YTELSE_FELT + "søknadsperiode", feil);
        var intervalForEndring = søknadsperiode.union(
                toLocalDateTimeline(gyldigeEndringsperioder, "gyldigeEndringsperioder", feil),
                StandardCombinators::coalesceLeftHandSide);

        //TODO: Slette når endringerperioder utledes.
        intervalForEndring = intervalForEndring.union(
                toLocalDateTimeline(psb.getEndringsperiode(), YTELSE_FELT + "endringsperiode", feil),
                StandardCombinators::coalesceLeftHandSide);
        //TODO: Slette når endringerperioder utledes.

        //Lag alle perioder om til tidslinje
        var perioderSomMåVæreInnenforSøknadsperiode = new PerioderMedEndringTidslinje()
                .tilPerioderMedEndringTidslinje(
                        PerioderMedEndringUtil.getAllePerioderSomMåVæreInnenforSøknadsperiode(psb),
                        feil);

        //Valider at intervalForEndring ikke er tom. Interval for endring er = søkandsperiode + gyldigeEndringsperioder
        //valider at søkand er komplett

        //Valider at perioder er innenfor intervalForEndring
        //Valider at perioder med endring ikke er innenfor trekkKravPeroder
        //Valider at perioder er komplett med nødvendig interval


        feil.addAll(inneholderSøknadsperiodeEllerGyldigeEndringsperioder(psb, gyldigeEndringsperioder));




        var trekkKravPerioder = toLocalDateTimeline(psb.getTrekkKravPerioder(), YTELSE_FELT + "trekkKravPerioder", feil);

        feil.addAll(finnPerioderInnenforTrekkKrav(trekkKravPerioder, søknadsperiode, YTELSE_FELT + "søknadperiode"));
        innenforGyldigIntervalForEndringOgIkkeInnenforTrekkAvKrav(intervalForEndring,
                trekkKravPerioder,
                PerioderMedEndringUtil.getAllePerioderSomMåVæreInnenforSøknadsperiode(psb),
                feil);
        periodeneErKomplett(søknadsperiode, psb.getUttak().getPerioder(), YTELSE_FELT + "uttak", feil);

        return feil;
    }

    private void validerAtAllePerioderErLukket(List<Periode> gyldigeEndringsperioder, PleiepengerSyktBarn psb, List<Feil> feil) {
        validerPerioderErLukket(gyldigeEndringsperioder, "gyldigeEndringsperioder", feil);
        validerPerioderErLukket(psb.getSøknadsperiodeList(), YTELSE_FELT + "søknadsperiode", feil);
        validerPerioderErLukket(psb.getEndringsperiode(), YTELSE_FELT + "endringsperiode", feil);
        validerPerioderErLukket(psb.getTrekkKravPerioder(), YTELSE_FELT + "trekkKravPerioder", feil);
        validerPerioderErLukket(psb.getBosteder().getPerioder(), YTELSE_FELT + "bosteder", feil);
        PerioderMedEndringUtil.getAllePerioderSomMåVæreInnenforSøknadsperiode(psb)
                .forEach(p -> validerPerioderErLukket(p.getPeriodeList(), p.getFelt(), feil));
    }

    private void innenforGyldigIntervalForEndringOgIkkeInnenforTrekkAvKrav(LocalDateTimeline<Boolean> gyldigInterval,
                                                                                 LocalDateTimeline<Boolean> trekkKrav,
                                                                                 List<PerioderMedEndringTidslinje> perioderMedEndringList,
                                                                                 List<Feil> feil) {
        for (var ytelsePeriode : perioderMedEndringList) {
            var ytelsePeriodeTidsserie = toLocalDateTimeline(ytelsePeriode.getPeriodeTimeline(), ytelsePeriode.getFelt(), feil);
            feil.addAll(finnPerioderUtenforGyldigInterval(gyldigInterval, ytelsePeriodeTidsserie, ytelsePeriode.getFelt()));
            feil.addAll(finnPerioderInnenforTrekkKrav(trekkKrav, ytelsePeriodeTidsserie, ytelsePeriode.getFelt()));
        }
    }

    private List<Feil> finnPerioderUtenforGyldigInterval(LocalDateTimeline<Boolean> gyldigInterval,
                                                         LocalDateTimeline<Boolean> testTidsserie,
                                                         String felt) {
        var perioderUtenfor = toPeriodeList(
                testTidsserie.disjoint(gyldigInterval));
        return perioderUtenfor.stream()
                .map(p -> toFeil(p, felt + ".perioder", "ugyldigPeriode", "Perioden er utenfor gyldig interval(" + gyldigInterval.toString() + ") : "))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void periodeneErKomplett(LocalDateTimeline<Boolean> intervalHvorPeriodeMåVæreKomplett,
                                           Map<Periode, ?> testPerioder,
                                           String felt,
                                           List<Feil> feil) {
        var testPerioderTidsserie = toLocalDateTimeline(testPerioder, felt, feil);
        feil.addAll(finnIkkeKomplettePerioder(
                intervalHvorPeriodeMåVæreKomplett,
                testPerioderTidsserie, felt));
    }

    private List<Feil> finnPerioderInnenforTrekkKrav(LocalDateTimeline<Boolean> trekkKravPerioder,
                                                     LocalDateTimeline<Boolean> test, String felt) {
        var perioderInnenforTrekkKrav = toPeriodeList(trekkKravPerioder.intersection(test));

        return perioderInnenforTrekkKrav.stream()
                .map(p -> toFeil(p, felt + ".perioder", "ugyldigTrekkKrav", "Overlapper med trekk krav periode: "))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Feil> finnIkkeKomplettePerioder(LocalDateTimeline<Boolean> intervalHvorTidserieMåVæreKomplett,
                                                 LocalDateTimeline<Boolean> test, String felt) {
        var ikkeKomplettePerioder = toPeriodeList(intervalHvorTidserieMåVæreKomplett.disjoint(test));

        return ikkeKomplettePerioder.stream()
                .filter(this::periodeInneholderDagerSomIkkeErHelg)
                .map(p -> toFeil(p, felt + ".perioder", "ikkeKomplettPeriode", "Periodene er ikke komplett, periode som mangler er: "))
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
        return new Feil(felt, feilkode, feilmelding + periode.toString());
    }

    private Feil toFeil(ConstraintViolation<PleiepengerSyktBarn> constraintViolation) {
        return new Feil(
                constraintViolation.getPropertyPath().toString(),
                PÅKREVD,
                constraintViolation.getMessage());
    }

    private List<Feil> validerKomplettSøknad(PleiepengerSyktBarn psb) {
        var feil = new ArrayList<Feil>();
        if (psb.getBarn() == null) {
            feil.add(new Feil("barn", "missingArgument","Barn kan ikke være null."));
        }
        return feil;
    }

    private void inneholderSøknadsperiodeEllerGyldigeEndringsperioder(PleiepengerSyktBarn psb, List<Periode> gyldigeEndringsperioder, List<Feil> feil) {
        if (psb.getSøknadsperiodeList().isEmpty() && gyldigeEndringsperioder.isEmpty()) {
            feil.add(new Feil("søknadsperiode/gyldigeEndringsperioder", "missingArgument","Mangler søknadsperiode eller gyldigeEndringsperioder.")));
        }
    }

    private void validerPerioderErLukket(Map<Periode, ?> periodeMap, String felt, List<Feil> feil) {
        validerPerioderErLukket(new ArrayList<>(periodeMap.keySet()), felt, feil);
    }

    private void validerPerioderErLukket(List<Periode> periodeList, String felt, List<Feil> feil) {
        periodeList.forEach(p -> erLukket(p, felt, feil));
    }

    private void erLukket(Periode p, String felt, List<Feil> feil) {
        if (p == null) {
            return;
        }
        if (p.getTilOgMed() == null) {
            feil.add(new Feil(felt, "påkrevd", "Til og med (TOM) må være satt."));
        }
        if (p.getFraOgMed() == null) {
            feil.add(new Feil(felt, "påkrevd", "Fra og med (FOM) må være satt."));
        }
    }


    private List<Feil> validerPerioderErLukketOgIkkeFeilRekkefølge(List<PerioderMedEndringTidslinje> perioderMedEndringList) {
        var feil = new ArrayList<Feil>();
        for (var ytelsePerioder : perioderMedEndringList) {
            feil.addAll(validerPerioderErLukketOgIkkeFeilRekkefølge(ytelsePerioder.getPeriodeTimeline(), ytelsePerioder.getFelt()));
        }
        return feil;
    }

    private List<Feil> validerPerioderErLukketOgIkkeFeilRekkefølge(Map<Periode, ?> perioder, String felt) {
        return validerPerioderErLukketOgIkkeFeilRekkefølge(new ArrayList<>(perioder.keySet()), felt);
    }

    private List<Feil> validerPerioderErLukketOgIkkeFeilRekkefølge(List<Periode> perioder, String felt) {
        var feil = new ArrayList<Feil>();
        for (int i = 0; i < perioder.size(); i++ ) {
            validerPerioderErLukketOgIkkeFeilRekkefølge(perioder.get(i), felt + ".perioder[" + i + "]", feil);
        }
        return feil;
    }

    private void validerPerioderErLukketOgIkkeFeilRekkefølge(Periode periode, String felt, List<Feil> feil) {
        if (periode == null) {
            return;
        }
        if (periode.getTilOgMed() == null) {
            feil.add(new Feil(felt, "påkrevd", "Til og med (TOM) må være satt."));
        }
        if (periode.getFraOgMed() == null) {
            feil.add(new Feil(felt, "påkrevd", "Fra og med (FOM) må være satt."));
        }
//        if (periode.getFraOgMed() != null && periode.getTilOgMed() != null && periode.getTilOgMed().isBefore(periode.getFraOgMed())) {
//            feil.add(new Feil(felt, "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM)."));
//        }
    }

    private static class PerioderMedEndringTidslinje {
        private String felt;
        private LocalDateTimeline<Boolean> periodeTimeline;

        public PerioderMedEndringTidslinje(String felt, Map<Periode, ?> periodeMap, List<Feil> feil) {
            this.felt
            medPerioder(felt, new ArrayList<>(periodeMap.keySet()), feil);
        }

        public PerioderMedEndringTidslinje(String felt, List<Periode> periodeList, List<Feil> feil) {
            this.felt = felt;
            this.periodeTimeline = toLocalDateTimeline(periodeList, felt, feil);
        }


        public String getFelt() {
            return felt;
        }

        public LocalDateTimeline<Boolean> getPeriodeTimeline() {
            return periodeTimeline;
        }

        private List<PerioderMedEndringTidslinje> tilPerioderMedEndringTidslinje(List<PerioderMedEndringUtil.PerioderMedEndring> perioderMedEndringList, List<Feil> feil) {
            return perioderMedEndringList.stream()
                    .map(p -> tilPerioderMedEndringTidslinje(p, feil))
                    .collect(Collectors.toList());
        }

        private PerioderMedEndringTidslinje tilPerioderMedEndringTidslinje(PerioderMedEndringUtil.PerioderMedEndring perioderMedEndring, List<Feil> feil) {
            return new PerioderMedEndringTidslinje().medPerioder(perioderMedEndring.getFelt(), perioderMedEndring.getPeriodeList(), feil);
        }
    }

}
