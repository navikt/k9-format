package no.nav.k9.oppgave;

import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.TestValidator;
import no.nav.k9.søknad.felles.Feil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifiserer at FRITEKST-mønsteret (Patterns.FRITEKST) tillater og avviser
 * tegn i henhold til ADR-en for input-validering (felt-input-validering.md – Fritekst).
 * <p>
 * Mønster: {@code ^[\p{L}\p{M}\p{N}\p{P}\p{S}\p{Space}]*$}
 */
class FritekstPatternTest {

    private static final TestValidator VALIDATOR = new TestValidator();

    @ParameterizedTest(name = "gyldig uttalelse: {0}")
    @ValueSource(strings = {
            "Jeg er uenig i dette vedtaket",
            "æøå ÆØÅ",
            "Det er riktig – jeg bodde i utlandet.",
            "Tekst med linjeskift\nog mer tekst",
            "Unicode tall: ½ ¾ ③",
            "Arabisk: مرحبا",
            "Kinesisk: 你好",
            "Emoji: 😊👍",
    })
    void uttalelse_med_gyldig_tekst_gir_ingen_feil(String uttalelse) {
        var bekreftelse = JsonUtils.fromString(lagJson(uttalelse), Bekreftelse.class);
        VALIDATOR.verifyIngenFeil(bekreftelse);
    }

    @ParameterizedTest(name = "ugyldig uttalelse med kontrollkarakter: {0}")
    @ValueSource(strings = {
            "Null-byte: \\u0000",
            "Bell: \\u0007",
            "Delete: \\u007F",
    })
    void uttalelse_med_kontrollkarakter_gir_ugyldigSyntaks(String jsonEscapedUttalelse) {
        // Kontrollkarakterer sendes som JSON unicode-escape (f.eks. \u0000) slik at
        // Jackson kan parse JSON-en, men regex-valideringen avviser innholdet.
        var json = """
                {
                  "type": "UNG_AVVIK_REGISTERINNTEKT",
                  "oppgaveReferanse": "00000000-0000-0000-0000-000000000099",
                  "harUttalelse": true,
                  "uttalelseFraBruker": "%s"
                }
                """.formatted(jsonEscapedUttalelse);
        var bekreftelse = JsonUtils.fromString(json, Bekreftelse.class);
        List<Feil> feil = VALIDATOR.verifyHarFeil(bekreftelse);
        assertThat(feil)
                .extracting(Feil::getFeilkode)
                .contains("ugyldigSyntaks");
    }

    private static String lagJson(String uttalelse) {
        String escaped = uttalelse
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
        return """
                {
                  "type": "UNG_AVVIK_REGISTERINNTEKT",
                  "oppgaveReferanse": "00000000-0000-0000-0000-000000000099",
                  "harUttalelse": true,
                  "uttalelseFraBruker": "%s"
                }
                """.formatted(escaped);
    }
}
