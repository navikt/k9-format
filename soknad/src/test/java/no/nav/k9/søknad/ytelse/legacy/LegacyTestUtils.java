package no.nav.k9.søknad.ytelse.legacy;

import no.nav.k9.søknad.LegacySøknad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("removal")
class LegacyTestUtils {

    private static String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/ytelse/psb/" + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static LegacySøknad legacyJsonFil(String filename) {
        return LegacySøknad.SerDes.deserialize(jsonFromFile(filename));
    }
}
