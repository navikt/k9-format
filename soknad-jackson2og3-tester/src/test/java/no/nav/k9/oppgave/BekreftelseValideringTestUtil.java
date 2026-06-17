package no.nav.k9.oppgave;

import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.TestValidator;
import no.nav.k9.søknad.felles.Feil;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Felles testverktøy for {@link Bekreftelse}-implementasjoner: asserterer at
 * {@code @AssertTrue}-valideringen (isUttalelseFraBrukerSattVedHarUttalelse) kjøres etter parsing.
 */
final class BekreftelseValideringTestUtil {

    static final String FORVENTET_FEILMELDING =
            "uttalelseFraBruker må være satt dersom harUttalelse er true";

    private static final TestValidator VALIDATOR = new TestValidator();

    private BekreftelseValideringTestUtil() {
    }

    /**
     * Parser JSON og verifiserer at valideringen feiler med forventet feilmelding når
     * {@code harUttalelse} er true men {@code uttalelseFraBruker} mangler.
     */
    static void assertManglendeUttalelseGirFeil(String json) {
        var bekreftelse = JsonUtils.fromString(json, Bekreftelse.class);

        var feil = VALIDATOR.verifyHarFeil(bekreftelse);
        assertThat(feil)
                .extracting(Feil::getFeilmelding)
                .contains(FORVENTET_FEILMELDING);
    }

}


