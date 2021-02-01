package no.nav.søknad.ytelse.omsorgspenger.utvidetrett;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerKroniskSyktBarn;

public class OmsorgspengerKroniskSyktBarnValidatorTest {
    private static final YtelseValidator validator = new OmsorgspengerKroniskSyktBarn.MinValidator();

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

    private void verifyIngenFeil(Ytelse builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil).isEmpty();
    }

    private void verifyIngenFeil(Søknad søknad) {
        final List<Feil> feil = validator.valider(søknad.getYtelse());
        assertThat(feil).isEmpty();
    }

    private List<Feil> valider(Ytelse builder) {
        try {
            return validator.valider(builder);
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

        static OmsorgspengerKroniskSyktBarn minimumSøknad() {
            var barn = new Barn(null, LocalDate.now());
            return new OmsorgspengerKroniskSyktBarn().medBarn(barn);
        }

    }
}
