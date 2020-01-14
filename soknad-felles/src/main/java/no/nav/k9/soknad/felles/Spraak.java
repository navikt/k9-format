package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Spraak {

    NORSK_BOKMAAL("nb"),
    NORSK_NYNORSK("nn");

    @JsonValue
    public final String dto;

    Spraak(String dto) {
        this.dto = dto;
    }

    @JsonCreator
    public static Spraak of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (Spraak språk : values()) {
            if (språk.dto.equals(value)) {
                return språk;
            }
        }
        throw new IllegalStateException("Ikke støttet språk '" + value + "'");
    }
}
