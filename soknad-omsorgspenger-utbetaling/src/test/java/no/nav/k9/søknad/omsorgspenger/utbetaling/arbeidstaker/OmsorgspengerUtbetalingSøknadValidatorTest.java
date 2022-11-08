package no.nav.k9.søknad.omsorgspenger.utbetaling.arbeidstaker;

import static no.nav.k9.søknad.omsorgspenger.utbetaling.arbeidstaker.TestUtils.jsonForKomplettSøknad;
import static no.nav.k9.søknad.omsorgspenger.utbetaling.arbeidstaker.TestUtils.komplettBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;

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
        assertThat(builderFeil).hasSameSizeAs(jsonFeil);
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
        builder.fosterbarn(new Barn());
        assertThat(verifyHarFeil(builder)).hasSize(1);
        builder.barn = new ArrayList<>();
        builder.fosterbarn(new Barn().medFødselsdato(LocalDate.now()).medNorskIdentitetsnummer(NorskIdentitetsnummer.of("123")));
        assertThat(verifyHarFeil(builder)).hasSize(1);
        builder.barn = new ArrayList<>();
        builder.fosterbarn(new Barn().medFødselsdato(LocalDate.now()));
        builder.fosterbarn(new Barn().medNorskIdentitetsnummer(NorskIdentitetsnummer.of("123")));
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
        assertThat(feil).isNotEmpty();
        return feil;
    }

    private List<Feil> verifyHarFeil(OmsorgspengerUtbetalingSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    private void verifyIngenFeil(OmsorgspengerUtbetalingSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil).isEmpty();
    }

    private void verifyIngenFeil(OmsorgspengerUtbetalingSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil).isEmpty();
    }

}
