package no.nav.k9.søknad.ytelse.psb;

import static no.nav.k9.søknad.ytelse.psb.TestUtils.feilInneholder;
import static no.nav.k9.søknad.ytelse.psb.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.psb.ValiderUtil.verifyIngenFeil;

import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarnSøknadValidator;

public class UtenPeriodisertDataTest {

    @Test
    public void søknad_uten_periodisert_data() {
        var validator = new PleiepengerSyktBarnSøknadValidator();
        var søknad = SøknadJsonEksempel.utenPeriodisertDataJson();
        var endringsPerioderFraK9Sak = List.of(new Periode("2021-01-01/2021-01-01"));
        // K9-Punsj validerer søknaden uten periodsert data med en gyldig endringsperiode hentet fra K9-Sak og får ingen valideringsfeil
        verifyIngenFeil(søknad, endringsPerioderFraK9Sak);

        // K9-Sak på sin side validerer uten å bruke gyldig endringsperiode
        var feil = verifyHarFeil(søknad);
        feilInneholder(feil, new Feil("søknadsperiode/gyldigEndringsPerioder", "missingArgument", "Mangler søknadsperiode eller gyldigEndringsPerioder."));
    }
}
