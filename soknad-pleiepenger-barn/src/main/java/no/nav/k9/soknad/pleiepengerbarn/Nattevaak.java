package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Periode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class Nattevaak {
    public final Map<Periode, NattevaakInfo> perioder;

    @JsonCreator
    private Nattevaak(
            @JsonProperty("perioder")
            Map<Periode, NattevaakInfo> perioder) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, NattevaakInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, NattevaakInfo> perioder) {
            this.perioder.putAll(perioder);
            return this;
        }

        public Builder periode(Periode periode, NattevaakInfo nattevaakInfo) {
            this.perioder.put(periode, nattevaakInfo);
            return this;
        }

        public Nattevaak build() {
            return new Nattevaak(
                    perioder
            );
        }
    }

    public static final class NattevaakInfo {
        public final String tilleggsinformasjon;

        @JsonCreator
        private NattevaakInfo(
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

            public NattevaakInfo build() {
                return new NattevaakInfo(
                        tilleggsinformasjon
                );
            }
        }
    }
}
