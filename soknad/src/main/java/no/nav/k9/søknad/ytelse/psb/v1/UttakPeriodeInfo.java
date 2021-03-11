package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class UttakPeriodeInfo {

    @Valid
    @NotNull
    @DurationMin
    @JsonProperty(value = "timerPleieAvBarnetPerDag", required = true)
    private Duration timerPleieAvBarnetPerDag;

    @JsonCreator
    public UttakPeriodeInfo(
            @JsonProperty(value = "timerPleieAvBarnetPerDag", required = true) @Valid @NotNull Duration timerPleieAvBarnetPerDag) {
        this.timerPleieAvBarnetPerDag = timerPleieAvBarnetPerDag;
    }

    public UttakPeriodeInfo() {
    }

    public Duration getTimerPleieAvBarnetPerDag() {
        return timerPleieAvBarnetPerDag;
    }

    public UttakPeriodeInfo setTimerPleieAvBarnetPerDag(Duration timerPleieAvBarnetPerDag) {
        this.timerPleieAvBarnetPerDag = timerPleieAvBarnetPerDag;
        return this;
    }
}
