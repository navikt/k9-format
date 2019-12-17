package no.nav.k9.soknad.felles;

public class Nattevaak {
    private Boolean svar;
    private String tilleggsinformasjon;

    public void setSvar(Boolean svar) {
        this.svar = svar;
    }

    public Boolean getSvar() {
        return svar;
    }

    public void setTilleggsinformasjon(String tilleggsinformasjon) {
        this.tilleggsinformasjon = tilleggsinformasjon;
    }

    public String getTilleggsinformasjon() {
        return tilleggsinformasjon;
    }
}
