package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;
import static no.nav.k9.søknad.TidsserieUtils.toPeriodeList;
import static no.nav.k9.søknad.ytelse.psb.v1.PerioderMedEndringUtil.PerioderMedEndring;

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

        feil.addAll(validerPerioderErLukketOgIkkeFeilRekkefølge(gyldigeEndringsperioder, "gyldigeEndringsperioder"));
        feil.addAll(validerPerioderErLukketOgIkkeFeilRekkefølge(psb.getSøknadsperiodeList(), "søknadsperioder"));
        feil.addAll(validerPerioderErLukketOgIkkeFeilRekkefølge(psb.getTrekkKravPerioder(), "trekkKravPerioder"));
        feil.addAll(validerPerioderErLukketOgIkkeFeilRekkefølge(psb.getBosteder().getPerioder(), "bosteder"));
        feil.addAll(validerPerioderErLukketOgIkkeFeilRekkefølge(PerioderMedEndringUtil.getAllePerioderSomMåVæreInnenforSøknadsperiode(psb)));

        var søknadsperiode = toLocalDateTimeline(psb.getSøknadsperiodeList(), "søknadsperiode", feil);
        var gyldigeIntervalForEndring = søknadsperiode.union(
                toLocalDateTimeline(gyldigeEndringsperioder, "gyldigeEndringsperioder", feil),
                StandardCombinators::coalesceLeftHandSide);

        //TODO: Slette når endringerperioder utledes.
        gyldigeIntervalForEndring = gyldigeIntervalForEndring.union(
                toLocalDateTimeline(psb.getEndringsperiode(), "endringsperioder", feil),
                StandardCombinators::coalesceLeftHandSide);
        //TODO: Slette når endringerperioder utledes.


        var trekkKravPerioder = toLocalDateTimeline(psb.getTrekkKravPerioder(), "trekkKravPerioder", feil);

        feil.addAll(finnPerioderInnenforTrekkKrav(trekkKravPerioder, søknadsperiode, "søknadperiode"));

        feil.addAll(innenforGyldigIntervalForEndringOgIkkeInnenforTrekkAvKrav(gyldigeIntervalForEndring,
                trekkKravPerioder,
                PerioderMedEndringUtil.getAllePerioderSomMåVæreInnenforSøknadsperiode(psb)));
        feil.addAll(periodeneErKomplett(søknadsperiode, psb.getUttak().getPerioder(), "uttak"));

        return feil;
    }

    private List<Feil> innenforGyldigIntervalForEndringOgIkkeInnenforTrekkAvKrav(LocalDateTimeline<Boolean> gyldigInterval,
                                                                                 LocalDateTimeline<Boolean> trekkKrav,
                                                                                 List<PerioderMedEndring> perioderMedEndringList) {
        var feil = new ArrayList<Feil>();
        for (var ytelsePeriode : perioderMedEndringList) {
            var ytelsePeriodeTidsserie = toLocalDateTimeline(ytelsePeriode.getPeriodeList(), ytelsePeriode.getFelt(), feil);
            feil.addAll(finnPerioderUtenforGyldigInterval(gyldigInterval, ytelsePeriodeTidsserie, ytelsePeriode.getFelt()));
            feil.addAll(finnPerioderInnenforTrekkKrav(trekkKrav, ytelsePeriodeTidsserie, ytelsePeriode.getFelt()));
        }
        return feil;
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

    private List<Feil> periodeneErKomplett(LocalDateTimeline<Boolean> intervalHvorPeriodeMåVæreKomplett,
                                           Map<Periode, ?> testPerioder,
                                           String felt) {
        var feil = new ArrayList<Feil>();
        var testPerioderTidsserie = toLocalDateTimeline(testPerioder, felt, feil);
        feil.addAll(finnIkkeKomplettePerioder(
                intervalHvorPeriodeMåVæreKomplett,
                testPerioderTidsserie, felt));
        return feil;
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

    private List<Feil> inneholderSøknadsperiodeEllerGyldigeEndringsperioder(PleiepengerSyktBarn psb, List<Periode> gyldigeEndringsperioder) {
        var feil = new ArrayList<Feil>();
        if ( (psb.getSøknadsperiodeList().isEmpty() && gyldigeEndringsperioder.isEmpty())) {
            feil.add(new Feil("søknadsperiode/gyldigeEndringsperioder", "missingArgument","Mangler søknadsperiode eller gyldigeEndringsperioder."));
        }
        return feil;
    }

    private static List<Feil> validerPerioderErLukketOgIkkeFeilRekkefølge(List<PerioderMedEndring> perioderMedEndringList) {
        var feil = new ArrayList<Feil>();
        for (var ytelsePerioder : perioderMedEndringList) {
            feil.addAll(validerPerioderErLukketOgIkkeFeilRekkefølge(ytelsePerioder.getPeriodeList(), ytelsePerioder.getFelt()));
        }
        return feil;
    }

    private static List<Feil> validerPerioderErLukketOgIkkeFeilRekkefølge(Map<Periode, ?> perioder, String felt) {
        return validerPerioderErLukketOgIkkeFeilRekkefølge(new ArrayList<>(perioder.keySet()), felt);
    }

    private static List<Feil> validerPerioderErLukketOgIkkeFeilRekkefølge(List<Periode> perioder, String felt) {
        var feil = new ArrayList<Feil>();
        for (int i = 0; i < perioder.size(); i++ ) {
            validerPerioderErLukketOgIkkeFeilRekkefølge(perioder.get(i), felt + "[" + i + "]", feil);
        }
        return feil;
    }

    private static void validerPerioderErLukketOgIkkeFeilRekkefølge(Periode periode, String felt, List<Feil> feil) {
        if (periode == null) {
            return;
        }
        if (periode.getTilOgMed() == null) {
            feil.add(new Feil(felt, "påkrevd", "Til og med (TOM) må være satt."));
        }
        if (periode.getFraOgMed() == null) {
            feil.add(new Feil(felt, "påkrevd", "Fra og med (FOM) må være satt."));
        }
        if (periode.getFraOgMed() != null && periode.getTilOgMed() != null && periode.getTilOgMed().isBefore(periode.getFraOgMed())) {
            feil.add(new Feil(felt, "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM)."));
        }
    }

}
