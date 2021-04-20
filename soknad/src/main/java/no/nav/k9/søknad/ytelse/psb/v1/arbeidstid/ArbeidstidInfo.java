package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import com.fasterxml.jackson.annotation.*;
import no.nav.k9.søknad.felles.type.Periode;
import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.unmodifiableMap;

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
