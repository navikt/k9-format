package no.nav.k9.søknad.omsorgspenger.overføring;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static no.nav.k9.søknad.omsorgspenger.overføring.TestUtils.*;
import static org.junit.Assert.assertEquals;

public class OmsorgspengerOverføringSøknadTest {
    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = jsonForKomplettSøknad();
        var fraBuilder = komplettBuilder().build();
        JSONAssert.assertEquals(json, OmsorgspengerOverføringSøknad.SerDes.serialize(fraBuilder), true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = jsonForKomplettSøknad();
        var søknad = OmsorgspengerOverføringSøknad.SerDes.deserialize(json);
        JSONAssert.assertEquals(json, OmsorgspengerOverføringSøknad.SerDes.serialize(søknad), true);
    }

    @Test
    public void deserialisereSøknadUtenBarn() {
        var json = jsonForSøknadUtenBarn();
        var søknad = OmsorgspengerOverføringSøknad.SerDes.deserialize(json);
        assertEquals(0, søknad.barn.size());
    }
}
