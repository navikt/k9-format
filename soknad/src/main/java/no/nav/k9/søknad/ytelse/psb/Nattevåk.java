package no.nav.k9.søknad.ytelse.psb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import no.nav.k9.søknad.felles.type.Periode;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPerioder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Nattevåk {

    @JsonProperty(value="perioder")
    @JsonInclude(value = Include.NON_NULL)
    @Valid
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class NattevåkPeriodeInfo {

        @JsonProperty(value="tilleggsinformasjon")
        @Valid
        public final String tilleggsinformasjon;

        @JsonCreator
        private NattevåkPeriodeInfo(
                @JsonProperty(value="tilleggsinformasjon")
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
