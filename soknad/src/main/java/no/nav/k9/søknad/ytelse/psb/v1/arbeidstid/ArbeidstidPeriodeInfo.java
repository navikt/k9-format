package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import java.time.Duration;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class ArbeidstidPeriodeInfo {

    @Valid
    @NotNull
    @DurationMin(hours = 0, message = "[ugyldigVerdi] Må være større eller lik 0.")
    @DurationMax(hours = 24, message = "[ugyldigVerdi] Må være lavere eller lik 24 timer.")
    @JsonProperty(value = "jobberNormaltTimerPerDag", required = true)
    private Duration jobberNormaltTimerPerDag;

    @Valid
    @NotNull
    @DurationMin(hours = 0, message = "[ugyldigVerdi] Må være større eller lik 0.")
    @DurationMax(hours = 24, message = "[ugyldigVerdi] Må være lavere eller lik 24 timer.")
    @JsonProperty(value = "faktiskArbeidTimerPerDag", required = true)
    private Duration faktiskArbeidTimerPerDag;

    public ArbeidstidPeriodeInfo(ArbeidstidPeriodeInfo info) {
        this.jobberNormaltTimerPerDag = info.jobberNormaltTimerPerDag;
        this.faktiskArbeidTimerPerDag = info.faktiskArbeidTimerPerDag;
    }

    public ArbeidstidPeriodeInfo() {
    }

    public Duration getFaktiskArbeidTimerPerDag() {
        return faktiskArbeidTimerPerDag;
    }

    public ArbeidstidPeriodeInfo medFaktiskArbeidTimerPerDag(Duration faktiskArbeidTimerPerDag) {
        this.faktiskArbeidTimerPerDag = Objects.requireNonNull(faktiskArbeidTimerPerDag, "faktiskArbeidTimerPerDag");
        return this;
    }

    public Duration getJobberNormaltTimerPerDag() {
        return jobberNormaltTimerPerDag;
    }

    public ArbeidstidPeriodeInfo medJobberNormaltTimerPerDag(Duration jobberNormaltTimerPerDag) {
        this.jobberNormaltTimerPerDag = Objects.requireNonNull(jobberNormaltTimerPerDag, "jobberNormaltTimerPerDag");
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(faktiskArbeidTimerPerDag, jobberNormaltTimerPerDag);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ArbeidstidPeriodeInfo other = (ArbeidstidPeriodeInfo) obj;
        return Objects.equals(faktiskArbeidTimerPerDag, other.faktiskArbeidTimerPerDag)
                && Objects.equals(jobberNormaltTimerPerDag, other.jobberNormaltTimerPerDag);
    }
}
