package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Organisasjonsnummer {
    @JsonValue
    public final String verdi;

    @JsonCreator
    private Organisasjonsnummer(String verdi) {
        this.verdi = verdi;
    }

}
