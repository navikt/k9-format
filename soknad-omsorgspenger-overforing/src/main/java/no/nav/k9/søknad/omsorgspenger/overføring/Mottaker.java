package no.nav.k9.søknad.omsorgspenger.overføring;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.PersonIdent;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Mottaker {
    
    @JsonProperty(value="norskIdentitetsnummer", required = true)
    public final NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonCreator
    private Mottaker(@JsonProperty(value = "norskIdentitetsnummer", required=true) NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = Objects.requireNonNull(norskIdentitetsnummer, "norskIdentitetsnummer");
    }

    public PersonIdent getPersonIdent() {
        return norskIdentitetsnummer;
    }
    
    public static Mottaker.Builder builder() {
        return new Mottaker.Builder();
    }

    public static final class Builder {
        private NorskIdentitetsnummer norskIdentitetsnummer;

        private Builder() {}

        public Mottaker.Builder norskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
            this.norskIdentitetsnummer = norskIdentitetsnummer;
            return this;
        }

        public Mottaker build() {
            return new Mottaker(norskIdentitetsnummer);
        }
    }
}
