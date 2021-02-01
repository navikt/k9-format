package no.nav.søknad.ytelse.omsorgspenger.utvidetrett;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerKroniskSyktBarn;

final class TestUtils {

    private TestUtils() {
    }

    private static String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/ytelse/omp/utvidetrett/" + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Søknad komplettSøknad() {
        return Søknad.SerDes.deserialize(jsonFromFile("komplett-søknad.json"));
    }

    static Søknad minimumJsonSøknad() {
        return Søknad.SerDes.deserialize(jsonFromFile("minimum-søknad.json"));
    }

    static OmsorgspengerKroniskSyktBarn komplettBuilder() {

        var barn = Barn.builder()
            .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
            .fødselsdato(null)
            .build();

        return new OmsorgspengerKroniskSyktBarn(barn, true);
    }

    static OmsorgspengerKroniskSyktBarn minimumSøknad() {
        var barn = new Barn(null, LocalDate.now());
        return new OmsorgspengerKroniskSyktBarn().medBarn(barn);
    }

}
