package no.nav.k9.søknad.ytelse.psb.v1;

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

public class PleiepengerSyktBarnValidator extends YtelseValidator {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Override
    public List<Feil> valider(Ytelse ytelse) {
        var psb = (PleiepengerSyktBarn) ytelse;
        var validate = VALIDATOR_FACTORY.getValidator().validate(psb);

        List<Feil> feil = validate.stream()
                .map(this::toFeil)
                .collect(Collectors.toList());

        validerPerioderErKomplettOgGyldig(psb, feil);

        return feil;
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
        var test = toLocalDateTimeline(periodeMap, felt, feil);
        if (feil.isEmpty()) {
            finnPerioderUtenfor(test, tidsserier).valider("beredskap", feil);
        }

        return feil;
    }

    private List<Feil> komplettOginnenforGyldigperiode(TidsserieValidator.Perioder tidsserier, Map<Periode, ?> periodeMap, String felt) {
        var feil = new ArrayList<Feil>();
        var test = toLocalDateTimeline(periodeMap, felt, feil);
        if (feil.isEmpty()) {
            finnIkkeKomplettePerioderOgPerioderUtenfor(test, tidsserier)
                    .valider("uttak", feil);
        }
        return feil;
    }

    private Feil toFeil(ConstraintViolation<PleiepengerSyktBarn> constraintViolation) {
        if (constraintViolation.getInvalidValue() == null) {
            return new Feil(
                    constraintViolation.getPropertyPath().toString(),
                    PÅKREVD,
                    constraintViolation.getMessage());
        }
        return new Feil(
                    constraintViolation.getPropertyPath().toString(),
                    UGYLDIG_ARGUMENT,
                    constraintViolation.getMessage());
    }
}
