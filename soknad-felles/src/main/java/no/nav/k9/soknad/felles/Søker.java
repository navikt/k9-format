package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Søker {

    public final NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonCreator
    private Søker(
            @JsonProperty("norskIdentitetsnummer")
            NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private NorskIdentitetsnummer norskIdentitetsnummer;

        private Builder() {}

        public Builder norskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
            this.norskIdentitetsnummer = norskIdentitetsnummer;
            return this;
        }

        public Søker build() {
            return new Søker(norskIdentitetsnummer);
        }
    }
}
