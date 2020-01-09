
package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.JsonUtils;
import no.nav.k9.soknad.ValideringsFeil;
import no.nav.k9.soknad.felles.*;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class PleiepengerBarnSoknadValidatorTest {
    private static final PleiepengerBarnSoknadValidator validator = new PleiepengerBarnSoknadValidator();

    @Test
    public void soknadUtenNoeSatt() {
        PleiepengerBarnSoknad.Builder builder = PleiepengerBarnSoknad.builder();
        PleiepengerBarnSoknad soknad = JsonUtils.fromString("{\"versjon\":\"0.0.1\"}", PleiepengerBarnSoknad.class);
        List<Feil> builderFeil = verifyHarFeil(builder);
        List<Feil> jsonFeil = verifyHarFeil(soknad);
        assertThat(builderFeil, is(jsonFeil));
    }

    @Test
    public void komplettSoknadSkalIkkeHaValideringsfeil() {
        final PleiepengerBarnSoknad soknad = TestUtils.komplettSoknad();
        verifyIngenFeil(soknad);
    }

    @Test
    public void mottattDatoErPaakrevd() {
        final PleiepengerBarnSoknad.Builder builder = TestUtils.komplettBuilder();

        builder.mottattDato(null);
        verifyHarFeil(builder);

        builder.mottattDato(ZonedDateTime.now());
        verifyIngenFeil(builder);
    }

    @Test
    public void sokerNorskIdentitetsnummerPaakrevd() {
        final PleiepengerBarnSoknad.Builder builder = TestUtils.komplettBuilder();

        builder.soker(Soker.builder().build());
        verifyHarFeil(builder);

        builder.soker(Soker.builder().norskIdentitetsnummer(NorskIdentitetsnummer.of("123")).build());
        verifyIngenFeil(builder);
    }

    @Test
    public void soknadsperiodeErPaakrevd() {
        final PleiepengerBarnSoknad.Builder builder = TestUtils.komplettBuilder();

        builder.periode(null);
        verifyHarFeil(builder);

        builder.periode(Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().plusDays(1)).build());
        verifyIngenFeil(builder);
    }

    private List<Feil> verifyHarFeil(PleiepengerBarnSoknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }
    private List<Feil> verifyHarFeil(PleiepengerBarnSoknad soknad) {
        final List<Feil> feil = validator.valider(soknad);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }
    private void verifyIngenFeil(PleiepengerBarnSoknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(Collections.emptyList()));
    }
    private void verifyIngenFeil(PleiepengerBarnSoknad soknad) {
        final List<Feil> feil = validator.valider(soknad);
        assertThat(feil, is(Collections.emptyList()));
    }

    private List<Feil> valider(PleiepengerBarnSoknad.Builder builder) {
        try {
            builder.build();
            return Collections.emptyList();
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }
}
