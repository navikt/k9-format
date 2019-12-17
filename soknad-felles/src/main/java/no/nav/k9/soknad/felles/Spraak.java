package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Spraak {

    NORSK_BOKMAAL("nb"),
    NORSK_NYNORSK("nn");

    @JsonValue
    public final String dto;

    @JsonCreator
    Spraak(String dto) {
        this.dto = dto;
    }
}
