package no.nav.k9.søknad.felles.fravær;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FraværÅrsak {
    STENGT_SKOLE_ELLER_BARNEHAGE(
            "STENGT_SKOLE_ELLER_BARNEHAGE",
            "Stengt skole eller barnehage"),

    SMITTEVERNHENSYN(
            "SMITTEVERNHENSYN",
            "Smitteverhensyn"
    ),

    ORDINÆRT_FRAVÆR(
            "ORDINÆRT_FRAVÆR",
            "Ordinært fravær"
    );


    private String kode;
    private String årsak;


    FraværÅrsak(String kode, String årsak) {
        this.kode = kode;
        this.årsak = årsak;
    }

    public String getÅrsak() {
        return årsak;
    }

    @JsonValue
    public String getKode() { return kode; }

    @JsonCreator
    public static FraværÅrsak of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (FraværÅrsak årsak : values()) {
            if (årsak.kode.equals(value)) {
                return årsak;
            }
        }
        throw new IllegalArgumentException("Ikke støttet årsak '" + value + "'");
    }
}
