package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.PeriodeValidator.validerGyldigPeriode;
import static no.nav.k9.søknad.TidsserieValidator.TidsserieUtils.toLocalDateTimeline;
import static no.nav.k9.søknad.TidsserieValidator.finnIkkeKomplettePerioderOgPerioderUtenfor;
import static no.nav.k9.søknad.TidsserieValidator.finnPerioderUtenfor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import no.nav.k9.søknad.TidsserieValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;

public class PleiepengerSyktBarnYtelseValidator extends YtelseValidator {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Override
    public List<Feil> valider(Ytelse ytelse) {
        var psb = (PleiepengerSyktBarn) ytelse;
        var validate = VALIDATOR_FACTORY.getValidator().validate(psb);

        List<Feil> feil = validate.stream()
                .map(this::toFeil)
                .collect(Collectors.toList());
        manglerIkkeSøknadEllerEndringsPerioder(psb, feil);
        validerKomplettSøknad(psb, feil);
        validerAllePerioder(psb, feil);
        validerPerioderErKomplettOgGyldig(psb, feil);

        return feil;
    }

    private void validerAllePerioder(PleiepengerSyktBarn psb, List<Feil> feil) {
        feil.addAll(validerPerioder(psb.getUtenlandsopphold().getPerioder(), "utenlandsopphold.perioder"));
        feil.addAll(validerPerioder(psb.getBosteder().getPerioder(), "bosteder.perioder"));
        feil.addAll(validerPerioder(psb.getSøknadsperiodeList(), "søknadsperiode"));
        feil.addAll(validerPerioder(psb.getEndringsperiode(), "endringsperiode"));
        feil.addAll(validerPerioder(psb.getBeredskap().getPerioder(), "beredskap.perioder"));
        feil.addAll(validerPerioder(psb.getNattevåk().getPerioder(), "nattevåk.perioder"));
        feil.addAll(validerPerioder(psb.getTilsynsordning().getPerioder(), "tilsynsordning.perioder"));
        feil.addAll(validerPerioder(psb.getLovbestemtFerie().getPerioder(), "lovbestemtFerie.perioder"));
        feil.addAll(validerPerioder(psb.getUttak().getPerioder(), "uttak.perioder"));
        feil.addAll(validerArbeidstid(psb.getArbeidstid()));

    }

    private void validerPerioderErKomplettOgGyldig(PleiepengerSyktBarn psb, List<Feil> feil) {
        var tidsserier = new TidsserieValidator.Perioder(psb.getSøknadsperiodeList(), psb.getEndringsperiode(), feil);
        feil.addAll(innenforGyldigperiode(tidsserier, psb.getBeredskap().getPerioder(), "beredskap.perioder"));
        feil.addAll(innenforGyldigperiode(tidsserier, psb.getNattevåk().getPerioder(), "nattevåk.perioder"));
        feil.addAll(innenforGyldigperiode(tidsserier, psb.getTilsynsordning().getPerioder(), "tilsynsordning.perioder"));
        feil.addAll(innenforGyldigperiode(tidsserier, psb.getLovbestemtFerie().getPerioder(), "lovbestemtFerie.perioder"));
        feil.addAll(komplettOginnenforGyldigperiode(tidsserier, psb.getUttak().getPerioder(), "uttak.perioder"));
        feil.addAll(validerArbeidstidPerioderKomplettOgInnenforGyldigperiode(psb, tidsserier));
    }

    private List<Feil> validerArbeidstidPerioderKomplettOgInnenforGyldigperiode(PleiepengerSyktBarn psb, TidsserieValidator.Perioder tidsserier) {
        var feil = new ArrayList<Feil>();
        if (!psb.getArbeidstid().getArbeidstakerList().isEmpty()){
            for (int i = 0; i < psb.getArbeidstid().getArbeidstakerList().size(); i++) {
                feil.addAll(komplettOginnenforGyldigperiode(tidsserier,
                        psb.getArbeidstid().getArbeidstakerList().get(i).getArbeidstidInfo().getPerioder(),
                        "arbeidstid.arbeidstaker[" + i + "]"));
            }
        }
        if (psb.getArbeidstid().getFrilanserArbeidstidInfo().isPresent()) {
            feil.addAll(komplettOginnenforGyldigperiode(tidsserier,
                    psb.getArbeidstid().getFrilanserArbeidstidInfo().get().getPerioder(),
                    "arbeidstid.frilanserArbeidstidInfo.perioder"));
        }
        if (psb.getArbeidstid().getSelvstendigNæringsdrivendeArbeidstidInfo().isPresent()) {
            feil.addAll(komplettOginnenforGyldigperiode(tidsserier,
                    psb.getArbeidstid().getSelvstendigNæringsdrivendeArbeidstidInfo().get().getPerioder(),
                    "arbeidstid.selvstendigNæringsdrivendeArbeidstidInfo.perioder"));
        }
        return feil;
    }

