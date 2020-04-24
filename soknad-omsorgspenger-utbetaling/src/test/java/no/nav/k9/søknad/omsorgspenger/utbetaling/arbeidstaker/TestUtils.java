package no.nav.k9.søknad.omsorgspenger.utbetaling.arbeidstaker;

import no.nav.k9.søknad.felles.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;

class TestUtils {

    static String jsonForKomplettSøknad() {
        try {
            return Files.readString(Path.of("src/test/resources/arbeidstaker/komplett-søknad.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static String jsonForKomplettSøknadMedBarn() {
        try {
            return Files.readString(Path.of("src/test/resources/arbeidstaker/komplett-søknad-med-barn.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static String jsonForSøknadUtenBarn() {
        try {
            return Files.readString(Path.of("src/test/resources/arbeidstaker/uten-barn.json"));
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
                .fosterbarn(Barn.builder().norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111113")).build())
                .fosterbarn(Barn.builder().fødselsdato(LocalDate.parse("2000-01-01")).build())
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .søknadId(SøknadId.of("123-123-123"));
    }
}
