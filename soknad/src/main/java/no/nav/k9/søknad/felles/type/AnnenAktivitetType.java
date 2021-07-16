package no.nav.k9.søknad.felles.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AnnenAktivitetType {

    MILITÆR_ELLER_SIVILTJENESTE("MILITÆR_ELLER_SIVILTJENESTE", "Militær eller siviltjeneste"),
    UDEFINERT("-", "Ikke definert"),
    ;

    private String navn;

    private String kode;

    AnnenAktivitetType(String kode, String navn) {
        this.kode = kode;
        this.navn = navn;
    }

    public String getNavn() {
        return navn;
    }

    @JsonValue
    public String getKode() {
        return kode;
    }

}
