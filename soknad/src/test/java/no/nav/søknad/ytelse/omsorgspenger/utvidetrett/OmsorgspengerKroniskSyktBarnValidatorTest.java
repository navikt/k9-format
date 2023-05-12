package no.nav.søknad.ytelse.omsorgspenger.utvidetrett;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerKroniskSyktBarnSøknadValidator;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerKroniskSyktBarn;

public class OmsorgspengerKroniskSyktBarnValidatorTest {
    private static final OmsorgspengerKroniskSyktBarnSøknadValidator validator = new OmsorgspengerKroniskSyktBarnSøknadValidator();

    @Test
    void minimumSøknadNullTest() {
        var søknad = TestUtils.minimumSøknad();
        verifyIngenFeil(søknad);
    }

    @Test
    void minimumJsonSøknad() {
        var søknad = TestUtils.minimumJsonSøknad();
        verifyIngenFeil(søknad);
    }

    @Test
    void komplettSøknadSkalIkkeHaValideringsfeil() {
        var søknad = TestUtils.komplettSøknad();
        verifyIngenFeil(søknad);

    }

    private void verifyIngenFeil(Søknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil).isEmpty();
    }

    private List<Feil> valider(Søknad søknad) {
        try {
            return validator.valider(søknad);
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }

    private static class TestUtils {

        private static String jsonFromFile(String filename) {
            try {
                return Files.readString(Path.of("src/test/resources/ytelse/omp/utvidetrett/" + filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        static Søknad komplettSøknad() {
            return Søknad.SerDes.deserialize(jsonFromFile("komplett-søknad-kronisk-syk.json"));
        }

        static Søknad minimumJsonSøknad() {
            return Søknad.SerDes.deserialize(jsonFromFile("minimum-søknad-kronisk-syk.json"));
        }

        static Søknad minimumSøknad() {
            var barn = new Barn().medFødselsdato(LocalDate.now());
            return new Søknad()
                    .medSøknadId("222e4a87-f471-4181-9d5b-7d67d046b031")
                    .medMottattDato(ZonedDateTime.now())
                    .medVersjon("1.0.0")
                    .medSøker(new Søker(NorskIdentitetsnummer.of("02119970078")))
                    .medYtelse(new OmsorgspengerKroniskSyktBarn()
                            .medKroniskEllerFunksjonshemming(true)
                            .medBarn(barn));
        }
    }
}
