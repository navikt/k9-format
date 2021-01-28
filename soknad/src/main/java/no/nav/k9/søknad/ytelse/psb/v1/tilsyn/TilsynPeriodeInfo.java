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
import java.time.Duration;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class TilsynPeriodeInfo {

    @Valid
    @NotNull
    @JsonProperty(value = "etablertTilsynTimerPerDag", required = true)
    private Duration etablertTilsynTimerPerDag;

    @JsonCreator
    public TilsynPeriodeInfo(@JsonProperty(value = "etablertTilsynTimerPerDag", required = true) @Valid @NotNull Duration etablertTilsynTimerPerDag) {
        this.etablertTilsynTimerPerDag = etablertTilsynTimerPerDag;
    }

    public TilsynPeriodeInfo() {
    }

    public Duration getEtablertTilsynTimerPerDag() {
        return etablertTilsynTimerPerDag;
    }

    public TilsynPeriodeInfo medEtablertTilsynTimerPerDag(Duration etablertTilsynTimerPerDag) {
        this.etablertTilsynTimerPerDag = etablertTilsynTimerPerDag;
        return this;
    }
}
