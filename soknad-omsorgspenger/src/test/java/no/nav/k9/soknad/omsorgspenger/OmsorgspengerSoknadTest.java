package no.nav.k9.soknad.omsorgspenger;

import no.nav.k9.soknad.JsonUtils;
import no.nav.k9.soknad.felles.Barn;
import no.nav.k9.soknad.felles.NorskIdentitetsnummer;
import no.nav.k9.soknad.felles.Soker;
import no.nav.k9.soknad.felles.SoknadId;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static no.nav.k9.soknad.omsorgspenger.TestUtils.kompletFraJson;

public class OmsorgspengerSoknadTest {
    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = kompletFraJson();
        OmsorgspengerSoknad fraJson = JsonUtils.fromString(json, OmsorgspengerSoknad.class);
        OmsorgspengerSoknad fraBuilder = OmsorgspengerSoknad
                .builder()
                .barn(Barn.builder()
                        .foedselsdato(fraJson.barn.foedselsdato)
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of(fraJson.barn.norskIdentitetsnummer.verdi))
                        .build())
                .soker(Soker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of(fraJson.soker.norskIdentitetsnummer.verdi))
                        .build())
                .mottattDato(fraJson.mottattDato)
                .soknadId(SoknadId.of(fraJson.soknadId.id))
                .build();
        JSONAssert.assertEquals(json, JsonUtils.toString(fraBuilder), true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = kompletFraJson();
        OmsorgspengerSoknad soknad = JsonUtils.fromString(json, OmsorgspengerSoknad.class);
        JSONAssert.assertEquals(json, JsonUtils.toString(soknad), true);
    }
}
