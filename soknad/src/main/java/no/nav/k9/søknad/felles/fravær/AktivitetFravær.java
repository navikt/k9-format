package no.nav.k9.søknad.felles.fravær;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AktivitetFravær {
    FRILNSER(
            "FRILANSER",
            "Frilanser"),

    SELVSTENDIG_VIRKSOMHET(
            "SELVSTENDIG_VIRKSOMHET",
            "Selvstendig virksomhet"
    );

    private String kode;
    private String årsak;


    AktivitetFravær(String kode, String årsak) {
        this.kode = kode;
        this.årsak = årsak;
    }

    public String getÅrsak() {
        return årsak;
    }

    @JsonValue
    public String getKode() { return kode; }

    @JsonCreator
    public static AktivitetFravær of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (AktivitetFravær årsak : values()) {
            if (årsak.kode.equals(value)) {
                return årsak;
            }
        }
        throw new IllegalArgumentException("Ikke støttet årsak '" + value + "'");
    }
}
