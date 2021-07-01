package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class ArbeidstidInfo {

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "perioder", required = true)
    @Valid
    @NotNull
    private Map<Periode, ArbeidstidPeriodeInfo> perioder;

    @JsonCreator
    public ArbeidstidInfo(
            @JsonProperty(value = "perioder", required = true) @Valid @NotNull Map<Periode, ArbeidstidPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
    }

    public ArbeidstidInfo() {
    }

    public Map<Periode, ArbeidstidPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public ArbeidstidInfo medPerioder(Map<Periode, ArbeidstidPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public ArbeidstidInfo leggeTilPeriode(Periode periode, ArbeidstidPeriodeInfo arbeidstidPeriodeInfo) {
        this.perioder.put(periode, arbeidstidPeriodeInfo);
        return this;
    }
}
