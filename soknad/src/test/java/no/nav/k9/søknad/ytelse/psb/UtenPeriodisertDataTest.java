package no.nav.k9.søknad.ytelse.psb;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarnSøknadValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UtenPeriodisertDataTest {

    @Test
    public void søknad_uten_periodisert_data() {
        var validator = new PleiepengerSyktBarnSøknadValidator();
        var søknad = SøknadJsonEksempel.utenPeriodisertDataJson();
        var endringsPerioderFraK9Sak = List.of(new Periode("2021-01-01/2021-01-01"));
        // K9-Punsj validerer søknaden uten periodsert data med en gyldig endringsperiode hentet fra K9-Sak og får ingen valideringsfeil
        assertThat(validator.valider(søknad, endringsPerioderFraK9Sak)).isEmpty();
        // K9-Sak på sin side validerer uten å bruke gyldig endringsperiode
        assertThat(validator.valider(søknad)).hasSameElementsAs(List.of(
                new Feil("søknadsperiode/gyldigEndringsPerioder", "missingArgument", "Mangler søknadsperiode eller gyldigEndringsPerioder.")
        ));
    }
}
