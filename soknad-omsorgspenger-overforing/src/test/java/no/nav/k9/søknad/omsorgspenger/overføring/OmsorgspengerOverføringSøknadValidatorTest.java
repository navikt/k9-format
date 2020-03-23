package no.nav.k9.søknad.omsorgspenger.overføring;

import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.Søker;
import no.nav.k9.søknad.felles.SøknadId;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static no.nav.k9.søknad.omsorgspenger.overføring.TestUtils.jsonForKomplettSøknad;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class OmsorgspengerOverføringSøknadValidatorTest {
    private static final OmsorgspengerOverføringSøknadValidator validator = new OmsorgspengerOverføringSøknadValidator();

    @Test
    public void søknadUtenNoeSatt() {
        OmsorgspengerOverføringSøknad.Builder builder = OmsorgspengerOverføringSøknad.builder();
        OmsorgspengerOverføringSøknad søknad = OmsorgspengerOverføringSøknad.SerDes.deserialize("{\"versjon\":\"0.0.1\"}");
        List<Feil> builderFeil = verifyHarFeil(builder);
        List<Feil> jsonFeil = verifyHarFeil(søknad);
        assertThat(builderFeil, is(jsonFeil));
    }

    @Test
    public void komplettSøknadFraJson() {
        OmsorgspengerOverføringSøknad søknad = OmsorgspengerOverføringSøknad.SerDes.deserialize(jsonForKomplettSøknad());
        verifyIngenFeil(søknad);
    }

    private List<Feil> valider(OmsorgspengerOverføringSøknad.Builder builder) {
        try {
            builder.build();
            return Collections.emptyList();
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }
    private List<Feil> verifyHarFeil(OmsorgspengerOverføringSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }
    private List<Feil> verifyHarFeil(OmsorgspengerOverføringSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }

    private void verifyIngenFeil(OmsorgspengerOverføringSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(Collections.emptyList()));
    }

    private void verifyIngenFeil(OmsorgspengerOverføringSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil, is(Collections.emptyList()));
    }

    private OmsorgspengerOverføringSøknad.Builder medSøker() {
        return OmsorgspengerOverføringSøknad
                .builder()
                .søknadId(SøknadId.of("123"))
                .mottattDato(ZonedDateTime.now())
                .søker(Søker
                        .builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build()
                );
    }
}
