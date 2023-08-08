package no.nav.k9.søknad.felles.type;

import java.util.Objects;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class SøknadId {

    @JsonValue
    @Size(max=40)
    @Pattern(regexp = "^[\\p{Alnum}-]+$", message = "'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
    private final String id;

    public SøknadId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Kan ikke ha null eller blank id");
        }
        this.id = id;
    }

    @JsonCreator
    public static SøknadId of(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return new SøknadId(id);
    }
    
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var other = (SøknadId) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"<id="+id+">";
    }
}
