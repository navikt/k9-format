package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import com.fasterxml.jackson.annotation.*;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class ArbeidstidInfo {

    @Valid
    @NotNull
    @JsonProperty(value = "jobberNormaltTimerPerDag", required = true)
    private Duration jobberNormaltTimerPerDag;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "perioder")
    @Valid
    @NotNull
    private Map<Periode, ArbeidstidPeriodeInfo> perioder;



    @JsonCreator
    public ArbeidstidInfo(@JsonProperty(value = "jobberNormaltTimerPerDag", required = true) @Valid @NotNull Duration jobberNormaltTimerPerDag,
            @JsonProperty(value = "perioder") @Valid @NotNull Map<Periode, ArbeidstidPeriodeInfo> perioder) {
        this.jobberNormaltTimerPerDag = jobberNormaltTimerPerDag;
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public Map<Periode, ArbeidstidPeriodeInfo> getPerioder() {
        return perioder;
    }

    public Duration getJobberNormaltTimerPerDag() {
        return jobberNormaltTimerPerDag;
    }

    public void setJobberNormaltTimerPerDag(Duration jobberNormaltTimerPerDag) {
        this.jobberNormaltTimerPerDag = jobberNormaltTimerPerDag;
    }
}
