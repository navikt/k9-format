package no.nav.k9.søknad.ytelse.psb.v1;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.k9.søknad.TidsserieValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
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
            var tidsserier = validerSøknadsOgEndringsPerioder(psb, feil);
            validerKomplettSøknad(psb, feil);

            validerBeredskap(psb.getBeredskap(), tidsserier, feil);
            validerUttak(psb.getUttak(), tidsserier, feil);
            validerNattevåk(psb.getNattevåk(), tidsserier, feil);
            validerTilsynsordning(psb.getTilsynsordning(), tidsserier, feil);
            validerLovbestemtFerie(psb.getLovbestemtFerie(), tidsserier, feil);
            validerArbeidstid(psb.getArbeidstid(), tidsserier, feil);

        } catch (IllegalArgumentException e) {
            feil.add(new Feil(e.getClass().getName(), e.getCause() + "IllegalArgumentException", e.getMessage()));
        }

        validerOpptjeningsAktivtet(psb, feil);
        validerBosteder(psb, feil);
        validerUtenlandsOpphold(psb, feil);
        validerOmsorg(psb, feil);

        return feil;
    }

    private void validerAtSøknadsperiodeIkkeErNullOgTom(String felt, PleiepengerSyktBarn psb, List<Feil> feil) {
        if(psb.getSøknadsperiodeList() == null || psb.getSøknadsperiodeList().isEmpty()) {
            feil.add(new Feil(felt,"IllegalArgumentException","Må være en søknad med minst en søknadsperiode"));
        }
    }

    private void manglerIkkeSøknadEllerEndringsPerioder(PleiepengerSyktBarn psb, List<Feil> feil) {
        if( (psb.getSøknadsperiodeList() == null && psb.getEndringsperiodeList() == null)) {
            feil.add(new Feil("søknadsperiode/endringsperiode", "missingArgument","Mangler søknadsperiode eller endringsperiode."));
        }
    }


    private TidsserieValidator.TidsseriePeriodeWrapper validerSøknadsOgEndringsPerioder(PleiepengerSyktBarn psb, List<Feil> feil) {
        manglerIkkeSøknadEllerEndringsPerioder(psb, feil);
        return new TidsserieValidator.TidsseriePeriodeWrapper(psb.getSøknadsperiodeList(), psb.getEndringsperiodeList());
    }

    private void validerOmsorg(PleiepengerSyktBarn psb, List<Feil> feil) {
        if (psb.getOmsorg() == null) {
            return;
        }
        validerAtSøknadsperiodeIkkeErNullOgTom("omsorg" ,psb, feil);
    }

    private void validerUtenlandsOpphold(PleiepengerSyktBarn psb, List<Feil> feil) {
        if (psb.getUtenlandsopphold() == null) {
            return;
        }
        validerAtSøknadsperiodeIkkeErNullOgTom("utenlandsopphold", psb, feil);
    }

    private void validerBosteder(PleiepengerSyktBarn psb, List<Feil> feil) {
        if (psb.getBosteder() == null) {
            return;
        }
        validerAtSøknadsperiodeIkkeErNullOgTom("bosteder", psb, feil);
    }

    private void validerOpptjeningsAktivtet(PleiepengerSyktBarn psb, List<Feil> feil) {
        if(psb.getOpptjeningAktivitet() == null) {
            return;
        }
        validerAtSøknadsperiodeIkkeErNullOgTom("opptjeningsAktivitet", psb, feil);
    }

    private void validerKomplettSøknad(PleiepengerSyktBarn psb, List<Feil> feil) {
        if (psb.getSøknadsperiodeList() != null && psb.getSøknadsperiodeList().size() <= 1) {
            if (psb.getUttak() == null) {
                feil.add(new Feil("uttak", "missingArgument","Uttak kan ikke være null."));
            }
            if (psb.getOmsorg() == null) {
                feil.add(new Feil("omsorg", "missingArgument","Omsorg kan ikke være null."));
            }
            if (psb.getBarn() == null) {
                feil.add(new Feil("barn", "missingArgument","Barn kan ikke være null."));
            }
            // bosteder (kan være tom?)
            // utenlandsopphold (kan være tom)
        }
    }

    private Feil toFeil(ConstraintViolation<PleiepengerSyktBarn> constraintViolation) {
        return new Feil(
                constraintViolation.getPropertyPath().toString(),
                PÅKREVD,
                constraintViolation.getMessage());
    }

    private LocalDateTimeline<Boolean> validerSøknadsperiode(PleiepengerSyktBarn psb) {
        if (psb.getSøknadsperiodeList() == null) {
            return new LocalDateTimeline<Boolean>(null);
        }
        return toLocalDateTimeline(psb.getSøknadsperiodeList());
    }

    private void validerBeredskap(Beredskap beredskap, TidsserieValidator.TidsseriePeriodeWrapper tidsseriePeriodeWrapper, List<Feil> feil) {
        if (beredskap == null) {
            return;
        }
        finnPerioderUtenfor(
                toLocalDateTimeline(beredskap.getPerioder()),
                tidsseriePeriodeWrapper)
                .valider("beredskap", feil);
    }

    private void validerUttak(Uttak uttak, TidsserieValidator.TidsseriePeriodeWrapper tidsseriePeriodeWrapper, List<Feil> feil) {
        if (uttak == null) {
            return;
        }
        finnIkkeKomplettePerioderOgPerioderUtenfor(
                toLocalDateTimeline(uttak.getPerioder()),
                tidsseriePeriodeWrapper)
                .valider("uttak", feil);
    }

    private void validerLovbestemtFerie(LovbestemtFerie lovbestemtFerie, TidsserieValidator.TidsseriePeriodeWrapper søknadsperiode, List<Feil> feil) {
        if (lovbestemtFerie == null) {
            return;
        }
        finnPerioderUtenfor(
                toLocalDateTimeline(lovbestemtFerie.getPerioder()),
                søknadsperiode).valider("lovbestemtFerie", feil);
    }

    private void validerNattevåk(Nattevåk nattevåk, TidsserieValidator.TidsseriePeriodeWrapper søknadsperiode, List<Feil> feil) {
        if (nattevåk == null) {
            return;
        }
        finnPerioderUtenfor(
                toLocalDateTimeline(nattevåk.getPerioder()),
                søknadsperiode)
                .valider("nattevåk", feil);
    }

    private void validerArbeidstid(Arbeidstid arbeidstid, TidsserieValidator.TidsseriePeriodeWrapper søknadsperiode, List<Feil> feil) {
        if (arbeidstid == null) {
            return;
        }
        validerArbeidstaker(arbeidstid.getArbeidstakerList(), søknadsperiode, feil);
        validerFrilanser(arbeidstid.getFrilanserArbeidstidInfo(), søknadsperiode, feil);
        validerSelvstendigNæringsdrivende(arbeidstid.getSelvstendigNæringsdrivendeArbeidstidInfo(), søknadsperiode, feil);
    }

    private void validerArbeidstaker(List<Arbeidstaker> arbeidstakerList, TidsserieValidator.TidsseriePeriodeWrapper søknadsperiode, List<Feil> feil) {
        if (arbeidstakerList == null) {
            return;
        }
        for (Arbeidstaker arbeidstaker : arbeidstakerList ) {
            arbeidstaker.valider("arbeidstid.arbeidstaker", feil);
            finnIkkeKomplettePerioderOgPerioderUtenfor(
                    toLocalDateTimeline(arbeidstaker.getArbeidstidInfo().getPerioder()), søknadsperiode)
                    .valider("arbeidstid.arbeidstaker", feil);
        }
    }

    private void validerFrilanser(ArbeidstidInfo frilanser, TidsserieValidator.TidsseriePeriodeWrapper søknadsperiode, List<Feil> feil) {
        if (frilanser == null) {
            return;
        }
        finnIkkeKomplettePerioderOgPerioderUtenfor(
                toLocalDateTimeline(frilanser.getPerioder()),
                søknadsperiode)
                .valider("arbeidstid.frilanser", feil);
    }

    private void validerSelvstendigNæringsdrivende(ArbeidstidInfo selvstendigNæringsdrivende, TidsserieValidator.TidsseriePeriodeWrapper søknadsperiode, List<Feil> feil) {
        if (selvstendigNæringsdrivende == null) {
            return;
        }
        finnIkkeKomplettePerioderOgPerioderUtenfor(
                toLocalDateTimeline(selvstendigNæringsdrivende.getPerioder()),
                søknadsperiode)
                .valider("arbeidstid.frilanser", feil);
    }

    private void validerTilsynsordning(Tilsynsordning tilsynsordning, TidsserieValidator.TidsseriePeriodeWrapper søknadsperiode, List<Feil> feil) {
        if (tilsynsordning == null) {
            return;
        }
        finnPerioderUtenfor(
                toLocalDateTimeline(tilsynsordning.getPerioder()),
                søknadsperiode)
                .valider("tilsynsordning", feil);
    }
}
