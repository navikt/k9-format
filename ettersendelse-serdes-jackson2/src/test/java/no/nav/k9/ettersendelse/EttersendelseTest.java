package no.nav.k9.ettersendelse;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class EttersendelseTest {

    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = TestUtils.jsonForKomplettEttersendelse();
        Ettersendelse ettersendelse = TestUtils.komplettBuilder().build();
        String serialize = EttersendelseSerDes.serialize(ettersendelse);
        assertEquals(json, serialize, true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = TestUtils.jsonForKomplettEttersendelse();
        Ettersendelse ettersendelse = EttersendelseSerDes.deserialize(json);
        assertEquals(json,  EttersendelseSerDes.serialize(ettersendelse), true);
    }
}
