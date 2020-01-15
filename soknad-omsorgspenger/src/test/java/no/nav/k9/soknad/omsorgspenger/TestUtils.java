package no.nav.k9.soknad.omsorgspenger;

import no.nav.k9.soknad.felles.Barn;
import no.nav.k9.soknad.felles.NorskIdentitetsnummer;
import no.nav.k9.soknad.felles.Soker;
import no.nav.k9.soknad.felles.SoknadId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;

class TestUtils {

    static String jsonForKomplettSoknad() {
        try {
            return Files.readString(Path.of("src/test/resources/komplett-soknad.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static OmsorgspengerSoknad.Builder komplettBuilder() {
        return OmsorgspengerSoknad
                .builder()
                .barn(Barn.builder()
                        .foedselsdato(LocalDate.parse("2015-01-01"))
                        .build())
                .soker(Soker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build())
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .soknadId(SoknadId.of("123-123-123"));
    }
}