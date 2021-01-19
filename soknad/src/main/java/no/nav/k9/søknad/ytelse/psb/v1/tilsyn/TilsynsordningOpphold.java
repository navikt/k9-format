package no.nav.k9.søknad.ytelse.psb.v1.tilsyn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class TilsynsordningOpphold {

    @JsonProperty(value="lengde", required=true)
    @NotNull
    private Duration lengde;

    @JsonCreator
    public TilsynsordningOpphold(
            @JsonProperty(value="lengde", required=true)
            Duration lengde) {
        this.lengde = lengde;
    }

    public Duration getLengde() {
        return lengde;
    }

    public void setLengde(Duration lengde) {
        this.lengde = lengde;
    }

    @Override
    public String toString() {
        return lengde.toString();
    }
}
