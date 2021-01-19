package no.nav.k9.søknad.omsorgspenger;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static no.nav.k9.søknad.omsorgspenger.TestUtils.jsonForKomplettSøknad;
import static no.nav.k9.søknad.omsorgspenger.TestUtils.komplettBuilder;

public class OmsorgspengerSøknadTest {
    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = jsonForKomplettSøknad();
        OmsorgspengerSøknad fraBuilder = komplettBuilder().build();
        String serialized = OmsorgspengerSøknad.SerDes.serialize(fraBuilder);
        JSONAssert.assertEquals(json, serialized, true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = jsonForKomplettSøknad();
        OmsorgspengerSøknad søknad = OmsorgspengerSøknad.SerDes.deserialize(json);
        String serialized = OmsorgspengerSøknad.SerDes.serialize(søknad);
        JSONAssert.assertEquals(json, serialized, true);
    }
}
