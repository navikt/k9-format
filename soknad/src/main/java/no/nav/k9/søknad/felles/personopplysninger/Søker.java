package no.nav.k9.søknad.felles.personopplysninger;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.PersonIdent;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Søker implements Person {

    @JsonAlias({ "fødselsnummer", "norskIdentifikator" })
    @JsonProperty(value = "norskIdentitetsnummer", required = true)
    @NotNull
    @Valid
    public final NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonCreator
    public Søker(@JsonProperty(value = "norskIdentitetsnummer", required = true) NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = Objects.requireNonNull(norskIdentitetsnummer, "norskIdentitetsnummer");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var other = (Søker) obj;

        return Objects.equals(norskIdentitetsnummer, other.norskIdentitetsnummer);
    }

    @Override
    public PersonIdent getPersonIdent() {
        return norskIdentitetsnummer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(norskIdentitetsnummer);
    }

    /**@deprecated brukt ctor.*/
    @Deprecated(forRemoval = true)
    public static Builder builder() {
        return new Builder();
    }

    /**@deprecated brukt ctor.*/
    @Deprecated(forRemoval = true)
    public static final class Builder {
        private NorskIdentitetsnummer norskIdentitetsnummer;

        private Builder() {
        }

        public Builder norskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
            this.norskIdentitetsnummer = norskIdentitetsnummer;
            return this;
        }

        public Søker build() {
            return new Søker(norskIdentitetsnummer);
        }
    }
}
