package no.nav.k9.søknad.ytelse.psb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import no.nav.k9.søknad.Søknad;

public class SøknadJsonEksempel {

    private static String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/ytelse/psb/" + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Søknad komplettSøknadJson() {
        return Søknad.SerDes.deserialize(jsonFromFile("komplett-søknad.json"));
    }

    public static Søknad minimumSøknadJson() {
        return Søknad.SerDes.deserialize(jsonFromFile("minimum-søknad.json"));
    }

    public static Søknad komplettGammelVersjonSøknadJson() {
        return Søknad.SerDes.deserialize(jsonFromFile("5.1.33/komplett-søknad.json"));
    }

    public static Søknad minimumGammelVersjonSøknadJson() {
        return Søknad.SerDes.deserialize(jsonFromFile("5.1.33/minimum-søknad.json"));
    }

    public static Søknad utenPeriodisertDataJson() {
        return Søknad.SerDes.deserialize(jsonFromFile("uten-periodisert-data.json"));
    }

    public static Søknad søknadMedEndring() {
        return Søknad.SerDes.deserialize(jsonFromFile("søknad-med-endring.json"));
    }

    public static Søknad søknadMedNullFeil() {
        return Søknad.SerDes.deserialize(jsonFromFile("søknad-null-feil.json"));
    }
}
