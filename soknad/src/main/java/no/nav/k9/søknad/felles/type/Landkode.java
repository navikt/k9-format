package no.nav.k9.søknad.felles.type;

import java.util.Objects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ISO 3166 alpha-3 landkode.
 *
 * @see https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3
 */
public class Landkode {

    public static final Landkode NORGE = new Landkode("NOR");
    public static final Landkode DANMARK = new Landkode("DNK");
    public static final Landkode SVERIGE = new Landkode("SWE");
    public static final Landkode FINLAND = new Landkode("FIN");
    public static final Landkode USA = new Landkode("USA");
    public static final Landkode CANADA = new Landkode("CAN");
    public static final Landkode SPANIA = new Landkode("ESP");

    @JsonValue
    @Size(max = 3)
    @NotNull
    @Pattern(regexp = "^[A-Z]+$", message = "'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
    private final String landkode;

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

    public String getLandkode() {
        return landkode;
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