    private List<Feil> innenforGyldigperiode(TidsserieValidator.Perioder tidsserier, Map<Periode, ?> periodeMap, String felt) {
        var feil = new ArrayList<Feil>();
        finnPerioderUtenfor(
                toLocalDateTimeline(periodeMap, felt, feil), tidsserier)
                .valider(felt, feil);
        return feil;
    }

    private List<Feil> komplettOginnenforGyldigperiode(TidsserieValidator.Perioder tidsserier, Map<Periode, ?> periodeMap, String felt) {
        var feil = new ArrayList<Feil>();
        finnIkkeKomplettePerioderOgPerioderUtenfor(
                toLocalDateTimeline(periodeMap, felt, feil), tidsserier)
                .valider(felt, feil);
        return feil;
    }

    private Feil toFeil(ConstraintViolation<PleiepengerSyktBarn> constraintViolation) {
        return new Feil(
                constraintViolation.getPropertyPath().toString(),
                PÅKREVD,
                constraintViolation.getMessage());
    }

    private void validerKomplettSøknad(PleiepengerSyktBarn psb, List<Feil> feil) {
        if (psb.getBarn() == null) {
            feil.add(new Feil("barn", "missingArgument","Barn kan ikke være null."));
        }
    }

    private void manglerIkkeSøknadEllerEndringsPerioder(PleiepengerSyktBarn psb, List<Feil> feil) {
        if ( (psb.getSøknadsperiodeList().isEmpty() && psb.getEndringsperiode().isEmpty())) {
            feil.add(new Feil("søknadsperiode/endringsperiode", "missingArgument","Mangler søknadsperiode eller endringsperiode."));
        }
    }

    public static List<Feil> validerArbeidstid(Arbeidstid arbeidstid) {
        var feil = new ArrayList<Feil>();
        if (!arbeidstid.getArbeidstakerList().isEmpty()) {
            feil.addAll(validerArbeidstakerList(arbeidstid.getArbeidstakerList()));
        }
        if (arbeidstid.getFrilanserArbeidstidInfo().isPresent()) {
            feil.addAll(validerPerioder(arbeidstid.getFrilanserArbeidstidInfo().get().getPerioder(),
                    "arbeidstid.frilanserArbeidstidInfo.perioder"));
        }
        if (arbeidstid.getSelvstendigNæringsdrivendeArbeidstidInfo().isPresent()) {
            feil.addAll(validerPerioder(arbeidstid.getSelvstendigNæringsdrivendeArbeidstidInfo().get().getPerioder(),
                    "arbeidstid.selvstendigNæringsdrivendeArbeidstidInfo.perioder"));
        }
        return feil;
    }

    public static ArrayList<Feil> validerArbeidstakerList(List<Arbeidstaker> arbeidstakers) {
        var feil = new ArrayList<Feil>();
        for (int i = 0; i < arbeidstakers.size(); i++) {
            feil.addAll(validerPerioder(arbeidstakers.get(i).getArbeidstidInfo().getPerioder(),
                    "arbeidstid.arbeidstaker[" + i + "].perioder"));
        }
        return feil;
    }

    public static List<Feil> validerPerioder(List<Periode> perioder, String felt) {
        var feil = new ArrayList<Feil>();
        for (int i = 0; i < perioder.size(); i++ ) {
            validerGyldigPeriode(perioder.get(i), felt + "[" + i + "]", false, feil);
        }
        return feil;
    }

    public static List<Feil> validerPerioder(Map<Periode, ?> periodeMap, String felt) {
        var feil = new ArrayList<Feil>();
        var i = 0;
        periodeMap.forEach((periode, o) -> {
            validerGyldigPeriode(periode, felt + "[" + i + "]", false, feil);
        });
        return feil;
    }
}
