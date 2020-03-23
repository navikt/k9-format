package no.nav.k9.søknad.omsorgspenger.overføring;

import no.nav.k9.søknad.felles.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.Søker;
import no.nav.k9.søknad.felles.SøknadId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;

class TestUtils {

    static String jsonForKomplettSøknad() {
        try {
            return Files.readString(Path.of("src/test/resources/komplett-søknad.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static OmsorgspengerOverføringSøknad.Builder komplettBuilder() {
        return OmsorgspengerOverføringSøknad
                .builder()
                .søker(Søker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build())
                .mottaker(Mottaker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111112"))
                        .build())
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .søknadId(SøknadId.of("123-123-123"));
    }
}