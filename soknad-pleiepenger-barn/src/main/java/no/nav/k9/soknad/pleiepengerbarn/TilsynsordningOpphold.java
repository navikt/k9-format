package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;

public class TilsynsordningOpphold {
    public final Duration lengde;

    @JsonCreator
    private TilsynsordningOpphold(
            @JsonProperty("lengde")
            Duration lengde) {
        this.lengde = lengde;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Duration lengde;

        private Builder() {}

        public Builder lengde(Duration lengde) {
            this.lengde = lengde;
            return this;
        }

        public TilsynsordningOpphold build() {
            return new TilsynsordningOpphold(lengde);
        }
    }
}
