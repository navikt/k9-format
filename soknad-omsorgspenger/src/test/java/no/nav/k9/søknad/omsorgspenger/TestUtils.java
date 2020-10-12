package no.nav.k9.søknad.omsorgspenger;

import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.SøknadId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;

class TestUtils {

    static String jsonForKomplettSøknad() {
        try {
            return Files.readString(Path.of("src/test/resources/komplett-søknad.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static OmsorgspengerSøknad.Builder komplettBuilder() {
        return OmsorgspengerSøknad
                .builder()
                .barn(Barn.builder()
                        .fødselsdato(LocalDate.parse("2015-01-01"))
                        .build())
                .søker(Søker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build())
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .søknadId(SøknadId.of("123-123-123"));
    }
}
