package no.nav.k9.søknad.ytelse.psb.v1;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import no.nav.k9.søknad.PeriodeValidator;
import no.nav.k9.søknad.Validator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.LovbestemtFerie;
import no.nav.k9.søknad.felles.aktivitet.ArbeidAktivitet;
import no.nav.k9.søknad.felles.aktivitet.Arbeidstaker;
import no.nav.k9.søknad.felles.aktivitet.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynsordningSvar;

public class PleiepengerSyktBarnValidator extends YtelseValidator {
    private static final Duration MAKS_INNENFOR_EN_UKE = Duration.ofDays(7);

    private final PeriodeValidator periodeValidator;

    public PleiepengerSyktBarnValidator() {
        this.periodeValidator = new PeriodeValidator();
    }

    @Override
    public List<Feil> valider(Ytelse ytelse) {
        final List<Feil> feil = new ArrayList<>();

        var pleiepengerSyktBarn = (PleiepengerSyktBarn) ytelse;

        validerSøknadsperioder(pleiepengerSyktBarn.getPerioder(), feil);

        validerBerdskap(pleiepengerSyktBarn.getBeredskap(), feil);
        validerNattevåk(pleiepengerSyktBarn.getNattevåk(), feil);
        validerTilsynsordning(pleiepengerSyktBarn.getTilsynsordning(), feil);
        validerLovbestemtFerie(pleiepengerSyktBarn.getLovbestemtFerie(), feil);
        validerArbeidstaker(pleiepengerSyktBarn.getAktivitet().getArbeidstaker(), feil);
        validerOpptjening(pleiepengerSyktBarn.getAktivitet(), feil);
        
        feil.addAll(Validator.validerTilFeil(ytelse));
        return feil;
    }

    private void validerLovbestemtFerie(LovbestemtFerie lovbestemtFerie, List<Feil> feil) {
        if (lovbestemtFerie != null) {
            feil.addAll(periodeValidator.validerIkkeTillattOverlapp(lovbestemtFerie.perioder, "lovbestemtFerie"));
        }
    }

    private void validerSøknadsperioder(Map<Periode, SøknadsperiodeInfo> søknadsperioder, List<Feil> feil) {
        if (søknadsperioder == null || søknadsperioder.isEmpty()) {
            feil.add(new Feil("perioder", PÅKREVD, "Må settes minst en periode for søknaden."));
        } else {
            feil.addAll(periodeValidator.validerIkkeTillattOverlapp(søknadsperioder, "perioder"));
        }
    }

    private void validerBerdskap(Beredskap beredskap, List<Feil> feil) {
        if (beredskap == null) return;
        feil.addAll(periodeValidator.validerTillattOverlapp(beredskap.perioder, "beredskap.perioder"));
    }

    private void validerNattevåk(Nattevåk nattevåk, List<Feil> feil) {
        if (nattevåk == null) return;
        feil.addAll(periodeValidator.validerTillattOverlapp(nattevåk.perioder, "nattevåk.perioder"));
    }

    private void validerArbeidstaker(List<Arbeidstaker> arbeidsgivere, List<Feil> feil) {
        int i = 0;
        for (Arbeidstaker arbeidstaker : arbeidsgivere) {
            if (arbeidstaker.norskIdentitetsnummer != null && arbeidstaker.organisasjonsnummer != null) {
                feil.add(new Feil("arbeid.arbeidstaker[" + i + "]", "ikkeEntydigIdPåArbeidsgiver", "Må oppgi en av norskIdentitetsnummer eller organisasjonsnummer."));
            } else if (arbeidstaker.norskIdentitetsnummer == null && arbeidstaker.organisasjonsnummer == null) {
                feil.add(new Feil("arbeid.arbeidstaker[" + i + "]", "idPåArbeidsgiverPåkrevd", "Må oppgi en av norskIdentitetsnummer eller organisasjonsnummer."));
            }
            feil.addAll(periodeValidator.validerTillattOverlapp(arbeidstaker.perioder, "arbeid.arbeidstaker[" + i + "].perioder"));
            for (Map.Entry<Periode, Arbeidstaker.ArbeidstakerPeriodeInfo> perioder : arbeidstaker.perioder.entrySet()) {
                BigDecimal skalJobbeProsent = perioder.getValue().skalJobbeProsent;
                if (skalJobbeProsent == null || skalJobbeProsent.doubleValue() < 0 || skalJobbeProsent.doubleValue() > 100) {
                    feil.add(new Feil("arbeid.arbeidstaker[" + i + "].perioder[" + perioder.getKey().iso8601 + "].skalJobbeProsent", "ugylidigProsent", "Skal jobbe prosent må være mellom 0 og 100"));
                }
                Duration jobberNormalPerUke = perioder.getValue().jobberNormaltPerUke;
                if (jobberNormalPerUke == null || jobberNormalPerUke.isNegative()) {
                    feil.add(new Feil("arbeid.arbeidstaker[" + i + "].perioder[" + perioder.getKey().iso8601 + "].jobberNormalPerUke", "ugyldigJobbperiode", "Jobber normalt per uke må settes til en gyldig verdi."));
                } else if (jobberNormalPerUke.compareTo(MAKS_INNENFOR_EN_UKE) > 0) {
                    feil.add(new Feil("arbeid.arbeidstaker[" + i + "].perioder[" + perioder.getKey().iso8601 + "].jobberNormalPerUke", "ugyldigJobbperiode", "Jobber normalt per uke kan ikke overstige en uke."));
                }
            }
            i++;
        }
    }

    private void validerOpptjening(ArbeidAktivitet arbeid, List<Feil> feil) {
        if (arbeid == null) return;

        int i = 0;
        for (SelvstendigNæringsdrivende selvstendigNæringsdrivende : arbeid.getSelvstendigNæringsdrivende()) {
            feil.addAll(periodeValidator.validerTillattOverlappOgÅpnePerioder(selvstendigNæringsdrivende.perioder, "arbeid.selvstendigNæringsdrivende[" + i++ + "].perioder"));
        }
    }

    private void validerTilsynsordning(Tilsynsordning tilsynsordning, List<Feil> feil) {
        if (tilsynsordning == null || tilsynsordning.iTilsynsordning == null) {
            feil.add(new Feil("tilsynsordning", PÅKREVD, "Må oppgi svar om barnet skal være i tilsynsordning."));
        } else if (TilsynsordningSvar.JA == tilsynsordning.iTilsynsordning && tilsynsordning.opphold != null) {
            feil.addAll(periodeValidator.validerIkkeTillattOverlapp(tilsynsordning.opphold, "tilsynsordning.opphold"));
            tilsynsordning.opphold.forEach((periode, opphold) -> {
                if (opphold.lengde == null) {
                    feil.add(new Feil("tilsynsordning.opphold[" + periode.iso8601 + "].lengde", PÅKREVD, "Lengde på opphold i tilynsordning må settes."));
                } else {
                    Duration maks = maksInnenforPeriode(periode);
                    if (maks != null && opphold.lengde.compareTo(maks) > 0) {
                        feil.add(new Feil("tilsynsordning.opphold[" + periode.iso8601 + "].lengde", "ugyldigLengdePåOpphold", "Lengden på oppholdet overskrider tiden i perioden."));
                    }
                }
            });
        }
    }

    private Duration maksInnenforPeriode(Periode periode) {
        if (periode.fraOgMed == null || periode.tilOgMed == null) return null;
        else {
            Period period = Period.between(periode.fraOgMed, periode.tilOgMed.plusDays(1));
            return Duration.ofDays(period.getDays()).abs();
        }
    }
}
