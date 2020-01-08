
package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.JsonUtils;
import no.nav.k9.soknad.ValideringsFeil;
import no.nav.k9.soknad.felles.*;
import org.junit.Test;

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

    /*
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

        soknad.setSoker(Soker.builder().build());
        verifyHarFeil(soknad);

        soknad.setSoker(Soker.builder().norskIdentitetsnummer(new NorskIdentitetsnummer("11111111111")).build());
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


    private void verifyIngenFeil(PleiepengerBarnSoknad soknad) {
        final PleiepengerBarnSoknadValidator validator = new PleiepengerBarnSoknadValidator();
        final List<Feil> resultat = validator.valider(soknad);
        assertThat(resultat, is(Collections.emptyList()));
    }

    private void verifyHarFeil(PleiepengerBarnSoknad soknad) {
        final PleiepengerBarnSoknadValidator validator = new PleiepengerBarnSoknadValidator();
        final List<Feil> resultat = validator.valider(soknad);
        assertThat(resultat, is(not(Collections.emptyList())));
    }
}
*/
