package no.nav.k9.søknad.ytelse.ung;

import no.nav.k9.søknad.ytelse.ung.v1.Ungdomsytelse;

import java.time.LocalDate;

public class YtelseEksempel {

    public static Ungdomsytelse komplettYtelseMedSøknadsperiode(LocalDate fraOgMed) {

        return new Ungdomsytelse()
                .medStartdato(fraOgMed);
    }
}
