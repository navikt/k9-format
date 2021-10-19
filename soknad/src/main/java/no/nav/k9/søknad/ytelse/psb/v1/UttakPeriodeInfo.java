package no.nav.k9.søknad.ytelse.psb.v1;

import java.time.Duration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class UttakPeriodeInfo {

    @Valid
    @NotNull
    @DurationMin(hours = 0)
    @DurationMax(hours = 7, minutes = 30)
    @JsonProperty(value = "timerPleieAvBarnetPerDag", required = true)
    private Duration timerPleieAvBarnetPerDag;


    public UttakPeriodeInfo(Duration timerPleieAvBarnetPerDag) {
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
