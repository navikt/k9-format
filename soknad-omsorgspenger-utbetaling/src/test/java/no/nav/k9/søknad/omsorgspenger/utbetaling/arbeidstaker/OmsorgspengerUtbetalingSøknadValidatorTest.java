package no.nav.k9.søknad.omsorgspenger.utbetaling.arbeidstaker;

import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static no.nav.k9.søknad.omsorgspenger.utbetaling.arbeidstaker.TestUtils.jsonForKomplettSøknad;
import static no.nav.k9.søknad.omsorgspenger.utbetaling.arbeidstaker.TestUtils.komplettBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("removal")
public class OmsorgspengerUtbetalingSøknadValidatorTest {
    private static final OmsorgspengerUtbetalingSøknadValidator validator = new OmsorgspengerUtbetalingSøknadValidator();

    @Test
    public void søknadUtenNoeSatt() {
        OmsorgspengerUtbetalingSøknad.Builder builder = OmsorgspengerUtbetalingSøknad.builder();
        OmsorgspengerUtbetalingSøknad søknad = OmsorgspengerUtbetalingSøknad.SerDes.deserialize("{\"versjon\":\"0.0.1\"}");
        List<Feil> builderFeil = verifyHarFeil(builder);
        builderFeil = Collections.unmodifiableList(builderFeil);
        List<Feil> jsonFeil = verifyHarFeil(søknad);
        jsonFeil = Collections.unmodifiableList(jsonFeil);
        assertThat(builderFeil.size(), is(jsonFeil.size()));
    }

    @Test
    public void komplettSøknadFraJson() {
        OmsorgspengerUtbetalingSøknad søknad = OmsorgspengerUtbetalingSøknad.SerDes.deserialize(jsonForKomplettSøknad());
        verifyIngenFeil(søknad);
    }

    @Test
    public void barnISøknad() {
        var builder = komplettBuilder();
        builder.barn = new ArrayList<>();
        verifyIngenFeil(builder);
        builder.fosterbarn(Barn.builder().build());
        assertThat(verifyHarFeil(builder)).hasSize(1);
        builder.barn = new ArrayList<>();
        builder.fosterbarn(Barn.builder().fødselsdato(LocalDate.now()).norskIdentitetsnummer(NorskIdentitetsnummer.of("123")).build());
        assertThat(verifyHarFeil(builder)).hasSize(1);
        builder.barn = new ArrayList<>();
        builder.fosterbarn(Barn.builder().fødselsdato(LocalDate.now()).build());
        builder.fosterbarn(Barn.builder().norskIdentitetsnummer(NorskIdentitetsnummer.of("123")).build());
        verifyIngenFeil(builder);
    }

    private List<Feil> valider(OmsorgspengerUtbetalingSøknad.Builder builder) {
        try {
            builder.build();
            return Collections.emptyList();
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }

    private List<Feil> verifyHarFeil(OmsorgspengerUtbetalingSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }

    private List<Feil> verifyHarFeil(OmsorgspengerUtbetalingSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }

    private void verifyIngenFeil(OmsorgspengerUtbetalingSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(Collections.emptyList()));
    }

    private void verifyIngenFeil(OmsorgspengerUtbetalingSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil, is(Collections.emptyList()));
    }

}
