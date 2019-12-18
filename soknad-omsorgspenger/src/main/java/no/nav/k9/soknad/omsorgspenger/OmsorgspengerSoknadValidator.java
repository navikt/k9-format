package no.nav.k9.soknad.omsorgspenger;

import no.nav.k9.soknad.SoknadValidator;
import no.nav.k9.soknad.felles.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class OmsorgspengerSoknadValidator extends SoknadValidator<OmsorgspengerSoknad> {

    @Override
    public List<Feil> valider(OmsorgspengerSoknad soknad) {
        List<Feil> feil = new ArrayList<>();
        validerSoknadId(soknad.soknadId, feil);
        validerMottattDato(soknad.mottattDato, feil);
        validerSoker(soknad.soker, feil);
        validerBarn(soknad.barn, feil);
        return feil;
    }

    private static void validerSoknadId(SoknadId soknadId, List<Feil> feil) {
        if (soknadId == null || soknadId.id == null || soknadId.id.isBlank()) {
            feil.add(new Feil("soknad_id", "paakrevd", "ID må settes i søknaden."));
        }
    }

    private static void validerMottattDato(ZonedDateTime mottatDato, List<Feil> feil) {
        if (mottatDato == null) {
            feil.add(new Feil("mottatt_dato", "paakrevd", "Mottatt dato må settes i søknaden."));
        }
    }

    private static void validerSoker(Soker soker, List<Feil> feil) {
        if (soker == null) {
            feil.add(new Feil("soker", "paakrevd", "Søker må settes i søknaden."));
        } else if (erNull(soker.norskIdentitetsnummer)) {
            feil.add(new Feil("soker.norsk_identitetsnummer", "paakrevd", "Søkers Personnummer/D-nummer må settes i søknaden."));
        }
    }

    private static void validerBarn(Barn barn, List<Feil> feil) {
        if (barn == null) {
            feil.add(new Feil("barn", "paakrevd", "Barn må settes i søknaden."));
        } else if (erNull(barn.norskIdentitetsnummer) && barn.foedselsdato == null) {
            feil.add(new Feil("barn", "norskIdentitetsnummerEllerFoedselsdatoPaakrevd", "Må sette enten Personnummer/D-nummer på barn, eller fødselsdato."));
        }
    }

    private static boolean erNull(NorskIdentitetsnummer norskIdentitetsnummer) {
        return norskIdentitetsnummer == null || norskIdentitetsnummer.getVerdi() == null || norskIdentitetsnummer.getVerdi().isBlank();
    }
}
