package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Periode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class Frilanser {

    public final Map<Periode, FrilanserPeriode> perioder;

    @JsonCreator
    private Frilanser(
            @JsonProperty("perioder")
            Map<Periode, FrilanserPeriode> perioder) {
        this.perioder = perioder == null ? emptyMap() : unmodifiableMap(perioder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, FrilanserPeriode> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, FrilanserPeriode> perioder) {
            this.perioder.putAll(perioder);
            return this;
        }

        public Builder periode(Periode periode, FrilanserPeriode frilanserPeriode) {
            this.perioder.put(periode, frilanserPeriode);
            return this;
        }

        public Frilanser build() {
            return new Frilanser(
                    perioder
            );
        }
    }

    public static final class FrilanserPeriode {

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private Builder() {}

            public FrilanserPeriode build() {
                return new FrilanserPeriode();
            }
        }

    }
}

