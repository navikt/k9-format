package no.nav.k9.søknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Bosted {

    public final Landkode land;

    @JsonCreator
    private Bosted(
            @JsonProperty("land")
            Landkode land) {
        this.land = land;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Landkode land;

        private Builder() {}

        public Builder land(Landkode land) {
            this.land = land;
            return this;
        }

        public Bosted build() {
            return new Bosted(
                    land
            );
        }
    }
}
