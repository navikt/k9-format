package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TilsynsordningSvar {
    JA("ja"),
    NEI("nei"),
    VET_IKKE("vetIkke");

    @JsonValue
    public final String dto;

    @JsonCreator
    TilsynsordningSvar(String dto) {
        this.dto = dto;
    }
}
