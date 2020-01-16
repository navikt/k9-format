package no.nav.k9.søknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.Periode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class Frilanser {

    public final Map<Periode, FrilanserPeriodeInfo> perioder;

    @JsonCreator
    private Frilanser(
            @JsonProperty("perioder")
            Map<Periode, FrilanserPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, FrilanserPeriodeInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, FrilanserPeriodeInfo> perioder) {
            this.perioder.putAll(perioder);
            return this;
        }

        public Builder periode(Periode periode, FrilanserPeriodeInfo frilanserPeriodeInfo) {
            this.perioder.put(periode, frilanserPeriodeInfo);
            return this;
        }

        public Frilanser build() {
            return new Frilanser(
                    perioder
            );
        }
    }

    public static final class FrilanserPeriodeInfo {

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private Builder() {}

            public FrilanserPeriodeInfo build() {
                return new FrilanserPeriodeInfo();
            }
        }

    }
}

