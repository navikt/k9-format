package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.JsonUtils;
import org.json.JSONException;
import org.junit.Test;

import org.skyscreamer.jsonassert.JSONAssert;

public class PleiepengerBarnSoknadTest {
    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = TestUtils.jsonForKomplettSoknad();
        PleiepengerBarnSoknad fraBuilder = TestUtils.komplettBuilder().build();
        JSONAssert.assertEquals(json, JsonUtils.toString(fraBuilder), true);

    }

    @Test
    public void reserialisering() throws JSONException {
        String json = TestUtils.jsonForKomplettSoknad();
        PleiepengerBarnSoknad soknad = JsonUtils.fromString(json, PleiepengerBarnSoknad.class);
        JSONAssert.assertEquals(json, JsonUtils.toString(soknad), true);
    }
}