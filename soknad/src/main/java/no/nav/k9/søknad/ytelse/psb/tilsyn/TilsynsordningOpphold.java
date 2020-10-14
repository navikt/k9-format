package no.nav.k9.søknad.ytelse.psb.tilsyn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TilsynsordningOpphold {

    @JsonProperty(value="lengde", required=true)
    @NotNull
    public final Duration lengde;

    @JsonCreator
    private TilsynsordningOpphold(
            @JsonProperty(value="lengde", required=true)
            Duration lengde) {
        this.lengde = lengde;
    }

    @Override
    public String toString() {
        return lengde.toString();
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
