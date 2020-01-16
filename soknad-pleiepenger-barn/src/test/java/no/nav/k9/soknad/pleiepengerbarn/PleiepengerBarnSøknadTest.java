package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.JsonUtils;
import org.json.JSONException;
import org.junit.Test;

import org.skyscreamer.jsonassert.JSONAssert;

public class PleiepengerBarnSøknadTest {
    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = TestUtils.jsonForKomplettSøknad();
        PleiepengerBarnSøknad fraBuilder = TestUtils.komplettBuilder().build();
        JSONAssert.assertEquals(json, JsonUtils.toString(fraBuilder), true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = TestUtils.jsonForKomplettSøknad();
        PleiepengerBarnSøknad søknad = JsonUtils.fromString(json, PleiepengerBarnSøknad.class);
        JSONAssert.assertEquals(json, JsonUtils.toString(søknad), true);
    }
}