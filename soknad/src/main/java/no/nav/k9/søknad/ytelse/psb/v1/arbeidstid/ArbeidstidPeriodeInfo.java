package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import java.time.Duration;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class ArbeidstidPeriodeInfo {

    @Valid
    @NotNull
    @DurationMin
    @DurationMax(hours = 24)
    @JsonProperty(value = "jobberNormaltTimerPerDag", required = true)
    private Duration jobberNormaltTimerPerDag;

    @Valid
    @NotNull
    @DurationMin
    @DurationMax(hours = 24)
    @JsonProperty(value = "faktiskArbeidTimerPerDag", required = true)
    private Duration faktiskArbeidTimerPerDag;

    @JsonCreator
    public ArbeidstidPeriodeInfo(
            @JsonProperty(value = "jobberNormaltTimerPerDag", required = true) @Valid @NotNull @DurationMin Duration jobberNormaltTimerPerDag,
            @JsonProperty(value = "faktiskArbeidTimerPerDag", required = true) @Valid @NotNull @DurationMin Duration faktiskArbeidTimerPerDag) {
        this.jobberNormaltTimerPerDag = jobberNormaltTimerPerDag;
        this.faktiskArbeidTimerPerDag = faktiskArbeidTimerPerDag;
    }

    public ArbeidstidPeriodeInfo() {
    }

    public Duration getFaktiskArbeidTimerPerDag() {
        return faktiskArbeidTimerPerDag;
    }

    public ArbeidstidPeriodeInfo medFaktiskArbeidTimerPerDag(Duration faktiskArbeidTimerPerDag) {
        this.faktiskArbeidTimerPerDag = faktiskArbeidTimerPerDag;
        return this;
    }

    public Duration getJobberNormaltTimerPerDag() {
        return jobberNormaltTimerPerDag;
    }

    public ArbeidstidPeriodeInfo medJobberNormaltTimerPerDag(Duration jobberNormaltTimerPerDag) {
        this.jobberNormaltTimerPerDag = jobberNormaltTimerPerDag;
        return this;
    }
}
