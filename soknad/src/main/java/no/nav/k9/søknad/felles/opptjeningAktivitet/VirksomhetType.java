package no.nav.k9.søknad.felles.opptjeningAktivitet;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VirksomhetType {

    DAGMAMMA("DAGMAMMA", "Dagmamma i eget hjem/familiebarnehage"),
    FISKE("FISKE", "Fiske"),
    JORDBRUK_SKOGBRUK("JORDBRUK_SKOGBRUK", "Jordbruk"),
    ANNEN("ANNEN", "Annen næringsvirksomhet"),
    UDEFINERT("-", "Ikke definert"),
    ;

    private String navn;

    private String kode;

    VirksomhetType(String kode, String navn) {
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
