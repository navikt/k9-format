package no.nav.k9.søknad.ytelse.omsorgspenger.v1;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.aktivitet.ArbeidAktivitet;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.fravær.FraværÅrsak;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OmsorgspengerUtbetalingValidatorTest {
    private static final YtelseValidator validator = new OmsorgspengerUtbetalingValidator();

    @Test
    void minimum_json_søknad_skal_ikke_ha_valideringsfeil() {
        var søknad = TestUtils.minimumJsonSøknad();
        verifyIngenFeil(søknad);
    }

    @Test
    void minimum_søknad_skal_ikke_ha_valideringsfeil() {
        var søknad = TestUtils.minimumSøknad();
        verifyIngenFeil(søknad);
    }

    @Test
    void komplett_json_søknad_skal_ikke_ha_valideringsfeil() {
        var søknad = TestUtils.komplettJsonSøknad();
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
                return Files.readString(Path.of("src/test/resources/ytelse/omp/utbetaling/" + filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        static Søknad komplettJsonSøknad() {
            return Søknad.SerDes.deserialize(jsonFromFile("komplett-søknad-omp-utbetaling-snf.json"));
        }

        static Søknad minimumJsonSøknad() {
            return Søknad.SerDes.deserialize(jsonFromFile("minimum-søknad-omp-utbetaling-snf.json"));
        }

        static OmsorgspengerUtbetaling minimumSøknad() {
            return new OmsorgspengerUtbetaling(
                    null,
                    new ArbeidAktivitet(null, null, null),
                    List.of(
                            new FraværPeriode(
                                    null,
                                    null,
                                    FraværÅrsak.ORDINÆRT_FRAVÆR
                            )
                    ),
                    null,
                    null
            );
        }

    }
}
