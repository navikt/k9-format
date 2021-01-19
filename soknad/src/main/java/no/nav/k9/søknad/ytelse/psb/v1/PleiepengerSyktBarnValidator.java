package no.nav.k9.søknad.ytelse.psb.v1;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.LovbestemtFerie;
import no.nav.k9.søknad.felles.aktivitet.ArbeidAktivitet;
import no.nav.k9.søknad.felles.aktivitet.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.psb.v1.arbeid.Arbeid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeid.ArbeidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynsordningOpphold;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynsordningSvar;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static no.nav.k9.søknad.PeriodeValidator.*;

public class PleiepengerSyktBarnValidator extends YtelseValidator {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    @Override
    public List<Feil> valider(Ytelse ytelse) {
        var psb = (PleiepengerSyktBarn) ytelse;
        var validate = VALIDATOR_FACTORY.getValidator().validate(psb);

        List<Feil> feil = validate.stream()
                .map(this::toFeil)
                .collect(Collectors.toList());

        validerSøknadsperiode(psb.getSøknadsperiode(), feil);
        var søknadsperiode = psb.getSøknadsperiode();

        validerBeredskap(psb.getBeredskap(), søknadsperiode, feil);
        validerUttak(psb.getUttak(), søknadsperiode, feil);
        validerNattevåk(psb.getNattevåk(), søknadsperiode, feil);
        validerTilsynsordning(psb.getTilsynsordning(), søknadsperiode, feil);
        validerLovbestemtFerie(psb.getLovbestemtFerie(), søknadsperiode, feil);
        validerArbeid(psb.getArbeid(), søknadsperiode, feil);
        validerArbeidAktivitet(psb.getArbeidAktivitet(), feil);

        return feil;
    }

    private Feil toFeil(ConstraintViolation<PleiepengerSyktBarn> constraintViolation) {
        return new Feil(
                constraintViolation.getPropertyPath().toString(),
                constraintViolation.getMessage(),
                constraintViolation.getMessageTemplate());
    }

    private void validerSøknadsperiode(Periode søknadsperiode, List<Feil> feil) {
        validerGyldigPeriode(søknadsperiode, "søknadsperiode", false, feil);
    }

    private void validerBeredskap(Beredskap beredskap, Periode søknadsperiode, List<Feil> feil) {
        if (beredskap == null) {
            return;
        }
        beredskap.getPerioder().forEach(((periode, beredskapPeriodeInfo) -> {
            validerGyldigPeriode(periode, "beredskap.perioder", false, feil);
            validerPeriodeInnenforSøknadsperiode(periode, "beredskap.perioder", søknadsperiode, feil);
        }));
    }

    private void validerUttak(Uttak uttak, Periode søknadsperiode, List<Feil> feil) {
        if (uttak == null) {
            return;
        }
        uttak.getPerioder().forEach((periode, uttakPeriodeInfo) -> {
            validerGyldigPeriode(periode, "uttak.perioder", false, feil);
            validerPeriodeInnenforSøknadsperiode(periode, "uttak.periode", søknadsperiode, feil);
        });
    }

    private void validerArbeidAktivitet(ArbeidAktivitet arbeidAktivitet, List<Feil> feil) {
        if (arbeidAktivitet == null) {
            return;
        }
        validerOpptjeningInfo(arbeidAktivitet, feil);
        if (arbeidAktivitet.getSelvstendigNæringsdrivende() != null) {
            validerSelvstendigNæringsdrivendeList(arbeidAktivitet.getSelvstendigNæringsdrivende(), feil);
        }
    }

    private void validerOpptjeningInfo(ArbeidAktivitet opptjening, List<Feil> feil) {
        if (opptjening.getSelvstendigNæringsdrivende() == null && opptjening.getFrilanser() == null) {
            feil.add(new Feil("opptjening.FL/SN", "påkrevd", "Frilans eller SN må være satt."));
        }
    }

    private void validerSelvstendigNæringsdrivendeList(List<SelvstendigNæringsdrivende> snList, List<Feil> feil) {
        int i = 0;
        for(SelvstendigNæringsdrivende sn : snList) {
            validerSelvstendigNæringsdrivende(sn,"arbeid.arbeidstaker[" + i + "].periode", feil );
            i++;
        }
    }

    private void validerSelvstendigNæringsdrivende(SelvstendigNæringsdrivende sn, String felt, List<Feil> feil) {
        sn.perioder.forEach(((periode, info) ->
                validerGyldigPeriode(periode, felt, true, feil)));
    }

    private void validerLovbestemtFerie(LovbestemtFerie lovbestemtFerie, Periode søknadsperiode, List<Feil> feil) {
        if (lovbestemtFerie == null) {
            return;
        }
        lovbestemtFerie.getPerioder().forEach((periode -> {
            validerGyldigPeriode(periode, "lovbestemtFerie.perioder", false, feil);
            validerPeriodeInnenforSøknadsperiode(periode, "lovbestemtFerie.perioder", søknadsperiode, feil);
        }));
    }

