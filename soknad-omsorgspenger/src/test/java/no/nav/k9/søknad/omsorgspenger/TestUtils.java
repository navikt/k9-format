package no.nav.k9.søknad.omsorgspenger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.SøknadId;

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
                .barn(new Barn().medFødselsdato(LocalDate.parse("2015-01-01")))
                .søker(new Søker(NorskIdentitetsnummer.of("11111111111")))
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .søknadId(SøknadId.of("123-123-123"));
    }
}
