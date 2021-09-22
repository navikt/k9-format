package no.nav.k9.søknad.ytelse.psb.v1.tilsyn;

import static java.util.Collections.unmodifiableMap;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Tilsynsordning {

    @JsonProperty(value="perioder", required = true)
    @NotNull
    @Valid
    private Map<@NotNull Periode, @NotNull TilsynPeriodeInfo> perioder = new TreeMap<>();

    // Hvorfor er dette et map? Dette er vel egentlig en liste med perioder?
    @JsonProperty(value="perioderSomSkalSlettes", required = true)
    @Valid
    private Map<@NotNull Periode, @NotNull TilsynPeriodeInfo> perioderSomSkalSlettes = new TreeMap<>();

    public Tilsynsordning() {
    }

    public Map<Periode, TilsynPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Tilsynsordning medPerioder(Map<Periode, TilsynPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public Tilsynsordning leggeTilPeriode(Periode periode, TilsynPeriodeInfo tilsynPeriodeInfo) {
        this.perioder.put(periode, tilsynPeriodeInfo);
        return this;
    }

    public Tilsynsordning leggeTilPeriode(Map<Periode, TilsynPeriodeInfo> perioder) {
        this.perioder.putAll(perioder);
        return this;
    }

    public Map<Periode, TilsynPeriodeInfo> getPerioderSomSkalSlettes() {
        return Collections.unmodifiableMap(perioderSomSkalSlettes);
    }

    public Tilsynsordning medPerioderSomSkalSlettes(Map<Periode, TilsynPeriodeInfo> perioderSomSkalSlettes) {
        this.perioderSomSkalSlettes = (perioderSomSkalSlettes == null) ? new TreeMap<>() : new TreeMap<>(perioderSomSkalSlettes);
        return this;
    }
}
