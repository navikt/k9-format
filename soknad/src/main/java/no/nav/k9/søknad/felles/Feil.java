package no.nav.k9.søknad.felles;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Feil {

    private final String felt;
    private final String feilkode;
    private final String feilmelding;

    @JsonCreator
    public Feil(@JsonProperty(value = "felt") String felt,
                @JsonProperty(value = "feilkode") String feilkode,
                @JsonProperty(value = "feilmelding") String feilmelding) {
        this.felt = felt;
        this.feilkode = feilkode;
        this.feilmelding = feilmelding;
    }

    public String getFeilkode() {
        return feilkode;
    }

    public String getFeilmelding() {
        return feilmelding;
    }

    public String getFelt() {
        return felt;
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
