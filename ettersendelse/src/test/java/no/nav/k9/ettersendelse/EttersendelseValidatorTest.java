package no.nav.k9.ettersendelse;

import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.*;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static no.nav.k9.ettersendelse.TestUtils.jsonForKomplettEttersendelse;
import static no.nav.k9.ettersendelse.TestUtils.komplettBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class EttersendelseValidatorTest {
    private static final EttersendelseValidator validator = new EttersendelseValidator();

    @Test
    public void ettersendelseUtenNoeSatt() {
        Ettersendelse.Builder builder = Ettersendelse.builder();
        Ettersendelse ettersendelse = Ettersendelse.SerDes.deserialize("{\"versjon\":\"0.0.1\"}");
        List<Feil> builderFeil = verifyHarFeil(builder);
        List<Feil> jsonFeil = verifyHarFeil(ettersendelse);
        assertThat(builderFeil, is(jsonFeil));
    }

    @Test
    public void ettersendelseUtenSøknadId() {
        Ettersendelse.Builder builder = komplettBuilder().søknadId(null);
        verifyIngenFeil(builder);
    }

    @Test
    public void ettersendelseForOmsorgspenger() {
        Ettersendelse.Builder builder = komplettBuilder().ytelse(Ytelse.OMSORGSPENGER);
        verifyIngenFeil(builder);
    }

    @Test
    public void ettersendelseUtenYtelse() {
        Ettersendelse.Builder builder = komplettBuilder().ytelse(null);
        assertEquals(1, verifyHarFeil(builder).size());
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
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }
    private List<Feil> verifyHarFeil(Ettersendelse ettersendelse) {
        final List<Feil> feil = validator.valider(ettersendelse);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }

    private void verifyIngenFeil(Ettersendelse.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(Collections.emptyList()));
    }

    private void verifyIngenFeil(Ettersendelse ettersendelse) {
        final List<Feil> feil = validator.valider(ettersendelse);
        assertThat(feil, is(Collections.emptyList()));
    }
}
