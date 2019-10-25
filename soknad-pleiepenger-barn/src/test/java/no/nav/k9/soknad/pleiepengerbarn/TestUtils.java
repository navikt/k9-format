package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class TestUtils {

    private TestUtils() {

    }

    static final String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/" + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String jsonForKomplettSoknad() {
        return jsonFromFile("komplett-soknad.json");
    }

    static PleiepengerBarnSoknad komplettSoknad() {
        final PleiepengerBarnSoknad soknad = JsonUtils.fromString(jsonForKomplettSoknad(), PleiepengerBarnSoknad.class);
        return soknad;
    }

}
