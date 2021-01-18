package no.nav.k9.søknad.ytelse.legacy;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collections;

import org.junit.Test;

import no.nav.k9.søknad.Validator;

public class LegacySøknadValidatorTest {

    @Test
    public void test_legacy_psb_søknad() {
        var søknad = LegacyTestUtils.legacyJsonFil("legacy-2021-01-psb-søknad.json");
        verifyIngenFeil(søknad);
    }

    @Test
    public void test_legacy_søknad_1() {
        var søknad = LegacyTestUtils.legacyJsonFil("legacy-2021-01-søknad_1.json");
        verifyIngenFeil(søknad);
    }

    private void verifyIngenFeil(Object builder) {
        assertThat(Validator.validerTilFeil(builder), is(Collections.emptyList()));
    }

}
