package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

public class Versjon {
    @JsonIgnore
    private static final String SEMVER_REGEX = "(\\d+)\\.(\\d+)\\.(\\d+)";

    @JsonValue
    public final String verdi;

    private Versjon(String verdi) {
        this.verdi = verdi;
    }

    @JsonCreator
    public static Versjon of(String verdi) {
        if (verdi == null || verdi.isBlank()) {
            return null;
        }
        return new Versjon(verdi);
    }

    @JsonIgnore
    public boolean erGyldig() {
        return verdi.matches(SEMVER_REGEX);
    }
}
