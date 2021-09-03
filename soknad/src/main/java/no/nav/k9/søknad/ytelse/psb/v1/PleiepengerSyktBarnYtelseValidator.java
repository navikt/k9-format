package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;
import static no.nav.k9.søknad.TidsserieValidator.finnIkkeKomplettePerioder;
import static no.nav.k9.søknad.TidsserieValidator.finnPerioderUtenforGyldigInterval;
import static no.nav.k9.søknad.ytelse.psb.v1.EndringsperiodeKalkulator.YtelsePerioder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.fpsak.tidsserie.StandardCombinators;
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

    public List<Feil> valider(Ytelse ytelse, List<Periode> gyldigEndringsPerioder) {
        var psb = (PleiepengerSyktBarn) ytelse;
        var validate = VALIDATOR_FACTORY.getValidator().validate(psb);

        List<Feil> feil = validate.stream()
                .map(this::toFeil)
                .collect(Collectors.toList());

        feil.addAll(manglerIkkeSøknadEllerEndringsPerioder(psb, gyldigEndringsPerioder));
        feil.addAll(validerKomplettSøknad(psb));

        var ytelsePerioder = EndringsperiodeKalkulator.getYtelsePerioder(psb);
        feil.addAll(validerPerioderErGyldig(psb.getSøknadsperiodeList(), "søknadsperiode"));
        feil.addAll(validerPerioderErGyldig(gyldigEndringsPerioder, "gyldigEndringsPerioder"));
        feil.addAll(validerPerioderErGyldig(ytelsePerioder));

        var søknadsperiode = toLocalDateTimeline(psb.getSøknadsperiodeList(), "søknadsperiode", feil);
        var gyldigInterval = søknadsperiode.union(
                toLocalDateTimeline(gyldigEndringsPerioder, "gyldigEndringsPerioder", feil),
                StandardCombinators::coalesceLeftHandSide);

        feil.addAll(innenforGyldigPeriode(gyldigInterval, ytelsePerioder));
        feil.addAll(periodeneErKomplett(søknadsperiode, psb.getUttak().getPerioder(), "uttak"));

        return feil;
    }

    private List<Feil> innenforGyldigPeriode(LocalDateTimeline<Boolean> gyldigInterval, List<YtelsePerioder> ytelsePerioderList) {
        var feil = new ArrayList<Feil>();
        for (var ytelsePerioder : ytelsePerioderList) {
            feil.addAll(finnPerioderUtenforGyldigInterval(
                    toLocalDateTimeline(ytelsePerioder.getPeriodeList(), ytelsePerioder.getFelt(), feil),
                    gyldigInterval,
                    ytelsePerioder.getFelt()));
        }
        return feil;
    }

    private List<Feil> periodeneErKomplett(LocalDateTimeline<Boolean> intervalHvorPeriodeMåVæreKomplett, List<YtelsePerioder> ytelsePerioderList) {
        var feil = new ArrayList<Feil>();
        for (var ytelsePerioder : ytelsePerioderList) {
            feil.addAll(finnIkkeKomplettePerioder(
                    toLocalDateTimeline(ytelsePerioder.getPeriodeList(), ytelsePerioder.getFelt(), feil),
                    intervalHvorPeriodeMåVæreKomplett, ytelsePerioder.getFelt()));
        }
        return feil;
    }

    private List<Feil> periodeneErKomplett(LocalDateTimeline<Boolean> intervalHvorPeriodeMåVæreKomplett, Map<Periode, ?> test, String felt) {
        var feil = new ArrayList<Feil>();
        feil.addAll(finnIkkeKomplettePerioder(
                toLocalDateTimeline(test, felt, feil),
                intervalHvorPeriodeMåVæreKomplett, felt));
        return feil;
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

    private List<Feil> manglerIkkeSøknadEllerEndringsPerioder(PleiepengerSyktBarn psb, List<Periode> gyldigEndringsPerioder) {
        var feil = new ArrayList<Feil>();
        if ( (psb.getSøknadsperiodeList().isEmpty() && gyldigEndringsPerioder.isEmpty())) {
            feil.add(new Feil("søknadsperiode/gyldigEndringsPerioder", "missingArgument","Mangler søknadsperiode eller gyldigEndringsPerioder."));
        }
        return feil;
    }

    public static List<Feil> validerPerioderErGyldig(List<Periode> perioder, String felt) {
        var feil = new ArrayList<Feil>();
        for (int i = 0; i < perioder.size(); i++ ) {
            validerGyldigPeriode(perioder.get(i), felt + "[" + i + "]", feil);
        }
        return feil;
    }

    public static List<Feil> validerPerioderErGyldig(List<YtelsePerioder> ytelsePerioderList) {
        var feil = new ArrayList<Feil>();
        for (var ytelsePerioder : ytelsePerioderList) {
            feil.addAll(validerPerioderErGyldig(ytelsePerioder.getPeriodeList(), ytelsePerioder.getFelt()));
        }
        return feil;
    }

    public static void validerGyldigPeriode(Periode periode, String felt, List<Feil> feil) {
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
