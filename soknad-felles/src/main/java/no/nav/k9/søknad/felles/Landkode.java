package no.nav.k9.søknad.felles;

import java.util.Objects;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ISO 3166 alpha-3 landkode.
 * 
 * @see https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3
 */
public class Landkode {

    @JsonValue
    @Size(max = 3)
    @Pattern(regexp = "^[A-Z]+$", message = "'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
    public final String landkode;

    private Landkode(String landkode) {
        this.landkode = landkode;
    }

    @JsonCreator
    public static Landkode of(String landkode) {
        if (landkode == null || landkode.isBlank()) {
            return null;
        }
        return new Landkode(landkode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        var landkode1 = (Landkode) o;
        return Objects.equals(landkode, landkode1.landkode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(landkode);
    }
}
