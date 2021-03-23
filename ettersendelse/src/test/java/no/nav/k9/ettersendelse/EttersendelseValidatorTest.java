package no.nav.k9.ettersendelse;

import static no.nav.k9.ettersendelse.TestUtils.jsonForKomplettEttersendelse;
import static no.nav.k9.ettersendelse.TestUtils.komplettBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;

public class EttersendelseValidatorTest {
    private static final EttersendelseValidator validator = new EttersendelseValidator();

    @Test
    public void ettersendelseUtenNoeSatt() {
        Ettersendelse.Builder builder = Ettersendelse.builder();
        Ettersendelse ettersendelse = Ettersendelse.SerDes.deserialize("{\"versjon\":\"0.0.1\"}");
        List<Feil> builderFeil = verifyHarFeil(builder);
        List<Feil> jsonFeil = verifyHarFeil(ettersendelse);
        assertThat(builderFeil).containsAll(jsonFeil);
    }

    @Test
    public void ettersendelseUtenSøknadId() {
        Ettersendelse.Builder builder = komplettBuilder().søknadId(null);
        verifyIngenFeil(builder);
    }

    @Test
    public void ettersendelseForYtelse() {
        Ettersendelse.Builder builder = komplettBuilder().ytelse(Ytelse.OMP_UT);
        verifyIngenFeil(builder);
    }

    @Test
    public void ettersendelseUtenYtelse() {
        Ettersendelse.Builder builder = komplettBuilder().ytelse(null);
        assertThat(verifyHarFeil(builder)).hasSize(1);
    }

    @Test
    public void komplettEttersendelseFraJson() {
        Ettersendelse ettersendelse = Ettersendelse.SerDes.deserialize(jsonForKomplettEttersendelse());
        verifyIngenFeil(ettersendelse);
    }

    private List<Feil> valider(Ettersendelse.Builder builder) {
        try {
            builder.build();
            return Collections.emptyList();
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }
    private List<Feil> verifyHarFeil(Ettersendelse.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil).isNotEmpty();
        return feil;
    }
    private List<Feil> verifyHarFeil(Ettersendelse ettersendelse) {
        final List<Feil> feil = validator.valider(ettersendelse);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    private void verifyIngenFeil(Ettersendelse.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil).isEmpty();
    }

    private void verifyIngenFeil(Ettersendelse ettersendelse) {
        final List<Feil> feil = validator.valider(ettersendelse);
        assertThat(feil).isEmpty();
    }
}
