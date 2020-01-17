package no.nav.k9.søknad.omsorgspenger;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.*;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import static no.nav.k9.søknad.omsorgspenger.TestUtils.jsonForKomplettSøknad;

public class OmsorgspengerSøknadValidatorTest {
    private static final OmsorgspengerSøknadValidator validator = new OmsorgspengerSøknadValidator();

    @Test
    public void søknadUtenNoeSatt() {
        OmsorgspengerSøknad.Builder builder = OmsorgspengerSøknad.builder();
        OmsorgspengerSøknad søknad = JsonUtils.fromString("{\"versjon\":\"0.0.1\"}", OmsorgspengerSøknad.class);
        List<Feil> builderFeil = verifyHarFeil(builder);
        List<Feil> jsonFeil = verifyHarFeil(søknad);
        assertThat(builderFeil, is(jsonFeil));
    }

    @Test
    public void søknadMedFødselsdatoSattPåBarn() {
        OmsorgspengerSøknad.Builder builder = medSøker()
                .barn(Barn
                        .builder()
                        .fødselsdato(LocalDate.now())
                        .build()
                );
        verifyIngenFeil(builder);
    }

    @Test
    public void søknadMedIdentSattPåBarn() {
        OmsorgspengerSøknad.Builder builder = medSøker()
                .barn(Barn
                        .builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build()
                );
        verifyIngenFeil(builder);
    }

    @Test
    public void komplettSøknadFraJson() {
        OmsorgspengerSøknad søknad = JsonUtils.fromString(jsonForKomplettSøknad(), OmsorgspengerSøknad.class);
        verifyIngenFeil(søknad);
    }

    private List<Feil> valider(OmsorgspengerSøknad.Builder builder) {
        try {
            builder.build();
            return Collections.emptyList();
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }
    private List<Feil> verifyHarFeil(OmsorgspengerSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }
    private List<Feil> verifyHarFeil(OmsorgspengerSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }

    private void verifyIngenFeil(OmsorgspengerSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(Collections.emptyList()));
    }

    private void verifyIngenFeil(OmsorgspengerSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil, is(Collections.emptyList()));
    }

    private OmsorgspengerSøknad.Builder medSøker() {
        return OmsorgspengerSøknad
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
