package no.nav.k9.søknad.ytelse.psb.v1.arbeid;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ArbeidPeriodeInfo {

    @Valid
    @NotNull
    @Min(0)
    @JsonProperty(value = "faktiskArbeidTimerPerDag")
    private BigDecimal faktiskArbeidTimerPerDag;

    @Valid
    @NotNull
    @Min(0)
    @JsonProperty(value = "jobberNormaltTimerPerDag")
    private BigDecimal jobberNormaltTimerPerDag;


    public ArbeidPeriodeInfo(
            @JsonProperty(value = "faktiskArbeidTimerPerDag") @Valid @NotNull BigDecimal faktiskArbeidTimerPerDag,
            @JsonProperty(value = "jobberNormaltTimerPerDag") @Valid @NotNull BigDecimal jobberNormaltTimerPerDag) {
        this.faktiskArbeidTimerPerDag = faktiskArbeidTimerPerDag;
        this.jobberNormaltTimerPerDag = jobberNormaltTimerPerDag;
    }

    public BigDecimal getFaktiskArbeidTimerPerDag() {
        return faktiskArbeidTimerPerDag;
    }

    public BigDecimal getJobberNormaltTimerPerDag() {
        return jobberNormaltTimerPerDag;
    }

}
