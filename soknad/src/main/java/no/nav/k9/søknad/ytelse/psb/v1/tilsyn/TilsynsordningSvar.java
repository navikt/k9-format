package no.nav.k9.søknad.ytelse.psb.v1.tilsyn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TilsynsordningSvar {
    JA("ja"),
    NEI("nei"),
    VET_IKKE("vetIkke");

    @JsonValue
    private String dto;

    TilsynsordningSvar(String dto) {
        this.dto = dto;
    }

    public String getKode() {
        return dto;
    }

    @JsonCreator
    public static TilsynsordningSvar of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (TilsynsordningSvar svar : values()) {
            if (svar.dto.equals(value)) {
                return svar;
            }
        }
        throw new IllegalArgumentException("Ikke støttet svar '" + value + "'");
    }

    public String getDto() {
        return dto;
    }

    public void setDto(String dto) {
        this.dto = dto;
    }
}
