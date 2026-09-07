package no.nav.k9.oppgave;

import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.TestValidator;
import org.junit.jupiter.api.Test;

class EndretPeriodeBekreftelseTest {

    @Test
    void validering_feiler_når_harUttalelse_true_men_uttalelse_mangler() {
        String json = """
                {
                  "type": "UNG_ENDRET_PERIODE",
                  "oppgaveReferanse": "00000000-0000-0000-0000-000000000003",
                  "harUttalelse": true
                }
                """;
        BekreftelseValideringTestUtil.assertManglendeUttalelseGirFeil(json);
    }

    @Test
    void validering_ok_når_harUttalelse_true_og_uttalelse_satt() {
        String json = """
                {
                  "type": "UNG_ENDRET_PERIODE",
                  "oppgaveReferanse": "00000000-0000-0000-0000-000000000003",
                  "harUttalelse": true,
                  "uttalelseFraBruker": "Jeg er enig"
                }
                """;

        new TestValidator().verifyIngenFeil(JsonUtils.fromString(json, Bekreftelse.class));
    }
}

