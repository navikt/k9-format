package no.nav.k9.søknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.Periode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import static no.nav.k9.søknad.felles.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.Periode.Utils.leggTilPerioder;


public class Nattevåk {
    public final Map<Periode, NattevåkPeriodeInfo> perioder;

    @JsonCreator
    private Nattevåk(
            @JsonProperty("perioder")
            Map<Periode, NattevåkPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, NattevåkPeriodeInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, NattevåkPeriodeInfo> perioder) {
            leggTilPerioder(this.perioder, perioder);
            return this;
        }

        public Builder periode(Periode periode, NattevåkPeriodeInfo nattevåkPeriodeInfo) {
            leggTilPeriode(this.perioder, periode, nattevåkPeriodeInfo);
            return this;
        }

        public Nattevåk build() {
            return new Nattevåk(
                    perioder
            );
        }
    }

    public static final class NattevåkPeriodeInfo {
        public final String tilleggsinformasjon;

        @JsonCreator
        private NattevåkPeriodeInfo(
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

            public NattevåkPeriodeInfo build() {
                return new NattevåkPeriodeInfo(
                        tilleggsinformasjon
                );
            }
        }
    }
}
