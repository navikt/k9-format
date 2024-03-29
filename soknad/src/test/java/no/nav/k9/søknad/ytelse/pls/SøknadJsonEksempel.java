package no.nav.k9.søknad.ytelse.pls;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        return Søknad.SerDes.deserialize(jsonFromFile("komplett-søknad.json"));
    }

    public static Søknad søknadUtenPleietrengendNorskIdent() {
        return Søknad.SerDes.deserialize(jsonFromFile("søknad-uten-pleietrengende-norskident.json"));
    }

    public static Søknad søknadUtenPleietrengendeInfo() {
        return Søknad.SerDes.deserialize(jsonFromFile("søknad-uten-pleietrengende-info.json"));
    }

    public static Søknad søknadUtenPleietrengendeIdentOgFødselsdato() {
        return Søknad.SerDes.deserialize(jsonFromFile("søknad-med-pleietrengende-norskident-og-fødselsdato.json"));
    }
}
