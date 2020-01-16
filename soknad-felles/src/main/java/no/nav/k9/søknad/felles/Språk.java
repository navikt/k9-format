package no.nav.k9.søknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Språk {

    NORSK_BOKMÅL("nb"),
    NORSK_NYNORSK("nn");

    @JsonValue
    public final String dto;

    Språk(String dto) {
        this.dto = dto;
    }

    @JsonCreator
    public static Språk of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (Språk språk : values()) {
            if (språk.dto.equals(value)) {
                return språk;
            }
        }
        throw new IllegalArgumentException("Ikke støttet språk '" + value + "'");
    }
}
