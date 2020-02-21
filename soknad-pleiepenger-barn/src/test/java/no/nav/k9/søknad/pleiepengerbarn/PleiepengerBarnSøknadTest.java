package no.nav.k9.søknad.pleiepengerbarn;

import org.json.JSONException;
import org.junit.Test;

import org.skyscreamer.jsonassert.JSONAssert;

public class PleiepengerBarnSøknadTest {
    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = TestUtils.jsonForKomplettSøknad();
        PleiepengerBarnSøknad fraBuilder = TestUtils.komplettBuilder().build();
        JSONAssert.assertEquals(json, PleiepengerBarnSøknad.SerDes.serialize(fraBuilder), true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = TestUtils.jsonForKomplettSøknad();
        PleiepengerBarnSøknad søknad = PleiepengerBarnSøknad.SerDes.deserialize(json);
        JSONAssert.assertEquals(json, PleiepengerBarnSøknad.SerDes.serialize(søknad), true);
    }
}