package no.nav.k9.ettersendelse;

import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.SøknadId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;

class TestUtils {

    static String jsonForKomplettEttersendelse() {
        try {
            return Files.readString(Path.of("src/test/resources/ettersendelse.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static Ettersendelse.Builder komplettBuilder() {
        return Ettersendelse
                .builder()
                .søker(Søker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build())
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .søknadId(SøknadId.of("123-123-123"))
                .ytelse(Ytelse.valueOf("PLEIEPENGER_SYKT_BARN"));
    }
}
