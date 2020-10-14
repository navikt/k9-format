package no.nav.k9.søknad.felles;

import java.util.Objects;

public class Feil {

    public final String felt;
    public final String feilkode;
    public final String feilmelding;

    public Feil(String felt, String feilkode, String feilmelding) {
        this.felt = felt;
        this.feilkode = feilkode;
        this.feilmelding = feilmelding;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Feil feil = (Feil) o;

        return Objects.equals(feilkode, feil.feilkode)
            && Objects.equals(felt, feil.felt)
            && Objects.equals(feilmelding, feil.feilmelding);
    }

    @Override
    public int hashCode() {
        return Objects.hash(felt, feilkode, feilmelding);
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
