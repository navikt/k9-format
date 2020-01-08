package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

public class Versjon {
    @JsonIgnore
    private static final String SEMVER_REGEX = "(\\d+)\\.(\\d+)\\.(\\d+)";

    @JsonValue
    public final String verdi;

    @JsonCreator
    private Versjon(String verdi) {
        this.verdi = verdi;
    }

    public static Versjon of(String verdi) {
        return new Versjon(verdi);
    }

    public static boolean erNull(Versjon versjon) {
        return versjon == null || versjon.verdi == null || versjon.verdi.isBlank();
    }

    @JsonIgnore
    public boolean erGyldig() {
        return verdi.matches(SEMVER_REGEX);
    }

}
