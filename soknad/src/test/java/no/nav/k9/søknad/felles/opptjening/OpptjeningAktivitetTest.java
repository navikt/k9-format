package no.nav.k9.søknad.felles.opptjening;

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


}
