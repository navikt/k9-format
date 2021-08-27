package no.nav.k9.søknad.felles.fravær;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SøknadÅrsak {
    ARBEIDSGIVER_KONKURS(
            "ARBEIDSGIVER_KONKURS",
            "Arbeidsgiver er konkurs"),

    NYOPPSTARTET_HOS_ARBEIDSGIVER(
            "NYOPPSTARTET_HOS_ARBEIDSGIVER",
            "Har startet hos ny arbeidsgiver"
    ),

    KONFLIKT_MED_ARBEIDSGIVER(
            "KONFLIKT_MED_ARBEIDSGIVER",
            "Konflikt med arbeidsgiver"
    );


    private String kode;
    private String årsak;


    SøknadÅrsak(String kode, String årsak) {
        this.kode = kode;
        this.årsak = årsak;
    }

    public String getÅrsak() {
        return årsak;
    }

    @JsonValue
    public String getKode() { return kode; }

    @JsonCreator
    public static SøknadÅrsak of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (SøknadÅrsak årsak : values()) {
            if (årsak.kode.equals(value)) {
                return årsak;
            }
        }
        throw new IllegalArgumentException("Ikke støttet årsak '" + value + "'");
    }
}
