package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Periode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class SelvstendigNæringsdrivende {

    public final Map<Periode, SelvstendigNæringsdrivendeInfo> perioder;

    @JsonCreator
    private SelvstendigNæringsdrivende(
            @JsonProperty("perioder")
            Map<Periode, SelvstendigNæringsdrivendeInfo> perioder) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, SelvstendigNæringsdrivendeInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, SelvstendigNæringsdrivendeInfo> perioder) {
            this.perioder.putAll(perioder);
            return this;
        }

        public Builder periode(Periode periode, SelvstendigNæringsdrivendeInfo selvstendigNæringsdrivendeInfo) {
            this.perioder.put(periode, selvstendigNæringsdrivendeInfo);
            return this;
        }

        public SelvstendigNæringsdrivende build() {
            return new SelvstendigNæringsdrivende(
                    perioder
            );
        }
    }

    public static final class SelvstendigNæringsdrivendeInfo {

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private Builder() {}

            public SelvstendigNæringsdrivendeInfo build() {
                return new SelvstendigNæringsdrivendeInfo();
            }
        }

    }
}
