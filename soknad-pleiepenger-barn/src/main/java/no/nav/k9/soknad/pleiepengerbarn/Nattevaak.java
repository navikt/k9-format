package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Nattevaak {
    public final String tilleggsinformasjon;

    @JsonCreator
    private Nattevaak(
            @JsonProperty("tilleggsinformasjon")
            String tilleggsinformasjon) {
        this.tilleggsinformasjon = tilleggsinformasjon;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String tilleggsinformasjon;

        private Builder() { }

        public Builder tilleggsinformasjon(String tilleggsinformasjon) {
            this.tilleggsinformasjon = tilleggsinformasjon;
            return this;
        }

        public Nattevaak build() {
            return new Nattevaak(
                    tilleggsinformasjon
            );
        }
    }
}
