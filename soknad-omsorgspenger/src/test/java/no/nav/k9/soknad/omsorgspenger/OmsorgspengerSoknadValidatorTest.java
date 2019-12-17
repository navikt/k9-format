package no.nav.k9.soknad.omsorgspenger;

import no.nav.k9.soknad.JsonUtils;
import no.nav.k9.soknad.ValideringsFeil;
import no.nav.k9.soknad.felles.Barn;
import no.nav.k9.soknad.felles.Feil;
import no.nav.k9.soknad.felles.NorskIdentitetsnummer;
import no.nav.k9.soknad.felles.Soker;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class OmsorgspengerSoknadValidatorTest {
    private static final OmsorgspengerSoknadValidator validator = new OmsorgspengerSoknadValidator();

    @Test
    public void soknadUtenNoeSatt() {
        OmsorgspengerSoknad.Builder builder = OmsorgspengerSoknad.builder();
        verifyHarFeil(builder);
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
                        .norskIdentitetsnummer(new NorskIdentitetsnummer("11111111111"))
                        .build()
                );
        verifyIngenFeil(builder);
    }

    @Test
    public void komplettSoknadFraJson() {
        OmsorgspengerSoknad soknad = JsonUtils.fromString(kompletFraJson(), OmsorgspengerSoknad.class);
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

    private void verifyIngenFeil(OmsorgspengerSoknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(Collections.emptyList()));
    }

    private void verifyHarFeil(OmsorgspengerSoknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(not(Collections.emptyList())));
    }

    private void verifyIngenFeil(OmsorgspengerSoknad soknad) {
        final List<Feil> feil = validator.valider(soknad);
        assertThat(feil, is(Collections.emptyList()));
    }

    private OmsorgspengerSoknad.Builder medSoker() {
        return OmsorgspengerSoknad
                .builder()
                .soker(Soker
                        .builder()
                        .norskIdentitetsnummer(new NorskIdentitetsnummer("11111111111"))
                        .build()
                );
    }

    private static String kompletFraJson() {
        try {
            return Files.readString(Path.of("src/test/resources/komplett-soknad.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
