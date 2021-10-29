package no.nav.k9.søknad.felles;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;

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

    public static <O> Feil toFeil(ConstraintViolation<O> constraintViolation) {
        var constraintMessage = constraintViolation.getMessage();

        final Pattern feilkodePattern = Pattern.compile("^[^\\[]*\\[([^]]*)\\](.*)$");
        final Matcher feilkodeMatcher = feilkodePattern.matcher(constraintMessage);

        final String feilkode;
        final String feilmelding;

        if (feilkodeMatcher.matches() && feilkodeMatcher.groupCount() >= 2) {
            feilkode = feilkodeMatcher.group(1).trim();
            feilmelding = feilkodeMatcher.group(2).trim();
        } else if (constraintMessage.equals("must not be null")){
            feilkode = "nullFeil";
            feilmelding = "Feltet kan ikke være tomt, null feil";

        }else if (constraintMessage.equals("must not be empty")){
            feilkode = "tomFeil";
            feilmelding = "Feltet kan ikke være tomt";

        } else {
            feilkode = "påkrevd";
            feilmelding = constraintMessage;
        }

        return new Feil(
                constraintViolation.getPropertyPath().toString(),
                feilkode,
                feilmelding);
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
