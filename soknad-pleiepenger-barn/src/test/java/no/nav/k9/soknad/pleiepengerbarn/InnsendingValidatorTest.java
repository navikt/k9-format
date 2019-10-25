package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.felles.Feil;
import no.nav.k9.soknad.felles.NorskIdentitetsnummer;
import no.nav.k9.soknad.felles.Periode;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InnsendingValidatorTest {

    @Test
    public void tomSoknadSkalFeile() {
        final PleiepengerBarnSoknad soknad = new PleiepengerBarnSoknad();
        verifyHarFeil(soknad);
    }

    @Test
    public void komplettSoknadSkalIkkeHaValideringsfeil() {
        final PleiepengerBarnSoknad soknad = TestUtils.komplettSoknad();
        verifyIngenFeil(soknad);
    }

    @Test
    public void mottattDatoErPaakrevd() {
        final PleiepengerBarnSoknad soknad = TestUtils.komplettSoknad();

        soknad.setMottattDato(null);
        verifyHarFeil(soknad);

        soknad.setMottattDato(ZonedDateTime.now());
        verifyIngenFeil(soknad);
    }

    @Test
    public void sokerNorskIdentitetsnummerPaakrevd() {
        final PleiepengerBarnSoknad soknad = TestUtils.komplettSoknad();

        soknad.getSoker().setNorskIdentitetsnummer(null);
        verifyHarFeil(soknad);

        soknad.getSoker().setNorskIdentitetsnummer(new NorskIdentitetsnummer("11111111111"));
        verifyIngenFeil(soknad);
    }

    @Test
    public void soknadsperiodeErPaakrevd() {
        final PleiepengerBarnSoknad soknad = TestUtils.komplettSoknad();

        soknad.setPeriode(new Periode());
        verifyHarFeil(soknad);

        soknad.setPeriode(new Periode(LocalDate.now().minusDays(10), LocalDate.now()));
        verifyIngenFeil(soknad);
    }

    @Test
    public void soknadsperiodeTomDatoSkalVaereEtterFomDato() {
        final PleiepengerBarnSoknad soknad = TestUtils.komplettSoknad();

        soknad.setPeriode(new Periode(LocalDate.now(), LocalDate.now().plusDays(1)));
        verifyIngenFeil(soknad);

        soknad.setPeriode(new Periode(LocalDate.now(), LocalDate.now()));
        verifyIngenFeil(soknad);

        soknad.setPeriode(new Periode(LocalDate.now().plusDays(1), LocalDate.now()));
        verifyHarFeil(soknad);
    }


    private void verifyIngenFeil(PleiepengerBarnSoknad soknad) {
        final InnsendingValidator validator = new InnsendingValidator();
        final List<Feil> resultat = validator.validate(soknad);
        assertThat(resultat.isEmpty(), is(true));
    }

    private void verifyHarFeil(PleiepengerBarnSoknad soknad) {
        final InnsendingValidator validator = new InnsendingValidator();
        final List<Feil> resultat = validator.validate(soknad);
        assertThat(resultat.isEmpty(), is(false));
    }
}
