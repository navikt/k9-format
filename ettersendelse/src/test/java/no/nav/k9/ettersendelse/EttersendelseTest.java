package no.nav.k9.ettersendelse;

import org.json.JSONException;
import org.junit.Test;

import static no.nav.k9.ettersendelse.Ettersendelse.SerDes.serialize;
import static no.nav.k9.ettersendelse.Ettersendelse.SerDes.deserialize;

import static no.nav.k9.ettersendelse.TestUtils.jsonForKomplettEttersendelse;
import static no.nav.k9.ettersendelse.TestUtils.komplettBuilder;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class EttersendelseTest {

    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = jsonForKomplettEttersendelse();
        assertEquals(json, serialize(komplettBuilder().build()), true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = jsonForKomplettEttersendelse();
        Ettersendelse ettersendelse = deserialize(json);
        assertEquals(json, serialize(ettersendelse), true);
    }
}