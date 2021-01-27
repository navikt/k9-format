package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class ArbeidstidPeriodeInfo {

    @Valid
    @NotNull
    @Min(0)
    @JsonProperty(value = "jobberNormaltTimerPerDag")
    private BigDecimal jobberNormaltTimerPerDag;

    @Valid
    @NotNull
    @Min(0)
    @JsonProperty(value = "faktiskArbeidTimerPerDag")
    private BigDecimal faktiskArbeidTimerPerDag;

    @JsonCreator
    public ArbeidstidPeriodeInfo(
            @JsonProperty(value = "jobberNormaltTimerPerDag") @Valid @NotNull BigDecimal jobberNormaltTimerPerDag,
            @JsonProperty(value = "faktiskArbeidTimerPerDag") @Valid @NotNull BigDecimal faktiskArbeidTimerPerDag) {
        this.jobberNormaltTimerPerDag = jobberNormaltTimerPerDag;
        this.faktiskArbeidTimerPerDag = faktiskArbeidTimerPerDag;
    }

    public BigDecimal getFaktiskArbeidTimerPerDag() {
        return faktiskArbeidTimerPerDag;
    }

    public void setFaktiskArbeidTimerPerDag(BigDecimal faktiskArbeidTimerPerDag) {
        this.faktiskArbeidTimerPerDag = faktiskArbeidTimerPerDag;
    }

    public BigDecimal getJobberNormaltTimerPerDag() {
        return jobberNormaltTimerPerDag;
    }

    public void setJobberNormaltTimerPerDag(BigDecimal jobberNormaltTimerPerDag) {
        this.jobberNormaltTimerPerDag = jobberNormaltTimerPerDag;
    }
}
