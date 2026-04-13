package no.nav.k9.søknad.felles.opptjening;

import static no.nav.k9.søknad.TestUtils.feilInneholder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.TestValidator;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.VirksomhetType;

class SelvstendigNæringsdrvendeTest {

    private static final TestValidator validator = new TestValidator();

    @Disabled
    @Test
    public void testLandkodeKanIkkeVæreNor() {
        var periode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var snInfo = new SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo()
                .medVirksomhetstyper(List.of(VirksomhetType.DAGMAMMA))
                .medLandkode(Landkode.NORGE);
        var sn = new SelvstendigNæringsdrivende().medPerioder(Map.of(periode, snInfo));

        var feil = validator.verifyHarFeil(sn);
        feilInneholder(feil, "ugyldigVerdi");
    }

    @Test
    public void testLandkodeKanVæreDanmark() {
        var periode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var snInfo = new SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo()
                .medVirksomhetstyper(List.of(VirksomhetType.DAGMAMMA))
                .medLandkode(Landkode.DANMARK);
        var sn = new SelvstendigNæringsdrivende().medPerioder(Map.of(periode, snInfo));
        validator.verifyIngenFeil(sn);
    }

    @Test
    public void testRegnskapsførerTlfKanIkkeVæreTom() {
        var periode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var snInfo = new SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo()
                .medVirksomhetstyper(List.of(VirksomhetType.DAGMAMMA))
                .medRegnskapsførerTlf("");
        var sn = new SelvstendigNæringsdrivende().medPerioder(Map.of(periode, snInfo));
        var feil = validator.verifyHarFeil(sn);
        feilInneholder(feil, "påkrevd");
    }

    @Test
    public void testRegnskapsførerTlfUtenFeil() {
        var periode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var snInfo = new SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo()
                .medVirksomhetstyper(List.of(VirksomhetType.DAGMAMMA))
                .medRegnskapsførerTlf("111111111");
        var sn = new SelvstendigNæringsdrivende().medPerioder(Map.of(periode, snInfo));
        validator.verifyIngenFeil(sn);
    }

    @Test
    public void testRegnskapsførerNavnKanIkkeVæreTom() {
        var periode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var snInfo = new SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo()
                .medVirksomhetstyper(List.of(VirksomhetType.DAGMAMMA))
                .medRegnskapsførerNavn("");
        var sn = new SelvstendigNæringsdrivende().medPerioder(Map.of(periode, snInfo));
        var feil = validator.verifyHarFeil(sn);
        feilInneholder(feil, "påkrevd");
    }

    @Test
    public void testRegnskapsførerNavnUtenFeil() {
        var periode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var snInfo = new SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo()
                .medVirksomhetstyper(List.of(VirksomhetType.DAGMAMMA))
                .medRegnskapsførerNavn("Ola Nordmann");
        var sn = new SelvstendigNæringsdrivende().medPerioder(Map.of(periode, snInfo));
        validator.verifyIngenFeil(sn);
    }

    @Test
    public void selvstendigNæringsdrivendeKanHaÅpnePerioder() {
        var snInfo = new SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo()
                .medVirksomhetstyper(List.of(VirksomhetType.ANNEN));

        var sn = new SelvstendigNæringsdrivende()
                .medPerioder(Map.of(Periode.parse("2020-01-01/.."), snInfo))
                .medVirksomhetNavn("testUlf");

        validator.verifyIngenFeil(sn);
    }

    @Test
    public void selvstendigNæringsdrivendeKanHaOverlappendePerioder() {
        var snInfo = new SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo()
                .medVirksomhetstyper(List.of(VirksomhetType.ANNEN));
        var sn = new SelvstendigNæringsdrivende()
                .medPerioder(Map.of(
                        Periode.parse("2020-01-01/2020-02-02"), snInfo,
                        Periode.parse("2020-01-05/2020-02-01"), snInfo))
                .medVirksomhetNavn("testUlf");
        var opptjeningAktivitet = new OpptjeningAktivitet().medSelvstendigNæringsdrivende(sn);

        validator.verifyIngenFeil(opptjeningAktivitet);
    }

    @Disabled("Kan aktivere dette igjen når punsj spør om SN er fisker på blad B.")
    @Test
    public void erFiskerPåBladBKanIkkeVæreNullDersomVirksomhetstypeErFiske() {
        var periode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(2));
        var snInfo = new SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo()
                .medVirksomhetstyper(List.of(VirksomhetType.FISKE));
        var sn = new SelvstendigNæringsdrivende().medPerioder(Map.of(periode, snInfo));
        var feil = validator.verifyHarFeil(sn);
        feilInneholder(feil, "påkrevd");
    }
}

