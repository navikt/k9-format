package no.nav.k9.søknad.ytelse.psb.v1.tilsyn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.unmodifiableMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Tilsynsordning {

    @JsonProperty(value="perioder")
    @Valid
    private Map<Periode, TilsynPeriodeInfo> perioder;

    @JsonCreator
    public Tilsynsordning(
            @JsonProperty("perioder") @Valid Map<Periode, TilsynPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
    }

    public Tilsynsordning() {
    }

    public Map<Periode, TilsynPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Tilsynsordning setPerioder(Map<Periode, TilsynPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public Tilsynsordning leggeTilPeriode(Periode periode, TilsynPeriodeInfo tilsynPeriodeInfo) {
        this.perioder.put(periode, tilsynPeriodeInfo);
        return this;
    }
}
