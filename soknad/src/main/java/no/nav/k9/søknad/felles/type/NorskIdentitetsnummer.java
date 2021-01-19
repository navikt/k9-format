package no.nav.k9.søknad.felles.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public class NorskIdentitetsnummer implements PersonIdent {

    @JsonValue
    @Size(max=11)
    @Pattern(regexp = "^\\d+$", message = "'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
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