    private void validerNattevåk(Nattevåk nattevåk, Periode søknadsperiode,  List<Feil> feil) {
        if (nattevåk == null) {
            return;
        }
        nattevåk.getPerioder().forEach(((periode, beredskapPeriodeInfo) -> {
            validerGyldigPeriode(periode, "nattevåk.perioder", false, feil);
            validerPeriodeInnenforSøknadsperiode(periode, "nattevåk.perioder", søknadsperiode, feil);
        }));
    }

    private void validerArbeid(Arbeid arbeid, Periode søknadsperiode, List<Feil> feil) {
        if (arbeid == null) {
            return;
        }
        validerArbeidstaker(arbeid.getArbeidstaker(), søknadsperiode, feil);
    }

    private void validerArbeidstaker(List<Arbeidstaker> arbeidstakerList, Periode søknadsperiode, List<Feil> feil) {
        if (arbeidstakerList == null) {
            return;
        }
        int i = 0;
        for (Arbeidstaker arbeidstaker : arbeidstakerList) {
            validerArbeidstakerInfo(arbeidstaker, "arbeid.arbeidstaker[" + i + "]", feil);
            validerArbeidstakerPeriode(arbeidstaker, i, søknadsperiode, feil);
            i++;
        }
    }

    private void validerArbeidstakerPeriode(Arbeidstaker arbeidstaker, int i, Periode søknadsperiode, List<Feil> feil) {
        for (Map.Entry<Periode, ArbeidPeriodeInfo> periode : arbeidstaker.getPerioder().entrySet()) {
            validerGyldigPeriode(periode.getKey(), "arbeid.arbeidstaker[" + i + "].periode", false, feil);
            validerPeriodeInnenforSøknadsperiode(periode.getKey(), "arbeid.arbeidstaker[" + i + "]", søknadsperiode, feil);
            validerOverlappendePerioder(arbeidstaker.getPerioder(), periode.getKey(), "arbeid.arbeidstaker.periode[" + i + "]", feil);
        }
    }


        private void validerArbeidstakerInfo(Arbeidstaker arbeidstaker, String felt, List<Feil> feil) {
        if (arbeidstaker.getNorskIdentitetsnummer() != null && arbeidstaker.getOrganisasjonsnummer() != null) {
            feil.add(new Feil(felt, "ikkeEntydigIdPåArbeidsgiver", "Ikke entydig ID på Arbeidsgiver, må oppgi enten norskIdentitetsnummer eller organisasjonsnummer."));
        } else if (arbeidstaker.getNorskIdentitetsnummer() == null && arbeidstaker.getOrganisasjonsnummer() == null) {
            feil.add(new Feil(felt, "idPåArbeidsgiverPåkrevd", "Mangler ID på Arbeidsgiver, må oppgi en av norskIdentitetsnummer eller organisasjonsnummer."));
        }
    }

    private void validerTilsynsordning(Tilsynsordning tilsynsordning, Periode søknadsperiode, List<Feil> feil) {
        if (tilsynsordning == null) {
            return;
        }
        if (TilsynsordningSvar.JA == tilsynsordning.getiTilsynsordning())
            if (tilsynsordning.getOpphold() == null || tilsynsordning.getOpphold().isEmpty()) {
                feil.add(new Feil("tilsynsordning.perioder", PÅKREVD, "Det må minst være en periode med opphold."));
            } else {
                tilsynsordning.getOpphold().forEach((periode, tilsynsordningOpphold) -> {
                    validerGyldigPeriode(periode, "tilsynsordning.perioder", false, feil);
                    validerPeriodeInnenforSøknadsperiode(periode, "tilsynsordning.perioder", søknadsperiode, feil);
                    validerTilsynsordningOpphold(tilsynsordningOpphold, periode, feil);
                });
        }
    }

    private void validerTilsynsordningOpphold(TilsynsordningOpphold tilsynsordningOpphold, Periode periode, List<Feil> feil) {
        Duration maks = maksInnenforPeriode(periode);
        if (maks != null && tilsynsordningOpphold.getLengde().compareTo(maks) > 0) {
            feil.add(new Feil("tilsynsordning.opphold[" + periode.getIso8601() + "].lengde", "ugyldigLengdePåOpphold", "Lengden på oppholdet overskrider tiden i perioden."));
        }
    }

    private Duration maksInnenforPeriode(Periode periode) {
        if (periode.getFraOgMed() == null || periode.getTilOgMed() == null) return null;
        else {
            Period p = Period.between(periode.getFraOgMed(), periode.getTilOgMed().plusDays(1));
            return Duration.ofDays(p.getDays()).abs();
        }
    }
}
