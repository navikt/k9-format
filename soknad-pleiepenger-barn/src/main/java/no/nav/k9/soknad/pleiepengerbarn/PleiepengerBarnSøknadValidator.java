package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.PeriodeValidator;
import no.nav.k9.soknad.SøknadValidator;
import no.nav.k9.soknad.felles.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PleiepengerBarnSøknadValidator extends SøknadValidator<PleiepengerBarnSøknad> {
    private final PeriodeValidator periodeValidator;

    PleiepengerBarnSøknadValidator() {
        this.periodeValidator = new PeriodeValidator();
    }

    @Override
    public List<Feil> valider(PleiepengerBarnSøknad soknad) {
        final List<Feil> feil = new ArrayList<>();

        validerSøknadId(soknad.søknadId, feil);
        validerVersjon(soknad.versjon, feil);
        validerPeriode(soknad.periode, feil);
        validerMottattDato(soknad.mottattDato, feil);
        validerSpråk(soknad.språk, feil);
        validerSoker(soknad.søker, feil);
        validerBarn(soknad.barn, feil);
        validerUtland(soknad.utland, feil);
        validerBerdskap(soknad.beredskap, feil);
        validerNattevåk(soknad.nattevåk, feil);
        validerTilsynsordning(soknad.tilsynsordning, feil);
        validerArbeid(soknad.arbeid, feil);

        return feil;
    }

    private void validerPeriode(Periode periode, List<Feil> feil) {
        if (periode == null) {
            feil.add(new Feil("periode", PÅKREVD, "Må settes en periode for søknaden."));
        } else {
            feil.addAll(periodeValidator.valider(periode, "periode"));
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

    private void validerUtland(Utland utland, List<Feil> feil) {
        if (utland == null) return;
        if (utland.harBoddIUtlandetSiste12Mnd == null) {
            feil.add(new Feil("utland.harBoddIUtlandetSiste12Mnd", PÅKREVD, "Må besvares om man har bodd i utlandet de siste 12 månededene."));
        }
        if (utland.skalBoIUtlandetNeste12Mnd == null) {
            feil.add(new Feil("utland.skalBoIUtlandetNeste12Mnd", PÅKREVD, "Må besvares om man skal bo i utlandet de neste 12 månededene."));
        }
        feil.addAll(periodeValidator.validerIkkeTillattOverlapp(utland.opphold,"utland.opphold"));
    }

    private static void validerSoker(Søker søker, List<Feil> feil) {
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
            }
            i++;
        }

        i = 0;
        for (Frilanser frilanser : arbeid.frilanser) {
            feil.addAll(
                    periodeValidator.validerTillattOverlapp(frilanser.perioder, "arbeid.frilanser[" + i++ + "].perioder")
            );
        }
        i = 0;
        for (SelvstendigNæringsdrivende selvstendigNæringsdrivende : arbeid.selvstendigNæringsdrivende) {
            feil.addAll(
                    periodeValidator.validerTillattOverlapp(selvstendigNæringsdrivende.perioder, "arbeid.selvstendigNæringsdrivende[" + i++ + "].perioder")
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
