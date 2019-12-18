package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class SoknadId {

    @JsonValue
    public final String id;

    @JsonCreator
    private SoknadId(String id) {
        this.id = id;
    }

    public static SoknadId of(String id) {
        return new SoknadId(id);
    }
}
