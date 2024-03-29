package no.nav.søknad.ytelse.omsorgspenger.utvidetrett;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerMidlertidigAleneSøknadValidator;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.AnnenForelder;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.AnnenForelder.SituasjonType;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerMidlertidigAlene;

public class OmsorgspengerMidlertidigAleneValidatorTest {
    private static final OmsorgspengerMidlertidigAleneSøknadValidator validator = new OmsorgspengerMidlertidigAleneSøknadValidator();

    @Test
    void komplettSøknadSkalIkkeHaValideringsfeil() {
        var søknad = TestUtils.komplettSøknad();
        verifyIngenFeil(søknad);
    }
    
    @Test
    void testKomplettSøknad() {
        var ytelse = TestUtils.komplettBuilder();
        var søker = new Søker(NorskIdentitetsnummer.of("123"));
        var søknad = new Søknad(new SøknadId("123"), new Versjon("0.1"), ZonedDateTime.now(), søker, ytelse);
        
        var json = JsonUtils.toString(søknad);
        
        var søknad2 = JsonUtils.fromString(json, Søknad.class);
        String string = JsonUtils.toString(søknad2);
        assertThat(string).isEqualTo(json);
    }

    private void verifyIngenFeil(Søknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil).isEmpty();
    }

    private static class TestUtils {

        private TestUtils() {
        }

        private static String jsonFromFile(String filename) {
            try {
                return Files.readString(Path.of("src/test/resources/ytelse/omp/utvidetrett/" + filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        static Søknad komplettSøknad() {
            return Søknad.SerDes.deserialize(jsonFromFile("komplett-søknad-midlertidig-alene.json"));
        }

        static OmsorgspengerMidlertidigAlene komplettBuilder() {

            var barn =  new Barn().medNorskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"));

            var annenForelder = new AnnenForelder(NorskIdentitetsnummer.of("21111111111"))
                .medSituasjon(SituasjonType.ANNET, "en annen \nårsak")
                .medPeriode(LocalDate.parse("2021-01-01"), LocalDate.parse("2021-07-01"));

            return new OmsorgspengerMidlertidigAlene(List.of(barn), annenForelder, "ok");
        }

    }
}
