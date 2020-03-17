package no.nav.k9.søknad.pleiepengerbarn;

import no.nav.k9.søknad.PeriodeValidator;
import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PleiepengerBarnSøknadValidator extends SøknadValidator<PleiepengerBarnSøknad> {
    private static final Duration MAKS_INNENFOR_EN_UKE = Duration.ofDays(7);

    private final PeriodeValidator periodeValidator;

    public PleiepengerBarnSøknadValidator() {
        this.periodeValidator = new PeriodeValidator();
    }

    @Override
    public List<Feil> valider(PleiepengerBarnSøknad søknad) {
        final List<Feil> feil = new ArrayList<>();

        validerSøknadId(søknad.søknadId, feil);
        validerVersjon(søknad.versjon, feil);
        validerSøknadsperioder(søknad.perioder, feil);
        validerMottattDato(søknad.mottattDato, feil);
        validerSpråk(søknad.språk, feil);
        validerSøker(søknad.søker, feil);
        validerBarn(søknad.barn, feil);
        validerUtenlandsopphold(søknad.utenlandsopphold, feil);
        validerBosteder(søknad.bosteder, feil);
        validerBerdskap(søknad.beredskap, feil);
        validerNattevåk(søknad.nattevåk, feil);
        validerTilsynsordning(søknad.tilsynsordning, feil);
        validerArbeid(søknad.arbeid, feil);
        validerLovbestemtFerie(søknad.lovbestemtFerie, feil);

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


    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon == null) {
            feil.add(new Feil("versjon", PÅKREVD, "Versjon må settes i søknaden."));
        } else if (!versjon.erGyldig()){
            feil.add(new Feil("versjon", "ugyldigVersjon", "Versjonen er på ugyldig format."));
        }
    }

    private static void validerSpråk(Språk språk, List<Feil> feil) {
        if (språk == null) {
            feil.add(new Feil("språk", PÅKREVD, "Språk må settes i søknaden."));
        }
    }

    private static void validerSøknadId(SøknadId søknadId, List<Feil> feil) {
        if (søknadId == null) {
            feil.add(new Feil("søknadId", PÅKREVD, "ID må settes i søknaden."));
        }
    }

    private static void validerMottattDato(ZonedDateTime mottatDato, List<Feil> feil) {
        if (mottatDato == null) {
            feil.add(new Feil("mottattDato", PÅKREVD, "Mottatt dato må settes i søknaden."));
        }
    }

    private void validerBerdskap(Beredskap beredskap, List<Feil> feil) {
        if (beredskap == null) return;
        feil.addAll(periodeValidator.validerTillattOverlapp(beredskap.perioder,"beredskap.perioder"));
    }

    private void validerNattevåk(Nattevåk nattevåk, List<Feil> feil) {
        if (nattevåk == null) return;
        feil.addAll(periodeValidator.validerTillattOverlapp(nattevåk.perioder, "nattevåk.perioder"));
    }

    private void validerUtenlandsopphold(Utenlandsopphold utenlandsopphold, List<Feil> feil) {
        if (utenlandsopphold == null) return;
        feil.addAll(periodeValidator.validerIkkeTillattOverlapp(utenlandsopphold.perioder, "utenlandsopphold.perioder"));
    }

    private void validerBosteder(Bosteder bosteder, List<Feil> feil) {
        if (bosteder == null) return;
        feil.addAll(periodeValidator.validerIkkeTillattOverlapp(bosteder.perioder, "bosteder.perioder"));
    }

    private static void validerSøker(Søker søker, List<Feil> feil) {
        if (søker == null) {
            feil.add(new Feil("søker", PÅKREVD, "Søker må settes i søknaden."));
        } else if (søker.norskIdentitetsnummer == null) {
            feil.add(new Feil("søker.norskIdentitetsnummer", PÅKREVD, "Søkers Personnummer/D-nummer må settes i søknaden."));
        }
    }

    private static void validerBarn(Barn barn, List<Feil> feil) {
        if (barn == null) {
            feil.add(new Feil("barn", PÅKREVD, "Barn må settes i søknaden."));
        } else if (barn.norskIdentitetsnummer == null && barn.fødselsdato == null) {
            feil.add(new Feil("barn", "norskIdentitetsnummerEllerFødselsdatoPåkrevd", "Må sette enten Personnummer/D-nummer på barn, eller fødselsdato."));
        } else if (barn.norskIdentitetsnummer != null && barn.fødselsdato != null) {
            feil.add(new Feil("barn", "ikkeEntydigIdPåBarnet", "Må sette enten Personnummer/D-nummer på barn, eller fødselsdato."));
        }
    }

    private void validerArbeid(Arbeid arbeid, List<Feil> feil) {
        if (arbeid == null) return;

        int i = 0;
        for (Arbeidstaker arbeidstaker : arbeid.arbeidstaker) {
            if (arbeidstaker.norskIdentitetsnummer != null && arbeidstaker.organisasjonsnummer != null) {
                feil.add(new Feil("arbeid.arbeidstaker[" + i + "]","ikkeEntydigIdPåArbeidsgiver", "Må oppgi en av norskIdentitetsnummer eller organisasjonsnummer."));
            } else if (arbeidstaker.norskIdentitetsnummer == null && arbeidstaker.organisasjonsnummer == null) {
                feil.add(new Feil("arbeid.arbeidstaker[" + i + "]","idPåArbeidsgiverPåkrevd", "Må oppgi en av norskIdentitetsnummer eller organisasjonsnummer."));
            }
            feil.addAll(
                    periodeValidator.validerTillattOverlapp(arbeidstaker.perioder, "arbeid.arbeidstaker[" + i + "].perioder")
            );
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

        i = 0;
        for (Frilanser frilanser : arbeid.frilanser) {
            feil.addAll(
                    periodeValidator.validerTillattOverlappOgÅpnePerioder(frilanser.perioder, "arbeid.frilanser[" + i++ + "].perioder")
            );
        }
        i = 0;
        for (SelvstendigNæringsdrivende selvstendigNæringsdrivende : arbeid.selvstendigNæringsdrivende) {
            feil.addAll(
                    periodeValidator.validerTillattOverlappOgÅpnePerioder(selvstendigNæringsdrivende.perioder, "arbeid.selvstendigNæringsdrivende[" + i++ + "].perioder")
            );
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
