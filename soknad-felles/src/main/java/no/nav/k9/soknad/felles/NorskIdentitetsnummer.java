package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public class NorskIdentitetsnummer {

    @JsonValue
    public final String verdi;

    private NorskIdentitetsnummer(String verdi) {
        this.verdi = verdi;
    }

    @JsonCreator
    public static NorskIdentitetsnummer of(String verdi) {
        if (verdi == null || verdi.isBlank()) {
            return null;
        }
        return new NorskIdentitetsnummer(verdi);
    }

    public static boolean erNull(NorskIdentitetsnummer norskIdentitetsnummer) {
        return norskIdentitetsnummer == null || norskIdentitetsnummer.verdi == null || norskIdentitetsnummer.verdi.isBlank();
    }

    @Override
    public String toString() {
        return verdi;
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
