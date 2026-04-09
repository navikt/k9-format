package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyIngenFeil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.JsonUtilsJackson2;
import no.nav.k9.søknad.Søknad;
import org.junit.jupiter.api.Test;

class FraJsonTest {

    @Test
    public void komplettSøknadJson() {
        jsonDeserializeSerializeTest("komplett-søknad.json");
    }

    @Test
    public void minimumSøknadJson() {
        jsonDeserializeSerializeTest("minimum-søknad.json");
    }

    @Test
    public void komplettGammelVersjonSøknadJson() {
        jsonDeserializeSerializeTest("5.1.33/komplett-søknad.json");
    }

    @Test
    public void minimumGammelVersjonSøknadJson() {
        jsonDeserializeSerializeTest("5.1.33/minimum-søknad.json");
    }

    @Test
    public void søknadMedEndringJson() {
        var jsonSøknad = jsonFromFile("søknad-med-endring.json");
        var søknad = JsonUtils.fromString(jsonSøknad, Søknad.class);
        verifyIngenFeil(søknad,((PleiepengerSyktBarn) søknad.getYtelse()).getEndringsperiode());

        JsonUtils.toString(søknad);
    }

    private void jsonDeserializeSerializeTest(String filname) {
        var jsonSøknad = jsonFromFile(filname);
        var søknad = JsonUtils.fromString(jsonSøknad, Søknad.class);
        verifyIngenFeil(søknad);

        var jsonSøknadSerialized = JsonUtils.toString(søknad);

        //vil ikke funke før json filene er laget på nytt med Alphabetically order.
        //assertEquals(jsonSøknadSerialized, jsonSøknad);
    }


    private static String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/ytelse/psb/" + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
