package no.nav.k9.innsyn.sak;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FagsakYtelseType {

    PLEIEPENGER_SYKT_BARN("PSB", "Pleiepenger sykt barn"),
    PLEIEPENGER_NÆRSTÅENDE("PPN", "Pleiepenger livets sluttfase"),
    OMSORGSPENGER_KS("OMP_KS", "Ekstra omsorgsdager kronisk syk"),
    OMSORGSPENGER_MA("OMP_MA", "Ekstra omsorgsdager midlertidig alene"),
    OMSORGSPENGER_AO("OMP_AO", "Alene om omsorgen"),
    OMSORGSPENGER("OMP", "Omsorgspenger"),
    OPPLÆRINGSPENGER("OLP", "Opplæringspenger");



    @JsonValue
    private final String kode;
    private final String navn;


    FagsakYtelseType(String kode, String navn) {
        this.kode = kode;
        this.navn = navn;
    }


    public String getKode() {
        return kode;
    }
    public String getNavn() {
        return navn;
    }

    public static FagsakYtelseType fraKode(String kode) {
        for (var v : values()) {
            if (v.kode.equals(kode)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Ukjent FagsakYtelseType: " + kode);
    }
}


