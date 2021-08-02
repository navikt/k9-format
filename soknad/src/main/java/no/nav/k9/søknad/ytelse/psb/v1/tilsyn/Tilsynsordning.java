package no.nav.k9.søknad.ytelse.psb.v1.tilsyn;

import static java.util.Collections.unmodifiableMap;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.ytelse.psb.v1.LukketPeriode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Tilsynsordning {

    @JsonProperty(value="perioder", required = true)
    @Valid
    private Map<@Valid LukketPeriode, @Valid TilsynPeriodeInfo> perioder = new TreeMap<>();

    @JsonProperty(value="perioderSomSkalSlettes", required = true)
    @Valid
    private Map<@Valid LukketPeriode, @Valid TilsynPeriodeInfo> perioderSomSkalSlettes = new TreeMap<>();

    public Tilsynsordning() {
    }

    public Map<LukketPeriode, TilsynPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Tilsynsordning medPerioder(Map<LukketPeriode, TilsynPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public Tilsynsordning leggeTilPeriode(LukketPeriode periode, TilsynPeriodeInfo tilsynPeriodeInfo) {
        this.perioder.put(periode, tilsynPeriodeInfo);
        return this;
    }

    public Map<LukketPeriode, TilsynPeriodeInfo> getPerioderSomSkalSlettes() {
        return Collections.unmodifiableMap(perioderSomSkalSlettes);
    }

    public Tilsynsordning medPerioderSomSkalSlettes(Map<LukketPeriode, TilsynPeriodeInfo> perioderSomSkalSlettes) {
        this.perioderSomSkalSlettes = (perioderSomSkalSlettes == null) ? new TreeMap<>() : new TreeMap<>(perioderSomSkalSlettes);
        return this;
    }
}
