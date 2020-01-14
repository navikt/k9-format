package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Soker {

    public final NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonCreator
    private Soker(
            @JsonProperty("norsk_identitetsnummer")
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

        public Soker build() {
            return new Soker(norskIdentitetsnummer);
        }
    }
}
