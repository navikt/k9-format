package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Periode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class Tilsynsordning {

    public final TilsynsordningSvar iTilsynsordning;
    public final Map<Periode, TilsynsordningOpphold> opphold;

    @JsonCreator
    private Tilsynsordning(
            @JsonProperty("iTilsynsordning")
            TilsynsordningSvar iTilsynsordning,
            @JsonProperty("opphold")
            Map<Periode, TilsynsordningOpphold> opphold) {
        this.iTilsynsordning = iTilsynsordning;
        this.opphold = (opphold == null) ? emptyMap() : unmodifiableMap(opphold);
    }

    public static Builder builder() {
        return new Builder();
    }
    public static final class Builder {
        private TilsynsordningSvar iTilsynsordning;
        private Map<Periode, TilsynsordningOpphold> opphold;

        private Builder() {
            opphold = new HashMap<>();
        }

        public Builder iTilsynsordning(TilsynsordningSvar iTilsynsordning) {
            this.iTilsynsordning = iTilsynsordning;
            return this;
        }

        public Builder opphold(Map<Periode, TilsynsordningOpphold> opphold) {
            this.opphold.putAll(opphold);
            return this;
        }

        public Builder opphold(Periode periode, TilsynsordningOpphold duration) {
            this.opphold.put(periode, duration);
            return this;
        }

        public Builder uke(TilsynsordningUke uke) {
            return opphold(uke.opphold);
        }

        public Tilsynsordning build() {
            return new Tilsynsordning(
                    iTilsynsordning,
                    opphold
            );
        }
    }
}
