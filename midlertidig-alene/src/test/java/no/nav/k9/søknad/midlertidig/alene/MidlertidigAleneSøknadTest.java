package no.nav.k9.søknad.midlertidig.alene;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static no.nav.k9.søknad.midlertidig.alene.TestUtils.jsonForKomplettSøknad;
import static no.nav.k9.søknad.midlertidig.alene.TestUtils.komplettBuilder;

public class MidlertidigAleneSøknadTest {

    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = jsonForKomplettSøknad();
        var fraBuilder = komplettBuilder().build();
        JSONAssert.assertEquals(json, MidlertidigAleneSøknad.SerDes.serialize(fraBuilder), true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = jsonForKomplettSøknad();
        var søknad = MidlertidigAleneSøknad.SerDes.deserialize(json);
        JSONAssert.assertEquals(json, MidlertidigAleneSøknad.SerDes.serialize(søknad), true);
    }

}