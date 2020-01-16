package no.nav.k9.søknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public class Landkode {

    @JsonValue
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Landkode landkode1 = (Landkode) o;
        return Objects.equals(landkode, landkode1.landkode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(landkode);
    }
}
