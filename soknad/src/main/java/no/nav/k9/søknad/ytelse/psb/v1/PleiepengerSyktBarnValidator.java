package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TidsserieValidator.TidsserieUtils.toLocalDateTimeline;
import static no.nav.k9.søknad.TidsserieValidator.finnIkkeKomplettePerioderOgPerioderUtenfor;
import static no.nav.k9.søknad.TidsserieValidator.finnPerioderUtenfor;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import static no.nav.k9.søknad.TidsserieValidator.TidsserieUtils.toLocalDateTimeline;
import static no.nav.k9.søknad.TidsserieValidator.finnIkkeKomplettePerioderOgPerioderUtenfor;
import static no.nav.k9.søknad.TidsserieValidator.finnPerioderUtenfor;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import no.nav.k9.søknad.PeriodeValidator;
import no.nav.k9.søknad.TidsserieValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

public class PleiepengerSyktBarnValidator extends YtelseValidator {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Override
    public List<Feil> valider(Ytelse ytelse) {
        var psb = (PleiepengerSyktBarn) ytelse;
        var validate = VALIDATOR_FACTORY.getValidator().validate(psb);

        List<Feil> feil = validate.stream()
                .map(this::toFeil)
                .collect(Collectors.toList());

        var tidsserier = validerSøknadsOgEndringsPerioder(psb, feil);
        validerBeredskap(psb.getBeredskap(), tidsserier, feil);
        validerUttak(psb.getUttak(), tidsserier, feil);
        validerNattevåk(psb.getNattevåk(), tidsserier, feil);
        validerTilsynsordning(psb.getTilsynsordning(), tidsserier, feil);
        validerLovbestemtFerie(psb.getLovbestemtFerie(), tidsserier, feil);
        validerArbeidstid(psb.getArbeidstid(), tidsserier, feil);

        //TODO valider OpptjeningAktivitet ??
        //TODO valider Omsorg
        //TODO valider Bosterder
        //TODO valider Utlandsopphold

        validerKomplettSøknad(psb, feil);
        //TODO validere at felter som bare kan være i en søknad er satt hvis det er en søknadsperiode.

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

    private TidsserieValidator.Perioder validerSøknadsOgEndringsPerioder(PleiepengerSyktBarn psb, List<Feil> feil) {
        manglerIkkeSøknadEllerEndringsPerioder(psb, feil);
        return new TidsserieValidator.Perioder(psb.getSøknadsperiodeList(), psb.getEndringsperiode(), feil);
    }

    private void manglerIkkeSøknadEllerEndringsPerioder(PleiepengerSyktBarn psb, List<Feil> feil) {
        if ( (psb.getSøknadsperiodeList().isEmpty() && psb.getEndringsperiode().isEmpty())) {
            feil.add(new Feil("søknadsperiode/endringsperiode", "missingArgument","Mangler søknadsperiode eller endringsperiode."));
        }
    }

    private void validerBeredskap(Beredskap beredskap, TidsserieValidator.Perioder perioder, List<Feil> feil) {
        finnPerioderUtenfor(
                toLocalDateTimeline(beredskap.getPerioder(), "beredskap.periode", feil), perioder)
                .valider("beredskap", feil);

    }

    private void validerUttak(Uttak uttak, TidsserieValidator.Perioder perioder, List<Feil> feil) {
        finnIkkeKomplettePerioderOgPerioderUtenfor(
                toLocalDateTimeline(uttak.getPerioder(), "uttak.periode", feil), perioder)
                .valider("uttak", feil);
    }

    private void validerLovbestemtFerie(LovbestemtFerie lovbestemtFerie, TidsserieValidator.Perioder søknadsperiode, List<Feil> feil) {
        finnPerioderUtenfor(
                toLocalDateTimeline(lovbestemtFerie.getPerioder(), "lovbestemtFerie.periode", feil),
                søknadsperiode).valider("lovbestemtFerie", feil);
    }

    private void validerNattevåk(Nattevåk nattevåk, TidsserieValidator.Perioder søknadsperiode, List<Feil> feil) {
        finnPerioderUtenfor(
                toLocalDateTimeline(nattevåk.getPerioder(), "nattevåk.periode", feil), søknadsperiode)
                .valider("nattevåk", feil);
    }

    private void validerArbeidstid(Arbeidstid arbeidstid, TidsserieValidator.Perioder søknadsperiode, List<Feil> feil) {
        validerArbeidstaker(arbeidstid.getArbeidstakerList(), søknadsperiode, feil);
        if (arbeidstid.getFrilanserArbeidstidInfo().isPresent()) {
            validerFrilanser(arbeidstid.getFrilanserArbeidstidInfo().get(), søknadsperiode, feil);
        }
        if (arbeidstid.getSelvstendigNæringsdrivendeArbeidstidInfo().isPresent()) {
            validerSelvstendigNæringsdrivende(arbeidstid.getSelvstendigNæringsdrivendeArbeidstidInfo().get(), søknadsperiode, feil);
        }
    }

    private void validerArbeidstaker(List<Arbeidstaker> arbeidstakerList, TidsserieValidator.Perioder søknadsperiode, List<Feil> feil) {
        if (arbeidstakerList.isEmpty()) {
            return;
        }
        for (Arbeidstaker arbeidstaker : arbeidstakerList ) {
            arbeidstaker.valider("arbeidstid.arbeidstaker", feil);
            finnIkkeKomplettePerioderOgPerioderUtenfor(
                    toLocalDateTimeline(arbeidstaker.getArbeidstidInfo().getPerioder(), "arbeidstid.arbeidstaker.periode", feil), søknadsperiode)
                    .valider("arbeidstid.arbeidstaker", feil);
        }
    }

    private void validerFrilanser(ArbeidstidInfo frilanser, TidsserieValidator.Perioder søknadsperiode, List<Feil> feil) {
        finnIkkeKomplettePerioderOgPerioderUtenfor(
                toLocalDateTimeline(frilanser.getPerioder(), "arbeistid.frilans.periode", feil),
                søknadsperiode)
                .valider("arbeidstid.frilanser", feil);
    }

    private void validerSelvstendigNæringsdrivende(ArbeidstidInfo selvstendigNæringsdrivende, TidsserieValidator.Perioder søknadsperiode, List<Feil> feil) {
        finnIkkeKomplettePerioderOgPerioderUtenfor(
                toLocalDateTimeline(selvstendigNæringsdrivende.getPerioder(), "arbeidstid.selvstendigNæringsdivende.periode", feil),
                søknadsperiode)
                .valider("arbeidstid.selvstendigNæringsdrivende", feil);
    }

    private void validerTilsynsordning(Tilsynsordning tilsynsordning, TidsserieValidator.Perioder søknadsperiode, List<Feil> feil) {
        finnPerioderUtenfor(
                toLocalDateTimeline(tilsynsordning.getPerioder(), "tilsynsordning.periode", feil),
                søknadsperiode)
                .valider("tilsynsordning", feil);
    }

}
