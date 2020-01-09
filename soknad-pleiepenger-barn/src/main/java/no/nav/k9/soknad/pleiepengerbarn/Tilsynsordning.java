package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Tilsynsordning {

    public final TilsynsordningSvar iTilsynsordning;
    public final Map<LocalDate, TilsynsordningOpphold> opphold;

    @JsonCreator
    private Tilsynsordning(
            @JsonProperty("iTilsynsordning")
            TilsynsordningSvar iTilsynsordning,
            @JsonProperty("opphold")
            Map<LocalDate, TilsynsordningOpphold> opphold) {
        this.iTilsynsordning = iTilsynsordning;
        this.opphold = opphold;
    }

    public static Builder builder() {
        return new Builder();
    }
    public static final class Builder {
        private TilsynsordningSvar iTilsynsordning;
        private Map<LocalDate, TilsynsordningOpphold> opphold;

        private Builder() {
            opphold = new HashMap<>();
        }

        public Builder iTilsynsordning(TilsynsordningSvar iTilsynsordning) {
            this.iTilsynsordning = iTilsynsordning;
            return this;
        }

        public Builder opphold(Map<LocalDate, TilsynsordningOpphold> opphold) {
            this.opphold.putAll(opphold);
            return this;
        }

        public Builder opphold(LocalDate dato, TilsynsordningOpphold duration) {
            this.opphold.put(dato, duration);
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
