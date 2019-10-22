package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public class NorskIdentitetsnummer {

    @JsonValue
    private final String verdi;

    @JsonCreator
    public NorskIdentitetsnummer(String verdi) {
        if (verdi == null) {
            throw new IllegalArgumentException("verdi == null");
        }
        this.verdi = verdi;
    }

    public String getVerdi() {
        return verdi;
    }

    @Override
    public String toString() {
        return getVerdi();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NorskIdentitetsnummer that = (NorskIdentitetsnummer) o;
        return Objects.equals(verdi, that.verdi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(verdi);
    }
}
