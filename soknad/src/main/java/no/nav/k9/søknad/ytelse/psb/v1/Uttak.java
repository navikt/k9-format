package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Uttak {
    @Valid
    @NotEmpty
    @JsonProperty(value = "perioder", required = true)
    private Map<Periode, UttakPeriodeInfo> perioder;

    @JsonCreator
    public Uttak( @JsonProperty(value = "perioder", required = true) @Valid @NotNull Map<Periode, UttakPeriodeInfo> perioder ) {
        this.perioder = new HashMap<>(perioder);
    }

    public Uttak() {
    }

    public Map<Periode, UttakPeriodeInfo> getPerioder() {
        return perioder;
    }

    public Uttak setPerioder(Map<Periode, UttakPeriodeInfo> perioder) {
        this.perioder = new HashMap<>(perioder);
        return this;
    }

    public Uttak leggeTilPeriode(Periode periode, UttakPeriodeInfo uttakPeriodeInfo) {
        this.perioder.put(periode, uttakPeriodeInfo);
        return this;
    }
}
