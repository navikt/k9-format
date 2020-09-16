package no.nav.k9.søknad.omsorgspenger.overføring;

import static junit.framework.TestCase.assertEquals;
import static no.nav.k9.søknad.omsorgspenger.overføring.TestUtils.jsonForKomplettSøknad;
import static no.nav.k9.søknad.omsorgspenger.overføring.TestUtils.komplettBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Barn;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.NorskIdentitetsnummer;

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

    @Test
    public void barnISøknad() {
        var builder = komplettBuilder();
        builder.barn = new ArrayList<>();
        verifyIngenFeil(builder);
        builder.barn(Barn.builder().build());
        assertEquals(1, verifyHarFeil(builder).size());
        builder.barn = new ArrayList<>();
        builder.barn(Barn.builder().fødselsdato(LocalDate.now()).norskIdentitetsnummer(NorskIdentitetsnummer.of("123")).build());
        assertEquals(1, verifyHarFeil(builder).size());
        builder.barn = new ArrayList<>();
        builder.barn(Barn.builder().fødselsdato(LocalDate.now()).build());
        builder.barn(Barn.builder().norskIdentitetsnummer(NorskIdentitetsnummer.of("123")).build());
        verifyIngenFeil(builder);
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
}
