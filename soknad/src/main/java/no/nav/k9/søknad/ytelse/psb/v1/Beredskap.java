package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPerioder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Beredskap {

    @JsonProperty(value="perioder")
    @JsonInclude(value = Include.ALWAYS)
    @Valid
    public final Map<Periode, BeredskapPeriodeInfo> perioder;

    @JsonCreator
    private Beredskap(
            @JsonProperty("perioder")
            Map<Periode, BeredskapPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, BeredskapPeriodeInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, BeredskapPeriodeInfo> perioder) {
            leggTilPerioder(this.perioder, perioder);
            return this;
        }

        public Builder periode(Periode periode, BeredskapPeriodeInfo beredskapPeriodeInfo) {
            leggTilPeriode(this.perioder, periode, beredskapPeriodeInfo);
            return this;
        }

        public Beredskap build() {
            return new Beredskap(
                    perioder
            );
        }
    }

    public static final class BeredskapPeriodeInfo {
        public final String tilleggsinformasjon;

        @JsonCreator
        private BeredskapPeriodeInfo(
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

            public BeredskapPeriodeInfo build() {
                return new BeredskapPeriodeInfo(
                        tilleggsinformasjon
                );
            }
        }
    }
}
