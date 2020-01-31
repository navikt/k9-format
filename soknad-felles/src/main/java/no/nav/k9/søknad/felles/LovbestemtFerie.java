package no.nav.k9.søknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class LovbestemtFerie {
    public final Map<Periode, LovbestemtFeriePeriodeInfo> perioder;

    @JsonCreator
    private LovbestemtFerie(
            @JsonProperty("perioder")
            Map<Periode, LovbestemtFeriePeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, LovbestemtFeriePeriodeInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, LovbestemtFeriePeriodeInfo> perioder) {
            this.perioder.putAll(perioder);
            return this;
        }

        public Builder periode(Periode periode, LovbestemtFeriePeriodeInfo lovbestemtFeriePeriodeInfo) {
            this.perioder.put(periode, lovbestemtFeriePeriodeInfo);
            return this;
        }

        public LovbestemtFerie build() {
            return new LovbestemtFerie(
                    perioder
            );
        }
    }

    public static final class LovbestemtFeriePeriodeInfo {

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {

            private Builder() { }

            public LovbestemtFeriePeriodeInfo build() {
                return new LovbestemtFeriePeriodeInfo();
            }
        }
    }
}
