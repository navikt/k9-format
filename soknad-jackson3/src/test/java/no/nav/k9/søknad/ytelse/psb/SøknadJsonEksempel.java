package no.nav.k9.søknad.ytelse.psb;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.JsonUtilsJackson3;
import no.nav.k9.søknad.Søknad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SøknadJsonEksempel {

    public static Søknad utenPeriodisertDataJson() {
        return JsonUtils.fromString(jsonFromFile("uten-periodisert-data.json"), Søknad.class);
    }

    public static Søknad søknadMedNullFeil() {
        return JsonUtils.fromString(jsonFromFile("søknad-null-feil.json"), Søknad.class);
    }

    private static String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/ytelse/psb/" + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
