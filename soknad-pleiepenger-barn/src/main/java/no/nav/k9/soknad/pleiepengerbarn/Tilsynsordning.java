package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Periode;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Tilsynsordning {

    public final TilsynsordningSvar iTilsynsordning;
    public final Map<Periode, Duration> opphold;

    @JsonCreator
    private Tilsynsordning(
            @JsonProperty("iTilsynsordning")
            TilsynsordningSvar iTilsynsordning,
            @JsonProperty("opphold")
            Map<Periode, Duration> opphold) {
        this.iTilsynsordning = iTilsynsordning;
        this.opphold = opphold;
    }

    public static Builder builder() {
        return new Builder();
    }
    public static final class Builder {
        private TilsynsordningSvar iTilsynsordning;
        private Map<Periode, Duration> opphold;

        private Builder() {
            opphold = new HashMap<>();
        }

        public Builder iTilsynsordning(TilsynsordningSvar iTilsynsordning) {
            this.iTilsynsordning = iTilsynsordning;
            return this;
        }

        public Builder opphold(Map<Periode, Duration> opphold) {
            this.opphold.putAll(opphold);
            return this;
        }

        public Builder opphold(Periode periode, Duration duration) {
            this.opphold.put(periode, duration);
            return this;
        }

        public Tilsynsordning build() {
            return new Tilsynsordning(
                    iTilsynsordning,
                    opphold
            );
        }
    }
}
