package no.nav.k9.oppgave;

import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.aktivitet.AktivitetAvklaringBekreftelse;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.TestValidator;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AktivitetAvklaringBekreftelseTest {

    @Test
    void validering_feiler_når_harUttalelse_true_men_uttalelse_mangler() {
        String json = """
                {
                  "type": "AVP_AKTIVITET_AVKLARING",
                  "oppgaveReferanse": "00000000-0000-0000-0000-000000000009",
                  "harUttalelse": true
                }
                """;
        BekreftelseValideringTestUtil.assertManglendeUttalelseGirFeil(json);
    }

    @Test
    void validering_ok_når_harUttalelse_true_og_uttalelse_satt() {
        String json = """
                {
                  "type": "AVP_AKTIVITET_AVKLARING",
                  "oppgaveReferanse": "00000000-0000-0000-0000-000000000009",
                  "harUttalelse": true,
                  "uttalelseFraBruker": "Jeg er enig"
                }
                """;

        new TestValidator().verifyIngenFeil(JsonUtils.fromString(json, Bekreftelse.class));
    }

    @Test
    void roundtrip_serialiserer_riktig_type_og_deserialiserer_til_riktig_record() {
        var original = new AktivitetAvklaringBekreftelse(
                UUID.fromString("00000000-0000-0000-0000-000000000009"),
                true,
                "Jeg er uenig");

        String json = JsonUtils.toString(original);
        assertThat(json).contains("\"AVP_AKTIVITET_AVKLARING\"");
        assertThat(BekreftelseSerialisertypeTest.antallForekomster(json, "\"type\""))
                .as("type-feltet skal kun forekomme én gang i JSON")
                .isEqualTo(1);

        var roundtrip = (AktivitetAvklaringBekreftelse) JsonUtils.fromString(json, Bekreftelse.class);
        assertThat(roundtrip).isEqualTo(original);
        assertThat(roundtrip.getType()).isEqualTo(Bekreftelse.Type.AVP_AKTIVITET_AVKLARING);
    }
}
