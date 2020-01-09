package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Opphold {

    public final Landkode land;

    @JsonCreator
    private Opphold(
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

        public Opphold build() {
            return new Opphold(
                    land
            );
        }
    }
}
