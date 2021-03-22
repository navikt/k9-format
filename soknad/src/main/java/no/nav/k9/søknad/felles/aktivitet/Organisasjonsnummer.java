package no.nav.k9.søknad.felles.aktivitet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Organisasjonsnummer {

    @JsonValue
    @NotNull
    @Size(max = 20)
    @Pattern(regexp = "^\\d+$", message = "'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
    private final String verdi;

    private Organisasjonsnummer(String verdi) {
        this.verdi = Objects.requireNonNull(verdi, "organisasjonsnummer");
    }

    public String getVerdi() {
        return verdi;
    }
    
    @JsonCreator
    public static Organisasjonsnummer of(String verdi) {
        if (verdi == null || verdi.isBlank()) {
            return null;
        }
        return new Organisasjonsnummer(verdi);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var other = (Organisasjonsnummer) obj;
        return Objects.equals(verdi, other.verdi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(verdi);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"<"+verdi+">";
    }
}
