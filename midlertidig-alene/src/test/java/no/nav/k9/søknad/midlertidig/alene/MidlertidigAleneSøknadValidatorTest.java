package no.nav.k9.søknad.midlertidig.alene;

import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import org.junit.Test;

import static no.nav.k9.søknad.midlertidig.alene.TestUtils.jsonForKomplettSøknad;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collections;
import java.util.List;

public class MidlertidigAleneSøknadValidatorTest {
    private static final MidlertidigAleneSøknadValidator validator = new MidlertidigAleneSøknadValidator();

    @Test
    public void søknadUtenNoeSatt() {
        MidlertidigAleneSøknad.Builder builder = MidlertidigAleneSøknad.builder();
        MidlertidigAleneSøknad søknad = MidlertidigAleneSøknad.SerDes.deserialize("{\"versjon\":\"0.0.1\"}");
        List<Feil> builderFeil = verifyHarFeil(builder);
        List<Feil> jsonFeil = verifyHarFeil(søknad);
        assertThat(builderFeil, is(jsonFeil));
    }

    @Test
    public void komplettSøknadFraJson() {
        MidlertidigAleneSøknad søknad = MidlertidigAleneSøknad.SerDes.deserialize(jsonForKomplettSøknad());
        verifyIngenFeil(søknad);
    }

    private List<Feil> valider(MidlertidigAleneSøknad.Builder builder) {
        try {
            builder.build();
            return Collections.emptyList();
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }

    private List<Feil> verifyHarFeil(MidlertidigAleneSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }

    private List<Feil> verifyHarFeil(MidlertidigAleneSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }

    private void verifyIngenFeil(MidlertidigAleneSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil, is(Collections.emptyList()));
    }

}
