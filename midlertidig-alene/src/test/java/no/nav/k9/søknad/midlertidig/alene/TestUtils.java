package no.nav.k9.søknad.midlertidig.alene;

import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.SøknadId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;

public class TestUtils {


    static String jsonForKomplettSøknad() {
        try {
            return Files.readString(Path.of("src/test/resources/komplett-søknad.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static MidlertidigAleneSøknad.Builder komplettBuilder() {
        return MidlertidigAleneSøknad
                .builder()
                .søker(Søker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build())
                .annenForelder(AnnenForelder.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .navn("Navnesen")
                        .situasjon(Situasjon.valueOf("FENGSEL"))
                        .situasjonBeskrivelse("Forklaring")
                        .build())
                .antallBarn(2)
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .søknadId(SøknadId.of("123-123-123"))
                .arbeidssituasjon(Arbeidssituasjon.valueOf("ARBEIDSTAKER"))
                .id("123456789ABC");
    }

}
