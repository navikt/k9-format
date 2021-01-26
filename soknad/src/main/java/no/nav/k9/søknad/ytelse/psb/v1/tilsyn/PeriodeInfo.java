package no.nav.k9.søknad.ytelse.psb.v1.tilsyn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class PeriodeInfo {

    @Valid
    @NotNull
    @Min(0)
    @JsonProperty(value = "etablertTilsynTimerPerDag", required = true)
    private BigDecimal etablertTilsynTimerPerDag;

    @JsonCreator
    public PeriodeInfo(@JsonProperty(value = "etablertTilsynTimerPerDag", required = true) @Valid @NotNull @Min(0) BigDecimal etablertTilsynTimerPerDag) {
        this.etablertTilsynTimerPerDag = etablertTilsynTimerPerDag;
    }

    public BigDecimal getEtablertTilsynTimerPerDag() {
        return etablertTilsynTimerPerDag;
    }

    public void setEtablertTilsynTimerPerDag(BigDecimal etablertTilsynTimerPerDag) {
        this.etablertTilsynTimerPerDag = etablertTilsynTimerPerDag;
    }
}
