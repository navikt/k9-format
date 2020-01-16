package no.nav.k9.soknad.omsorgspenger;

import no.nav.k9.soknad.JsonUtils;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static no.nav.k9.soknad.omsorgspenger.TestUtils.jsonForKomplettSoknad;
import static no.nav.k9.soknad.omsorgspenger.TestUtils.komplettBuilder;


public class OmsorgspengerSoknadTest {
    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = jsonForKomplettSoknad();
        OmsorgspengerSoknad fraBuilder = komplettBuilder().build();
        JSONAssert.assertEquals(json, JsonUtils.toString(fraBuilder), true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = jsonForKomplettSoknad();
        OmsorgspengerSoknad soknad = JsonUtils.fromString(json, OmsorgspengerSoknad.class);
        JSONAssert.assertEquals(json, JsonUtils.toString(soknad), true);
    }
}
