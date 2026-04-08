package no.nav.k9.ettersendelse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;

import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.SøknadId;

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
                .søker(new Søker(NorskIdentitetsnummer.of("11111111111")))
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .søknadId(SøknadId.of("123-123-123"))
                .ytelse(Ytelse.valueOf("PLEIEPENGER_SYKT_BARN"))
                .pleietrengende(new Pleietrengende(NorskIdentitetsnummer.of("22222222222")))
                .type(EttersendelseType.ANNET);
    }
}
