package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Periode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class Nattevaak {
    public final Map<Periode, NattevaakPeriodeInfo> perioder;

    @JsonCreator
    private Nattevaak(
            @JsonProperty("perioder")
            Map<Periode, NattevaakPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, NattevaakPeriodeInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, NattevaakPeriodeInfo> perioder) {
            this.perioder.putAll(perioder);
            return this;
        }

        public Builder periode(Periode periode, NattevaakPeriodeInfo nattevaakPeriodeInfo) {
            this.perioder.put(periode, nattevaakPeriodeInfo);
            return this;
        }

        public Nattevaak build() {
            return new Nattevaak(
                    perioder
            );
        }
    }

    public static final class NattevaakPeriodeInfo {
        public final String tilleggsinformasjon;

        @JsonCreator
        private NattevaakPeriodeInfo(
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

            public NattevaakPeriodeInfo build() {
                return new NattevaakPeriodeInfo(
                        tilleggsinformasjon
                );
            }
        }
    }
}
