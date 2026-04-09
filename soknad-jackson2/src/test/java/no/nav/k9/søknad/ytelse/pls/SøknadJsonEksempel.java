package no.nav.k9.søknad.ytelse.pls;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;

public class SøknadJsonEksempel {

    private static String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/ytelse/pls/" + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Søknad komplettSøknad() {
        return JsonUtils.fromString(jsonFromFile("komplett-søknad.json"), Søknad.class);
    }

    public static Søknad søknadUtenPleietrengendNorskIdent() {
        return JsonUtils.fromString(jsonFromFile("søknad-uten-pleietrengende-norskident.json"), Søknad.class);
    }

    public static Søknad søknadUtenPleietrengendeInfo() {
        return JsonUtils.fromString(jsonFromFile("søknad-uten-pleietrengende-info.json"), Søknad.class);
    }

    public static Søknad søknadUtenPleietrengendeIdentOgFødselsdato() {
        return JsonUtils.fromString(jsonFromFile("søknad-med-pleietrengende-norskident-og-fødselsdato.json"), Søknad.class);
    }
}
