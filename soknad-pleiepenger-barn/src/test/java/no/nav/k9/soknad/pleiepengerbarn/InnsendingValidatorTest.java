package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.felles.*;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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

    @Test
    public void oppholdslandSkalVaerePaakrevd() {
        final PleiepengerBarnSoknad soknad = TestUtils.komplettSoknad();
        soknad.getMedlemskap().setOpphold(new ArrayList<>());

        final Opphold opphold = new Opphold();
        opphold.setPeriode(new Periode(LocalDate.now(), null));
        soknad.getMedlemskap().setOpphold(Collections.singletonList(opphold));
        verifyHarFeil(soknad);

        opphold.setLand(Landkode.from("NOR"));
        verifyIngenFeil(soknad);
    }

    @Test
    public void oppholdsperiodeSkalVaerePaakrevd() {
        final PleiepengerBarnSoknad soknad = TestUtils.komplettSoknad();
        soknad.getMedlemskap().setOpphold(new ArrayList<>());

        final Opphold opphold = new Opphold();
        opphold.setLand(Landkode.from("NOR"));
        soknad.getMedlemskap().setOpphold(Collections.singletonList(opphold));
        verifyHarFeil(soknad);

        opphold.setPeriode(new Periode(LocalDate.now(), null));
        verifyIngenFeil(soknad);
    }

    @Test
    public void oppholdsperiodeSkalHaFomOgEllerTom() {
        final PleiepengerBarnSoknad soknad = TestUtils.komplettSoknad();
        soknad.getMedlemskap().setOpphold(new ArrayList<>());

        final Opphold opphold = new Opphold();
        opphold.setLand(Landkode.from("NOR"));
        opphold.setPeriode(new Periode(null, null));
        soknad.getMedlemskap().setOpphold(Collections.singletonList(opphold));

        opphold.setPeriode(new Periode(LocalDate.now(), null));
        verifyIngenFeil(soknad);

        opphold.setPeriode(new Periode(null, LocalDate.now()));
        verifyIngenFeil(soknad);

        opphold.setPeriode(new Periode(null, null));
        verifyHarFeil(soknad);
    }

    @Test
    public void oppholdsperiodeSkalIkkeOverlappe() {
        final PleiepengerBarnSoknad soknad = TestUtils.komplettSoknad();
        soknad.getMedlemskap().setOpphold(new ArrayList<>());

        final LocalDate now = LocalDate.now();

        final Opphold opphold1 = new Opphold();
        opphold1.setLand(Landkode.from("NOR"));
        opphold1.setPeriode(new Periode(now, null));

        final Opphold opphold2 = new Opphold();
        opphold2.setLand(Landkode.from("SWE"));
        opphold2.setPeriode(new Periode(null, now.minusDays(1)));

        soknad.getMedlemskap().setOpphold(Arrays.asList(opphold1, opphold2));

        verifyIngenFeil(soknad);

        opphold1.setPeriode(new Periode(now, null));
        opphold2.setPeriode(new Periode(null, now));
        verifyHarFeil(soknad);

        opphold1.setPeriode(new Periode(null, now));
        opphold2.setPeriode(new Periode(now, null));
        verifyHarFeil(soknad);

        opphold1.setPeriode(new Periode(now, now.plusDays(10)));
        opphold2.setPeriode(new Periode(null, now.minusDays(1)));
        verifyIngenFeil(soknad);

        opphold1.setPeriode(new Periode(now, now.plusDays(10)));
        opphold2.setPeriode(new Periode(null, now.minusDays(1)));
        verifyIngenFeil(soknad);

        opphold1.setPeriode(new Periode(null, now.minusDays(1)));
        opphold2.setPeriode(new Periode(now, now.plusDays(10)));
        verifyIngenFeil(soknad);

        opphold1.setPeriode(new Periode(now.minusDays(10), now.minusDays(1)));
        opphold2.setPeriode(new Periode(now, now.plusDays(10)));
        verifyIngenFeil(soknad);

        opphold1.setPeriode(new Periode(now.minusDays(10), now.plusDays(1)));
        opphold2.setPeriode(new Periode(now, now.plusDays(1)));
        verifyHarFeil(soknad);
    }


    private void verifyIngenFeil(PleiepengerBarnSoknad soknad) {
        final InnsendingValidator validator = new InnsendingValidator();
        final List<Feil> resultat = validator.validate(soknad);
        assertThat(resultat, is(Collections.emptyList()));
    }

    private void verifyHarFeil(PleiepengerBarnSoknad soknad) {
        final InnsendingValidator validator = new InnsendingValidator();
        final List<Feil> resultat = validator.validate(soknad);
        assertThat(resultat, is(not(Collections.emptyList())));
    }
}
