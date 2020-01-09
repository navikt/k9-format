package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.PeriodeValidator;
import no.nav.k9.soknad.SoknadValidator;
import no.nav.k9.soknad.felles.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PleiepengerBarnSoknadValidator extends SoknadValidator<PleiepengerBarnSoknad> {
    private final PeriodeValidator periodeValidator;

    PleiepengerBarnSoknadValidator() {
        this.periodeValidator = new PeriodeValidator();
    }

    @Override
    public List<Feil> valider(PleiepengerBarnSoknad soknad) {
        final List<Feil> feil = new ArrayList<>();

        validerSoknadId(soknad.soknadId, feil);
        validerVersjon(soknad.versjon, feil);
        validerPeriode(soknad.periode, feil);
        validerMottattDato(soknad.mottattDato, feil);
        validerSpraak(soknad.spraak, feil);
        validerSoker(soknad.soker, feil);
        validerBarn(soknad.barn, feil);
        validerUtland(soknad.utland, feil);
        validerBerdskap(soknad.beredskap, feil);
        validerNattebaak(soknad.nattevaak, feil);
        validerTilsynsordning(soknad.tilsynsordning, feil);

        return feil;
    }

    private void validerPeriode(Periode periode, List<Feil> feil) {
        if (periode == null) {
            feil.add(new Feil("periode", "paakrevd", "Må settes en periode for søknaden."));
        } else {
            feil.addAll(periodeValidator.valider(periode, "periode"));
        }
    }


    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (Versjon.erNull(versjon)) {
            feil.add(new Feil("versjon", "paakrevd", "Versjon må settes i søknaden."));
        } else if (!versjon.erGyldig()){
            feil.add(new Feil("versjon", "ugyldigVersjon", "Versjonen er på ugyldig format."));
        }
    }

    private static void validerSpraak(Spraak spraak, List<Feil> feil) {
        if (spraak == null) {
            feil.add(new Feil("spraak", "paakrevd", "Språk må settes i søknaden."));
        }
    }

    private static void validerSoknadId(SoknadId soknadId, List<Feil> feil) {
        if (SoknadId.erNull(soknadId)) {
            feil.add(new Feil("soknadId", "paakrevd", "ID må settes i søknaden."));
        }
    }

    private static void validerMottattDato(ZonedDateTime mottatDato, List<Feil> feil) {
        if (mottatDato == null) {
            feil.add(new Feil("mottattDato", "paakrevd", "Mottatt dato må settes i søknaden."));
        }
    }

    private void validerBerdskap(Map<Periode, Beredskap> beredskap, List<Feil> feil) {
        if (beredskap == null) return;
        feil.addAll(periodeValidator.validerTillattOverlapp(beredskap,"beredskap"));
    }

    private void validerNattebaak(Map<Periode, Nattevaak> nattevaak, List<Feil> feil) {
        if (nattevaak == null) return;
        feil.addAll(periodeValidator.validerTillattOverlapp(nattevaak, "nattevaak"));
    }

    private void validerUtland(Utland utland, List<Feil> feil) {
        if (utland == null) return;
        if (utland.harBoddIUtlandetSiste12Mnd == null) {
            feil.add(new Feil("utland.harBoddIUtlandetSiste12Mnd", "paakrevd", "Må besvares om man har bodd i utlandet de siste 12 månededene."));
        }
        if (utland.skalBoIUtlandetNeste12Mnd == null) {
            feil.add(new Feil("utland.skalBoIUtlandetNeste12Mnd", "paakrevd", "Må besvares om man skal bo i utlandet de neste 12 månededene."));
        }
        feil.addAll(periodeValidator.validerIkkeTillattOverlapp(utland.opphold,"utland.opphold"));
    }

    private static void validerSoker(Soker soker, List<Feil> feil) {
        if (soker == null) {
            feil.add(new Feil("soker", "paakrevd", "Søker må settes i søknaden."));
        } else if (NorskIdentitetsnummer.erNull(soker.norskIdentitetsnummer)) {
            feil.add(new Feil("soker.norskIdentitetsnummer", "paakrevd", "Søkers Personnummer/D-nummer må settes i søknaden."));
        }
    }

    private static void validerBarn(Barn barn, List<Feil> feil) {
        if (barn == null) {
            feil.add(new Feil("barn", "paakrevd", "Barn må settes i søknaden."));
        } else if (NorskIdentitetsnummer.erNull(barn.norskIdentitetsnummer) && barn.foedselsdato == null) {
            feil.add(new Feil("barn", "norskIdentitetsnummerEllerFoedselsdatoPaakrevd", "Må sette enten Personnummer/D-nummer på barn, eller fødselsdato."));
        }
    }

    private void validerTilsynsordning(Tilsynsordning tilsynsordning, List<Feil> feil) {
        if (tilsynsordning == null || tilsynsordning.iTilsynsordning == null) {
            feil.add(new Feil("tilsynsordning", "paakrevd", "Må oppgi svar om barnet skal være i tilsynsordning."));
        } else if (TilsynsordningSvar.JA == tilsynsordning.iTilsynsordning) {
            feil.addAll(periodeValidator.validerIkkeTillattOverlapp(tilsynsordning.opphold, "tilsynsordning.opphold"));
        }
    }
}
