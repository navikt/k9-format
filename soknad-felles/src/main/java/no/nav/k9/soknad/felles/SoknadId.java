package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class SoknadId {

    @JsonValue
    public final String id;

    private SoknadId(String id) {
        this.id = id;
    }

    @JsonCreator
    public static SoknadId of(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return new SoknadId(id);
    }

    public static boolean erNull(SoknadId soknadId) {
        return soknadId == null || soknadId.id == null || soknadId.id.isBlank();
    }
}
