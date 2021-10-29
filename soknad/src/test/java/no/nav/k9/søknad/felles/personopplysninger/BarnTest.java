package no.nav.k9.søknad.felles.personopplysninger;

import static no.nav.k9.søknad.TestUtils.feilInneholder;
import static no.nav.k9.søknad.TestUtils.okNorskIdentitetsnummer;
import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyIngenFeil;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.TestValidator;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.SøknadEksempel;
import no.nav.k9.søknad.ytelse.psb.YtelseEksempel;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;

class BarnTest {
    private static final TestValidator validator = new TestValidator();

    @Test
    public void barnKanIkkeVæreFødtIFremtiden() {
        var barn = new Barn()
                .medFødselsdato(LocalDate.now().plusMonths(2));

        var feil = validator.verifyHarFeil(barn);
        feilInneholder(feil, "fødselsdato", "ugyldigFødselsdato" , "Fødselsdato kan ikke være fremtidig");
    }

    @Test
    public void barnKanVæreFødtIFortiden() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(3));
        var søknad = SøknadEksempel.søknad(YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid(søknadsperiode));

        ((PleiepengerSyktBarn) søknad.getYtelse()).medBarn(new Barn().medFødselsdato(LocalDate.now().minusMonths(2)));
        verifyIngenFeil(søknad);
    }

    @Test
    public void barnKanVæreFødtIDag() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(3));
        var søknad = SøknadEksempel.søknad(YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid(søknadsperiode));

        ((PleiepengerSyktBarn) søknad.getYtelse()).medBarn(new Barn().medFødselsdato(LocalDate.now()));
        verifyIngenFeil(søknad);
    }

    @Test
    public void barnKanMangleFødselsdato() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(3));
        var søknad = SøknadEksempel.søknad(YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid(søknadsperiode));

        ((PleiepengerSyktBarn) søknad.getYtelse()).medBarn(new Barn()
                .medNorskIdentitetsnummer(okNorskIdentitetsnummer()));
        verifyIngenFeil(søknad);
    }

}