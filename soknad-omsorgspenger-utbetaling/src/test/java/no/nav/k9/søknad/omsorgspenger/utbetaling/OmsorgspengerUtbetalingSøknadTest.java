package no.nav.k9.søknad.omsorgspenger.utbetaling;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static no.nav.k9.søknad.omsorgspenger.utbetaling.TestUtils.jsonForKomplettSøknad;
import static no.nav.k9.søknad.omsorgspenger.utbetaling.TestUtils.komplettBuilder;

public class OmsorgspengerUtbetalingSøknadTest {
    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = jsonForKomplettSøknad();
        OmsorgspengerUtbetalingSøknad fraBuilder = komplettBuilder().build();
        JSONAssert.assertEquals(json, OmsorgspengerUtbetalingSøknad.SerDes.serialize(fraBuilder), true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = jsonForKomplettSøknad();
        OmsorgspengerUtbetalingSøknad søknad = OmsorgspengerUtbetalingSøknad.SerDes.deserialize(json);
        JSONAssert.assertEquals(json, OmsorgspengerUtbetalingSøknad.SerDes.serialize(søknad), true);
    }
}
