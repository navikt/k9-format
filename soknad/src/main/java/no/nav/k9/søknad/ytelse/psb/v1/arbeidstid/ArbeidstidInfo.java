package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import com.fasterxml.jackson.annotation.*;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
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
        this.perioder = new HashMap<>(perioder);
    }

    public ArbeidstidInfo() {
    }

    public Duration getJobberNormaltTimerPerDag() {
        return jobberNormaltTimerPerDag;
    }

    public ArbeidstidInfo medJobberNormaltTimerPerDag(Duration jobberNormaltTimerPerDag) {
        this.jobberNormaltTimerPerDag = jobberNormaltTimerPerDag;
        return this;
    }

    public Map<Periode, ArbeidstidPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public ArbeidstidInfo medPerioder(Map<Periode, ArbeidstidPeriodeInfo> perioder) {
        this.perioder = new HashMap<>(perioder);
        return this;
    }

    public ArbeidstidInfo leggeTilPeriode(Map<Periode, ArbeidstidPeriodeInfo> perioder) {
        this.perioder.putAll(perioder);
        return this;
    }

    public ArbeidstidInfo leggeTilPeriode(Periode periode, ArbeidstidPeriodeInfo arbeidstidPeriodeInfo) {
        this.perioder.put(periode, arbeidstidPeriodeInfo);
        return this;
    }
}
