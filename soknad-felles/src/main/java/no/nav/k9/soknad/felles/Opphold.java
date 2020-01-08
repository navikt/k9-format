package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Opphold implements Periodisert {
    public final Periode periode;

    public final Landkode land;

    @JsonCreator
    private Opphold(
            @JsonProperty("periode")
            Periode periode,
            @JsonProperty("land")
            Landkode land) {
        this.periode = periode;
        this.land = land;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    @JsonIgnore
    public Periode getPeriode() {
        return periode;
    }

    public static final class Builder {
        private Periode periode;
        private Landkode land;

        private Builder() {}

        public Builder land(Landkode land) {
            this.land = land;
            return this;
        }

        public Builder periode(Periode periode) {
            this.periode = periode;
            return this;
        }

        public Opphold build() {
            return new Opphold(
                    periode,
                    land
            );
        }
    }
}
