package no.nav.k9.søknad.ytelse.psb.v1.tilsyn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Tilsynsordning {

    @JsonProperty(value="iTilsynsordning", required=true)
    @NotNull
    @Valid
    private Boolean iTilsynsordning;

    @JsonProperty(value="opphold")
    @Valid
    private Map<Periode, PeriodeInfo> perioder;

    @JsonCreator
    public Tilsynsordning(
            @JsonProperty("iTilsynsordning") @Valid @NotNull Boolean iTilsynsordning,
            @JsonProperty("opphold") @Valid Map<Periode, PeriodeInfo> perioder) {
        this.iTilsynsordning = iTilsynsordning;
        this.perioder = new HashMap<>(perioder);
    }

    public Boolean getiTilsynsordning() {
        return iTilsynsordning;
    }

    public void setiTilsynsordning(Boolean iTilsynsordning) {
        this.iTilsynsordning = iTilsynsordning;
    }

    public Map<Periode, PeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public void setPerioder(Map<Periode, PeriodeInfo> perioder) {
        this.perioder = new HashMap<>(perioder);
    }

    public void leggeTilPerioder(Map<Periode, PeriodeInfo> perioder) {
        this.perioder.putAll(perioder);
    }
}
