package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Periode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class Beredskap {
    public final Map<Periode, BeredskapInfo> perioder;

    @JsonCreator
    private Beredskap(
            @JsonProperty("perioder")
            Map<Periode, BeredskapInfo> perioder) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public static final class Builder {
        private Map<Periode, BeredskapInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, BeredskapInfo> perioder) {
            this.perioder.putAll(perioder);
            return this;
        }

        public Builder periode(Periode periode, BeredskapInfo beredskapInfo) {
            this.perioder.put(periode, beredskapInfo);
            return this;
        }

        public Beredskap build() {
            return new Beredskap(
                    perioder
            );
        }
    }

    public static final class BeredskapInfo {
        public final String tilleggsinformasjon;

        @JsonCreator
        private BeredskapInfo(
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

            public BeredskapInfo build() {
                return new BeredskapInfo(
                        tilleggsinformasjon
                );
            }
        }
    }
}
