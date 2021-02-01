package no.nav.søknad.ytelse.omsorgspenger.utvidetrett;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerKroniskSyktBarn;

public class OmsorgspengerKroniskSyktBarnValidatorTest {
    private static final YtelseValidator validator = new OmsorgspengerKroniskSyktBarn.MinValidator();

    @Test
    public void minimumSøknadNullTest() {
        var søknad = TestUtils.minimumSøknad();
        verifyIngenFeil(søknad);
    }
    
    @Test
    public void minimumJsonSøknad() {
        var søknad = TestUtils.minimumJsonSøknad();
        verifyIngenFeil(søknad);
    }

    @Test
    public void komplettSøknadSkalIkkeHaValideringsfeil() {
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
}
