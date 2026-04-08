package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.TestUtils.feilInneholder;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.TestValidator;

class OmsorgTest {

    private static final TestValidator validator = new TestValidator();

    @Test
    public void relasjonMåFinnesNårRelasjonErAnnet() {
        var omsorg = new Omsorg().medRelasjonTilBarnet(Omsorg.BarnRelasjon.ANNET);

        var feil = validator.verifyHarFeil(omsorg);
        feilInneholder(feil, "påkrevd");
    }

    @Test
    public void relasjonKanVæreTomNårRelasjonIkkeErAnnet() {
        var omsorg = new Omsorg().medRelasjonTilBarnet(Omsorg.BarnRelasjon.MEDMOR);
        validator.verifyIngenFeil(omsorg);
    }

    @Test
    public void relasjonErAnnetUtenFeil() {
        var omsorg = new Omsorg()
                .medRelasjonTilBarnet(Omsorg.BarnRelasjon.ANNET)
                .medBeskrivelseAvOmsorgsrollen(TestUtils.testTekst());
        validator.verifyIngenFeil(omsorg);
    }

    @Test
    public void omsorgKanInneholdeNullPåRelasjonOgBeskrivelse() {
        var omorg = new Omsorg()
                .medBeskrivelseAvOmsorgsrollen(null)
                .medRelasjonTilBarnet(null);
        validator.verifyIngenFeil(omorg);
    }



}