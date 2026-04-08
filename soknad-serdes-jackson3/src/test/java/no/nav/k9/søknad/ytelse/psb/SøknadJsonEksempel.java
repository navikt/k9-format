package no.nav.k9.søknad.ytelse.psb;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.SøknadSerDes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SøknadJsonEksempel {

    public static Søknad utenPeriodisertDataJson() {
        return SøknadSerDes.deserialize(jsonFromFile("uten-periodisert-data.json"));
    }

    public static Søknad søknadMedNullFeil() {
        return SøknadSerDes.deserialize(jsonFromFile("søknad-null-feil.json"));
    }

    private static String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/ytelse/psb/" + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
