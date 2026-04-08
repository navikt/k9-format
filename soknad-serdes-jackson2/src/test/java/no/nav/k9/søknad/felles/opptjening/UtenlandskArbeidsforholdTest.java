package no.nav.k9.søknad.felles.opptjening;

import static no.nav.k9.søknad.TestUtils.feilInneholder;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.TestValidator;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Periode;

class UtenlandskArbeidsforholdTest {
    private static final TestValidator validator = new TestValidator();

    @Test
    public void ansettelsePeriodeInvers() {
        var periode = new Periode(LocalDate.now(), LocalDate.now().minusDays(10));
        var ua = new UtenlandskArbeidsforhold()
                .medAnsettelsePeriode(periode)
                .medLand(Landkode.FINLAND);

        var feil = validator.verifyHarFeil(ua);
        feilInneholder(feil, "ugyldigPeriode");
    }

    @Test
    public void ansettelsePeriodeFomTom() {
        var periode = new Periode(null, LocalDate.now().plusWeeks(10));
        var ua = new UtenlandskArbeidsforhold()
                .medAnsettelsePeriode(periode)
                .medLand(Landkode.FINLAND);

        var feil = validator.verifyHarFeil(ua);
        feilInneholder(feil, "ugyldigPeriode");
    }

    @Test
    public void ansettelsePeriodeUtenFeil() {
        var periode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(10));
        var ua = new UtenlandskArbeidsforhold()
                .medAnsettelsePeriode(periode)
                .medLand(Landkode.SPANIA);

        validator.verifyIngenFeil(ua);
    }

}
