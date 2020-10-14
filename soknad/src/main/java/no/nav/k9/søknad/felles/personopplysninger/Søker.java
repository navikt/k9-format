package no.nav.k9.søknad.felles.personopplysninger;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Søker {

    @NotNull
    @Valid
    public final NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonCreator
    private Søker(
                  @JsonProperty("norskIdentitetsnummer") NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
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
    public int hashCode() {
        return Objects.hash(norskIdentitetsnummer);
    }

    public static Builder builder() {
        return new Builder();
    }

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
