package no.nav.k9.søknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TilsynsordningSvar {
    JA("ja"),
    NEI("nei"),
    VET_IKKE("vetIkke");

    @JsonValue
    public final String kode;

    TilsynsordningSvar(String dto) {
        this.kode = dto;
    }
    
    public String getKode() {
        return kode;
    }

    @JsonCreator
    public static TilsynsordningSvar of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (TilsynsordningSvar svar : values()) {
            if (svar.kode.equals(value)) {
                return svar;
            }
        }
        throw new IllegalArgumentException("Ikke støttet svar '" + value + "'");
    }
}
