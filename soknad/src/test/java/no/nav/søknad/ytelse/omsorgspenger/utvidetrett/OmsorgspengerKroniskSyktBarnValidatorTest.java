package no.nav.søknad.ytelse.omsorgspenger.utvidetrett;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerKroniskSyktBarn;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerKroniskSyktBarnSøknadValidator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    void skalIkkeSkriveUtSensitivInfo_ForLangTekst() {
        var forLangTekst = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus fermentum, neque ut luctus interdum, massa arcu efficitur augue, nec elementum risus erat quis mauris. Quisque non turpis sed urna convallis tincidunt. Integer id neque et nisl suscipit vehicula. Suspendisse non magna id magna efficitur ullamcorper. Nam feugiat dignissim lacus, nec hendrerit mi congue at. Aliquam erat volutpat. Pellentesque vulputate lectus eget rhoncus tincidunt. Nulla facilisi. Fusce non magna eros. Phasellus suscipit risus vel nulla fermentum, et varius nibh lacinia. Morbi eu risus neque. Donec non sagittis purus. Maecenas bibendum dui a arcu finibus, in tempor arcu viverra. Integer vitae justo nec purus ullamcorper pretium. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Fusce nec dictum turpis, non feugiat sem. Aenean sit amet neque nec nisl posuere ullamcorper nec sed libero. Sed euismod vehicula libero nec tincidunt. Duis posuere, enim id lacinia tincidunt, urna mi pulvinar erat, eu accumsan lectus ipsum vitae augue. Suspendisse potenti. Etiam sit amet felis fermentum, semper nibh vel, sodales turpis. Ut laoreet, mi vel dignissim posuere, risus sapien aliquam est, sit amet ultricies odio dolor at libero. Sed ut purus faucibus, blandit nisl nec, tincidunt magna. Mauris fermentum erat ut lacus faucibus, vel pulvinar odio ultricies. Donec in sapien lacus. Integer ultrices facilisis suscipit. Donec luctus lacus nec justo mattis dignissim.
                """;
        var søknad = TestUtils.søknadMedFraværsbeskrivelse(forLangTekst);
        List<Feil> feilmeldinger = valider(søknad);
        assertThat(feilmeldinger).hasSize(1);
        Feil first = feilmeldinger.getFirst();
        assertThat(first.getFeilmelding()).isEqualTo("Må være mellom 1 og 1000 tegn");
        assertThat(first.getFeilkode()).isEqualTo("påkrevd");
        assertThat(first.getFelt()).isEqualTo("ytelse.høyereRisikoForFraværBeskrivelse");
    }

    @Test
    void skalIkkeSkriveUtSensitivInfo_ugyldigeTegn() {
        var beskrivelse = "\"ikke lov med  «spesielle fnutter»\"";
        var søknad = TestUtils.søknadMedFraværsbeskrivelse(beskrivelse);
        List<Feil> feilmeldinger = valider(søknad);
        assertThat(feilmeldinger).hasSize(1);
        Feil first = feilmeldinger.getFirst();
        assertThat(first.getFeilmelding()).contains("matcher ikke tillatt pattern");
        assertThat(first.getFeilmelding()).doesNotContain(beskrivelse);
        assertThat(first.getFeilkode()).doesNotContain(beskrivelse);
        assertThat(first.getFelt()).isEqualTo("ytelse.høyereRisikoForFraværBeskrivelse");
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

        static Søknad søknadMedFraværsbeskrivelse(String beskrivelse) {
            var barn = new Barn().medFødselsdato(LocalDate.now());
            return new Søknad()
                    .medSøknadId("222e4a87-f471-4181-9d5b-7d67d046b031")
                    .medMottattDato(ZonedDateTime.now())
                    .medVersjon("1.0.0")
                    .medSøker(new Søker(NorskIdentitetsnummer.of("02119970078")))
                    .medYtelse(new OmsorgspengerKroniskSyktBarn(barn, true, true, beskrivelse));
        }
    }
}
