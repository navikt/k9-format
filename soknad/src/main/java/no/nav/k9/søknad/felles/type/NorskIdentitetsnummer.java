package no.nav.k9.søknad.felles.type;

import java.util.Objects;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class NorskIdentitetsnummer implements PersonIdent {

    @JsonValue
    @Size(max=11)
    @Pattern(regexp = "^\\d+$", message = "'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
    private final String verdi;

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
    
    @Override
    public String getVerdi() {
        return verdi;
    }

    @Override
    public String toString() {
        return verdi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (NorskIdentitetsnummer) o;
        return Objects.equals(verdi, that.verdi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(verdi);
    }
}
