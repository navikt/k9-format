package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;
import no.nav.k9.søknad.felles.validering.periode.GyldigePerioderMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class ArbeidstidInfo {

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "perioder", required = true)
    @GyldigePerioderMap(payload = {AvbrytendeValideringsfeil.class})
    @NotNull
    private Map<@NotNull Periode, @Valid @NotNull ArbeidstidPeriodeInfo> perioder = new TreeMap<>();

    public ArbeidstidInfo() {
    }

    public ArbeidstidInfo(ArbeidstidInfo arbeidstidInfo) {
        this.perioder = new TreeMap<>(arbeidstidInfo.getPerioder().entrySet()
                .stream()
                .map(e -> Map.entry(
                        e.getKey(),
                        new ArbeidstidPeriodeInfo(e.getValue())
                ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    public Map<Periode, ArbeidstidPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public ArbeidstidInfo medPerioder(Map<Periode, ArbeidstidPeriodeInfo> perioder) {
        this.perioder = Objects.requireNonNull(perioder, "perioder");
        return this;
    }

    public ArbeidstidInfo leggeTilPeriode(Periode periode, ArbeidstidPeriodeInfo arbeidstidPeriodeInfo) {
        Objects.requireNonNull(periode, "periode");
        Objects.requireNonNull(arbeidstidPeriodeInfo, "arbeidstidPeriodeInfo");
        this.perioder.put(periode, arbeidstidPeriodeInfo);
        return this;
    }
}
