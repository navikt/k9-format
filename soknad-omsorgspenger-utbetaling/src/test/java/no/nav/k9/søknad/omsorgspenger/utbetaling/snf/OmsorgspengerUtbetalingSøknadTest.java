package no.nav.k9.søknad.omsorgspenger.utbetaling.snf;

import static no.nav.k9.søknad.omsorgspenger.utbetaling.snf.TestUtils.jsonForKomplettSøknad;
import static no.nav.k9.søknad.omsorgspenger.utbetaling.snf.TestUtils.jsonForKomplettSøknadMedBarn;
import static no.nav.k9.søknad.omsorgspenger.utbetaling.snf.TestUtils.jsonForKomplettSøknadUtenNæringsinntenkt;
import static no.nav.k9.søknad.omsorgspenger.utbetaling.snf.TestUtils.jsonForSøknadUtenBarn;
import static no.nav.k9.søknad.omsorgspenger.utbetaling.snf.TestUtils.komplettBuilder;
import static no.nav.k9.søknad.omsorgspenger.utbetaling.snf.TestUtils.komplettBuilderUtenNæringsinntekt;
import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

@SuppressWarnings("removal")
public class OmsorgspengerUtbetalingSøknadTest {
    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = jsonForKomplettSøknad();
        OmsorgspengerUtbetalingSøknad fraBuilder = komplettBuilder().build();
        JSONAssert.assertEquals(json, OmsorgspengerUtbetalingSøknad.SerDes.serialize(fraBuilder), true);
    }
    @Test
    public void serialiseringAvJsonUtenNæringsinntektOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = jsonForKomplettSøknadUtenNæringsinntenkt();
        OmsorgspengerUtbetalingSøknad fraBuilder = komplettBuilderUtenNæringsinntekt().build();
        JSONAssert.assertEquals(json, OmsorgspengerUtbetalingSøknad.SerDes.serialize(fraBuilder), false);
    }

    @Disabled
    @Test
    public void serialiseringAvJsonMedBarnOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = jsonForKomplettSøknadMedBarn();
        OmsorgspengerUtbetalingSøknad fraBuilder = komplettBuilder().build();
        JSONAssert.assertEquals(json, OmsorgspengerUtbetalingSøknad.SerDes.serialize(fraBuilder), true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = jsonForKomplettSøknad();
        OmsorgspengerUtbetalingSøknad søknad = OmsorgspengerUtbetalingSøknad.SerDes.deserialize(json);
        String jsonSerialized = OmsorgspengerUtbetalingSøknad.SerDes.serialize(søknad);
        JSONAssert.assertEquals(json, jsonSerialized, true);
    }

    @Test
    public void deserialisereSøknadUtenBarn() {
        var json = jsonForSøknadUtenBarn();
        var søknad = OmsorgspengerUtbetalingSøknad.SerDes.deserialize(json);
        assertThat(søknad.fosterbarn).isEmpty();
    }
}
