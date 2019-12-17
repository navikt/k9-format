package no.nav.k9.soknad.felles;

public class Feil {

    private String felt;
    private String feilkode;
    private String feilmelding;


    public Feil(String felt, String feilkode, String feilmelding) {
        this.felt = felt;
        this.feilkode = feilkode;
        this.feilmelding = feilmelding;
    }
    

    public String getFelt() {
        return felt;
    }

    public String getFeilkode() {
        return feilkode;
    }

    public String getFeilmelding() {
        return feilmelding;
    }

    @Override
    public String toString() {
        return "Feil{" +
                "felt='" + felt + '\'' +
                ", feilkode='" + feilkode + '\'' +
                ", feilmelding='" + feilmelding + '\'' +
                '}';
    }
}
