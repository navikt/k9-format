package no.nav.k9.søknad.felles.opptjening;

import static no.nav.k9.søknad.TestUtils.feilInneholder;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.TestValidator;

class OpptjeningAktivitetTest {

    private static final TestValidator validator = new TestValidator();

    @Test
    public void frilanserKanHaÅpenPeriode() {
        var frilanser = new Frilanser().medStartdato(LocalDate.now());
        var opptjeningAktivitet = new OpptjeningAktivitet().medFrilanser(frilanser);

        validator.verifyIngenFeil(opptjeningAktivitet);
    }

    @Test
    public void frilanserKanIkkeHaSluttdatoFørStartdato() {
        var frilanser = new Frilanser()
                .medStartdato(LocalDate.of(2022, 10, 10))
                .medSluttdato(LocalDate.of(2022, 10, 1));

        var feil = validator.verifyHarFeil(new OpptjeningAktivitet().medFrilanser(frilanser));

        feilInneholder(feil, "frilanser.sluttdatoFørStartdato", "ugyldigPeriode", "Sluttdato kan ikke være før startdato.");
    }

    @Test
    public void frilanserKanHaLikStartOgSluttdato() {
        var dato = LocalDate.of(2022, 10, 10);
        var frilanser = new Frilanser().medStartdato(dato).medSluttdato(dato);

        validator.verifyIngenFeil(new OpptjeningAktivitet().medFrilanser(frilanser));
    }
}
