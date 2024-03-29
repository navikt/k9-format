package no.nav.k9.søknad.omsorgspenger;

import static no.nav.k9.søknad.omsorgspenger.TestUtils.jsonForKomplettSøknad;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.SøknadId;

public class OmsorgspengerSøknadValidatorTest {
    private static final OmsorgspengerSøknadValidator validator = new OmsorgspengerSøknadValidator();

    @Test
    public void søknadUtenNoeSatt() {
        OmsorgspengerSøknad.Builder builder = OmsorgspengerSøknad.builder();
        OmsorgspengerSøknad søknad = OmsorgspengerSøknad.SerDes.deserialize("{\"versjon\":\"0.0.1\"}");
        List<Feil> builderFeil = verifyHarFeil(builder);
        List<Feil> jsonFeil = verifyHarFeil(søknad);
        assertThat(builderFeil).isEqualTo(jsonFeil);
    }

    @Test
    public void søknadMedFødselsdatoSattPåBarn() {
        OmsorgspengerSøknad.Builder builder = medSøker()
                .barn(new Barn().medFødselsdato(LocalDate.now()));
        verifyIngenFeil(builder);
    }

    @Test
    public void søknadMedIdentSattPåBarn() {
        OmsorgspengerSøknad.Builder builder = medSøker()
                .barn(new Barn().medNorskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                );
        verifyIngenFeil(builder);
    }

    @Test
    public void komplettSøknadFraJson() {
        OmsorgspengerSøknad søknad = OmsorgspengerSøknad.SerDes.deserialize(jsonForKomplettSøknad());
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
        assertThat(feil).isNotEmpty();
        return feil;
    }

    private List<Feil> verifyHarFeil(OmsorgspengerSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil).isNotEmpty();
        return feil;
    }

    private void verifyIngenFeil(OmsorgspengerSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil).isEmpty();
    }

    private void verifyIngenFeil(OmsorgspengerSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil).isEmpty();
    }

    private OmsorgspengerSøknad.Builder medSøker() {
        return OmsorgspengerSøknad
                .builder()
                .søknadId(SøknadId.of("123"))
                .mottattDato(ZonedDateTime.now())
                .søker(new Søker(NorskIdentitetsnummer.of("11111111111"))
                );
    }
}
