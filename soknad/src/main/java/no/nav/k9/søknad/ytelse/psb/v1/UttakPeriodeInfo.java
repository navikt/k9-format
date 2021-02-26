package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class UttakPeriodeInfo {

    @Valid
    @NotNull
    @JsonProperty(value = "timerPleieAvBarnetPerDag", required = true)
    private DurationWrapper timerPleieAvBarnetPerDag;

    @JsonCreator
    public UttakPeriodeInfo(
            @JsonProperty(value = "timerPleieAvBarnetPerDag", required = true) @Valid @NotNull Duration timerPleieAvBarnetPerDag) {
        this.timerPleieAvBarnetPerDag = new DurationWrapper(timerPleieAvBarnetPerDag);
    }

    public UttakPeriodeInfo() {
    }

    public Duration getTimerPleieAvBarnetPerDag() {
        return timerPleieAvBarnetPerDag.getDuration();
    }

    public UttakPeriodeInfo setTimerPleieAvBarnetPerDag(Duration timerPleieAvBarnetPerDag) {
        this.timerPleieAvBarnetPerDag = new DurationWrapper(timerPleieAvBarnetPerDag);
        return this;
    }
}
