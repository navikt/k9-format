package no.nav.k9.søknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class SøknadId {

    @JsonValue
    public final String id;

    private SøknadId(String id) {
        this.id = id;
    }

    @JsonCreator
    public static SøknadId of(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return new SøknadId(id);
    }
}
