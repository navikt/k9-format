package no.nav.k9.søknad.felles.opptjening;

import no.nav.k9.søknad.TestValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class OpptjeningAktivitetTest {

    private static final TestValidator validator = new TestValidator();

    @Test
    public void frilanserKanHaÅpenPeriode() {

        var frilanser = new Frilanser().medStartdato(LocalDate.now());
        var opptjeningAktivitet = new OpptjeningAktivitet().medFrilanser(frilanser);


        validator.verifyIngenFeil(opptjeningAktivitet);
    }


}
