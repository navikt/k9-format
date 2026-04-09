package no.nav.k9.ettersendelse;

import no.nav.k9.søknad.JsonUtils;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class EttersendelseTest {

    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = TestUtils.jsonForKomplettEttersendelse();
        Ettersendelse ettersendelse = TestUtils.komplettBuilder().build();
        String serialize =  JsonUtils.toString(ettersendelse);
        assertEquals(json, serialize, true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = TestUtils.jsonForKomplettEttersendelse();
        Ettersendelse ettersendelse = JsonUtils.fromString(json, Ettersendelse.class);
        assertEquals(json,  JsonUtils.toString(ettersendelse), true);
    }
}
