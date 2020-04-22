package no.nav.k9.søknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import no.nav.k9.søknad.felles.Periode;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import static no.nav.k9.søknad.felles.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.Periode.Utils.leggTilPerioder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SelvstendigNæringsdrivende {

    @JsonProperty(value="perioder")
    @JsonInclude(value = Include.ALWAYS)
    @Valid
    public final Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> perioder;

    @JsonCreator
    private SelvstendigNæringsdrivende(
            @JsonProperty("perioder")
            Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, SelvstendigNæringsdrivendePeriodeInfo> perioder) {
            leggTilPerioder(this.perioder, perioder);
            return this;
        }

        public Builder periode(Periode periode, SelvstendigNæringsdrivendePeriodeInfo selvstendigNæringsdrivendePeriodeInfo) {
            leggTilPeriode(this.perioder, periode, selvstendigNæringsdrivendePeriodeInfo);
            return this;
        }

        public SelvstendigNæringsdrivende build() {
            return new SelvstendigNæringsdrivende(
                    perioder
            );
        }
    }

    public static final class SelvstendigNæringsdrivendePeriodeInfo {

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private Builder() {}

            public SelvstendigNæringsdrivendePeriodeInfo build() {
                return new SelvstendigNæringsdrivendePeriodeInfo();
            }
        }

    }
}
