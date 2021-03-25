package no.nav.k9.søknad.ytelse.psb.v1;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.PsbArbeidstaker;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.stream.Collectors;

import static no.nav.k9.søknad.TidsserieValidator.TidsserieUtils.toLocalDateTimeline;
import static no.nav.k9.søknad.TidsserieValidator.finnIkkeKomplettePerioderOgPerioderUtenfor;
import static no.nav.k9.søknad.TidsserieValidator.finnPerioderUtenfor;

public class PleiepengerSyktBarnValidator extends YtelseValidator {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Override
    public List<Feil> valider(Ytelse ytelse) {
        var psb = (PleiepengerSyktBarn) ytelse;
        var validate = VALIDATOR_FACTORY.getValidator().validate(psb);

        List<Feil> feil = validate.stream()
                .map(this::toFeil)
                .collect(Collectors.toList());

        try {
                var søknadsperiode = validerSøknadsperiode(psb.getSøknadsperiode());
                validerBeredskap(psb.getBeredskap(), søknadsperiode, feil);
                validerUttak(psb.getUttak(), søknadsperiode, feil);
                validerNattevåk(psb.getNattevåk(), søknadsperiode, feil);
                validerTilsynsordning(psb.getTilsynsordning(), søknadsperiode, feil);
                validerLovbestemtFerie(psb.getLovbestemtFerie(), søknadsperiode, feil);
                validerArbeidstid(psb.getArbeidstid(), søknadsperiode, feil);
                //TODO validerOpptjeningAktivitet


        }catch (IllegalArgumentException e) {
            feil.add(new Feil(e.getClass().getName(), e.getCause() + "IllegalArgumentException", e.getMessage()));
        }

        return feil;
    }

    private Feil toFeil(ConstraintViolation<PleiepengerSyktBarn> constraintViolation) {
        return new Feil(
                constraintViolation.getPropertyPath().toString(),
                PÅKREVD,
                constraintViolation.getMessage());
    }

    private LocalDateTimeline<Boolean> validerSøknadsperiode(Periode søknadsperiode) {
        //Validerer at det ikke er overlapp i perioden
        return toLocalDateTimeline(List.of(søknadsperiode));
    }

    private void validerBeredskap(Beredskap beredskap, LocalDateTimeline<Boolean> søknadsperiode, List<Feil> feil) {
        if (beredskap == null) {
            return;
        }
        finnPerioderUtenfor(
                toLocalDateTimeline(beredskap.getPerioder()),
                søknadsperiode).valider("beredskap", feil);
    }

    private void validerUttak(Uttak uttak, LocalDateTimeline<Boolean> søknadsperiode, List<Feil> feil) {
        if (uttak == null) {
            return;
        }
        finnIkkeKomplettePerioderOgPerioderUtenfor(
                toLocalDateTimeline(uttak.getPerioder()),
                søknadsperiode)
                .valider("uttak", feil);
    }

    private void validerLovbestemtFerie(LovbestemtFerie lovbestemtFerie, LocalDateTimeline<Boolean> søknadsperiode, List<Feil> feil) {
        if (lovbestemtFerie == null) {
            return;
        }
        finnPerioderUtenfor(
                toLocalDateTimeline(lovbestemtFerie.getPerioder()),
                søknadsperiode).valider("lovbestemtFerie", feil);
    }

    private void validerNattevåk(Nattevåk nattevåk, LocalDateTimeline<Boolean> søknadsperiode,  List<Feil> feil) {
        if (nattevåk == null) {
            return;
        }
        finnPerioderUtenfor(
                toLocalDateTimeline(nattevåk.getPerioder()),
                søknadsperiode)
                .valider("nattevåk", feil);
    }

    private void validerArbeidstid(Arbeidstid arbeidstid, LocalDateTimeline<Boolean> søknadsperiode, List<Feil> feil) {
        if (arbeidstid == null) {
            return;
        }
        validerArbeidstaker(arbeidstid.getArbeidstakerList(), søknadsperiode, feil);
        validerFrilanser(arbeidstid.getFrilanserArbeidstidInfo(), søknadsperiode, feil);
        validerSelvstendigNæringsdrivende(arbeidstid.getSelvstendigNæringsdrivendeArbeidstidInfo(), søknadsperiode, feil);
    }

    private void validerArbeidstaker(List<PsbArbeidstaker> arbeidstakerList, LocalDateTimeline<Boolean> søknadsperiode, List<Feil> feil) {
        if (arbeidstakerList == null) {
            return;
        }
        for (PsbArbeidstaker arbeidstaker : arbeidstakerList ) {
            arbeidstaker.valider("arbeidstid.arbeidstaker", feil);
            finnIkkeKomplettePerioderOgPerioderUtenfor(
                    toLocalDateTimeline(arbeidstaker.getArbeidstidInfo().getPerioder()), søknadsperiode)
                    .valider("arbeidstid.arbeidstaker", feil);
        }
    }

    private void validerFrilanser(ArbeidstidInfo frilanser, LocalDateTimeline<Boolean> søknadsperiode, List<Feil> feil) {
        if (frilanser == null) {
            return;
        }
        finnIkkeKomplettePerioderOgPerioderUtenfor(
                toLocalDateTimeline(frilanser.getPerioder()),
                søknadsperiode)
                .valider("arbeidstid.frilanser", feil);
    }

    private void validerSelvstendigNæringsdrivende(ArbeidstidInfo selvstendigNæringsdrivende, LocalDateTimeline<Boolean> søknadsperiode, List<Feil> feil) {
        if (selvstendigNæringsdrivende == null) {
            return;
        }
        finnIkkeKomplettePerioderOgPerioderUtenfor(
                toLocalDateTimeline(selvstendigNæringsdrivende.getPerioder()),
                søknadsperiode)
                .valider("arbeidstid.frilanser", feil);
    }

    private void validerTilsynsordning(Tilsynsordning tilsynsordning, LocalDateTimeline<Boolean> søknadsperiode, List<Feil> feil) {
        if (tilsynsordning == null) {
            return;
        }
        finnPerioderUtenfor(
                toLocalDateTimeline(tilsynsordning.getPerioder()),
                søknadsperiode)
                .valider("tilsynsordning", feil);
    }
}
