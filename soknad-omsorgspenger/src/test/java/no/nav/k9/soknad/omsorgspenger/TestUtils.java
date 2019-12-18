package no.nav.k9.soknad.omsorgspenger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class TestUtils {

    static String kompletFraJson() {
        try {
            return Files.readString(Path.of("src/test/resources/komplett-soknad.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
