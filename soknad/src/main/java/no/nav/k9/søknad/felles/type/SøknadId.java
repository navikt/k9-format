package no.nav.k9.søknad.felles.type;

import java.util.Objects;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class SøknadId {

    @JsonValue
    @Size(max=40)
    @Pattern(regexp = "^[\\p{Alnum}-]+$", message = "'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
    public final String id;

    private SøknadId(String id) {
        this.id = id;
    }

    @JsonCreator
    public static SøknadId of(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return new SøknadId(id);
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
