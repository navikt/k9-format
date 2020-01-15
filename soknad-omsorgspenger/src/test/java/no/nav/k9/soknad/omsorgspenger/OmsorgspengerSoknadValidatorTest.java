package no.nav.k9.soknad.omsorgspenger;

import no.nav.k9.soknad.JsonUtils;
import no.nav.k9.soknad.ValideringsFeil;
import no.nav.k9.soknad.felles.*;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import static no.nav.k9.soknad.omsorgspenger.TestUtils.jsonForKomplettSoknad;

public class OmsorgspengerSoknadValidatorTest {
    private static final OmsorgspengerSoknadValidator validator = new OmsorgspengerSoknadValidator();

    @Test
    public void soknadUtenNoeSatt() {
        OmsorgspengerSoknad.Builder builder = OmsorgspengerSoknad.builder();
        OmsorgspengerSoknad soknad = JsonUtils.fromString("{\"versjon\":\"0.0.1\"}", OmsorgspengerSoknad.class);
        List<Feil> builderFeil = verifyHarFeil(builder);
        List<Feil> jsonFeil = verifyHarFeil(soknad);
        assertThat(builderFeil, is(jsonFeil));
    }

    @Test
    public void soknadMedFoedselsdatoSattPaaBarn() {
        OmsorgspengerSoknad.Builder builder = medSoker()
                .barn(Barn
                        .builder()
                        .foedselsdato(LocalDate.now())
                        .build()
                );
        verifyIngenFeil(builder);
    }

    @Test
    public void soknadMedIdentSattPaaBarn() {
        OmsorgspengerSoknad.Builder builder = medSoker()
                .barn(Barn
                        .builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build()
                );
        verifyIngenFeil(builder);
    }

    @Test
    public void komplettSoknadFraJson() {
        OmsorgspengerSoknad soknad = JsonUtils.fromString(jsonForKomplettSoknad(), OmsorgspengerSoknad.class);
        verifyIngenFeil(soknad);
    }

    private List<Feil> valider(OmsorgspengerSoknad.Builder builder) {
        try {
            builder.build();
            return Collections.emptyList();
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }
    private List<Feil> verifyHarFeil(OmsorgspengerSoknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }
    private List<Feil> verifyHarFeil(OmsorgspengerSoknad soknad) {
        final List<Feil> feil = validator.valider(soknad);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }

    private void verifyIngenFeil(OmsorgspengerSoknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(Collections.emptyList()));
    }

    private void verifyIngenFeil(OmsorgspengerSoknad soknad) {
        final List<Feil> feil = validator.valider(soknad);
        assertThat(feil, is(Collections.emptyList()));
    }

    private OmsorgspengerSoknad.Builder medSoker() {
        return OmsorgspengerSoknad
                .builder()
                .soknadId(SoknadId.of("123"))
                .mottattDato(ZonedDateTime.now())
                .soker(Soker
                        .builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build()
                );
    }
}
