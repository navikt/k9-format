package no.nav.k9.søknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class Bosteder {
    public final Map<Periode, BostedPeriodeInfo> perioder;

    @JsonCreator
    private Bosteder(
            @JsonProperty("perioder")
            Map<Periode, BostedPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, BostedPeriodeInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, BostedPeriodeInfo> perioder) {
            this.perioder.putAll(perioder);
            return this;
        }

        public Builder periode(Periode periode, BostedPeriodeInfo bostedPeriodeInfo) {
            this.perioder.put(periode, bostedPeriodeInfo);
            return this;
        }

        public Bosteder build() {
            return new Bosteder(
                    perioder
            );
        }
    }


    public static class BostedPeriodeInfo {
        public final Landkode land;

        @JsonCreator
        private BostedPeriodeInfo(
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

            public BostedPeriodeInfo build() {
                return new BostedPeriodeInfo(
                        land
                );
            }
        }
    }
}
