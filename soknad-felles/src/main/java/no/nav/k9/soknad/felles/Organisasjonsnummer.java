package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Organisasjonsnummer {
    @JsonValue
    public final String verdi;

    private Organisasjonsnummer(String verdi) {
        this.verdi = verdi;
    }

    @JsonCreator
    public static Organisasjonsnummer of(String verdi) {
        if (verdi == null || verdi.isBlank()) {
            return null;
        }
        return new Organisasjonsnummer(verdi);
    }

    public static boolean erNull(Organisasjonsnummer organisasjonsnummer) {
        return organisasjonsnummer == null || organisasjonsnummer.verdi == null || organisasjonsnummer.verdi.isBlank();
    }

}
