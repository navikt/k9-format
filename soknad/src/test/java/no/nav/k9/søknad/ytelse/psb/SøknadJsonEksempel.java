package no.nav.k9.søknad.ytelse.psb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import no.nav.k9.søknad.Søknad;

public class SøknadJsonEksempel {

    public static Søknad utenPeriodisertDataJson() {
        return Søknad.SerDes.deserialize(jsonFromFile("uten-periodisert-data.json"));
    }

    public static Søknad søknadMedNullFeil() {
        return Søknad.SerDes.deserialize(jsonFromFile("søknad-null-feil.json"));
    }

    private static String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/ytelse/psb/" + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
