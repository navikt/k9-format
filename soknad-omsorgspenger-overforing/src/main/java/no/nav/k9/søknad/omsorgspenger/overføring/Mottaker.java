package no.nav.k9.søknad.omsorgspenger.overføring;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.NorskIdentitetsnummer;

public class Mottaker {
    public final NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonCreator
    private Mottaker(
            @JsonProperty("norskIdentitetsnummer")
            NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
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
