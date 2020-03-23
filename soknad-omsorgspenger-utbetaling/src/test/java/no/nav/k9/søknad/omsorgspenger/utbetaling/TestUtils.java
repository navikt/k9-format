package no.nav.k9.søknad.omsorgspenger.utbetaling;

import no.nav.k9.søknad.felles.Barn;
import no.nav.k9.søknad.felles.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.Søker;
import no.nav.k9.søknad.felles.SøknadId;

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

    static String jsonForSøknadUtenBarn() {
        try {
            return Files.readString(Path.of("src/test/resources/uten-barn.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static OmsorgspengerUtbetalingSøknad.Builder komplettBuilder() {
        return OmsorgspengerUtbetalingSøknad
                .builder()
                .søker(Søker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build())
                .barn(Barn.builder().norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111113")).build())
                .barn(Barn.builder().fødselsdato(LocalDate.parse("2000-01-01")).build())
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .søknadId(SøknadId.of("123-123-123"));
    }
}