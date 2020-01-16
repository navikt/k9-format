package no.nav.k9.soknad.omsorgspenger;

import no.nav.k9.soknad.JsonUtils;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static no.nav.k9.soknad.omsorgspenger.TestUtils.jsonForKomplettSøknad;
import static no.nav.k9.soknad.omsorgspenger.TestUtils.komplettBuilder;


public class OmsorgspengerSøknadTest {
    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = jsonForKomplettSøknad();
        OmsorgspengerSøknad fraBuilder = komplettBuilder().build();
        JSONAssert.assertEquals(json, JsonUtils.toString(fraBuilder), true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = jsonForKomplettSøknad();
        OmsorgspengerSøknad søknad = JsonUtils.fromString(json, OmsorgspengerSøknad.class);
        JSONAssert.assertEquals(json, JsonUtils.toString(søknad), true);
    }
}
