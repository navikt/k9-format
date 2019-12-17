package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Navn {

    @JsonProperty(required = true)
    private String fornavn = "";

    @JsonProperty(required = true)
    private String mellomnavn = "";

    @JsonProperty(required = true)
    private String etternavn = "";


    public Navn() {

    }

    public Navn(String fornavn, String mellomnavn, String etternavn) {
        setFornavn(fornavn);
        setMellomnavn(mellomnavn);
        setEtternavn(etternavn);
    }

    public void setFornavn(String fornavn) {
        if (fornavn == null) {
            throw new IllegalArgumentException("fornavn == null");
        }
        this.fornavn = fornavn;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setMellomnavn(String mellomnavn) {
        if (mellomnavn == null) {
            throw new IllegalArgumentException("mellomnavn == null");
        }
        this.mellomnavn = mellomnavn;
    }

    public String getMellomnavn() {
        return mellomnavn;
    }

    public void setEtternavn(String etternavn) {
        if (etternavn == null) {
            throw new IllegalArgumentException("etternavn == null");
        }
        this.etternavn = etternavn;
    }

    public String getEtternavn() {
        return etternavn;
    }
}
