package no.nav.k9.søknad.felles;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ISO 639-1 2 bokstav språk kode (sans landkode).
 * 
 * @see https://www.loc.gov/standards/iso639-2/php/code_list.php
 */
public enum Språk {

    NORSK_BOKMÅL("nb"),
    NORSK_NYNORSK("nn");

    public final String kode;

    Språk(String dto) {
        this.kode = dto;
    }
    
    @JsonValue
    public String getKode() {
        return kode;
    }

    public static Språk of(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (Språk språk : values()) {
            if (språk.kode.equals(value)) {
                return språk;
            }
        }
        throw new IllegalArgumentException("Ikke støttet språk '" + value + "'");
    }
}
