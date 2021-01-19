package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class UttakPeriodeInfo {

    @Valid
    @NotNull
    @JsonProperty(value = "timerPleieAvBarnetPerDag", required = true)
    private BigDecimal timerPleieAvBarnetPerDag;

    @JsonCreator
    public UttakPeriodeInfo(
            @JsonProperty(value = "timerPleieAvBarnetPerDag", required = true) @Valid @NotNull BigDecimal timerPleieAvBarnetPerDag) {
        this.timerPleieAvBarnetPerDag = timerPleieAvBarnetPerDag;
    }

    public BigDecimal getTimerPleieAvBarnetPerDag() {
        return timerPleieAvBarnetPerDag;
    }

    public void setTimerPleieAvBarnetPerDag(BigDecimal timerPleieAvBarnetPerDag) {
        this.timerPleieAvBarnetPerDag = timerPleieAvBarnetPerDag;
    }
}
