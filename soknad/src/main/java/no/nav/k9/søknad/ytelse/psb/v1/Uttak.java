package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;


public class Uttak {
    @Valid
    @NotEmpty
    @JsonProperty(value = "perioder", required = true)
    private Map<Periode, UttakPeriodeInfo> perioder;

    @JsonCreator
    public Uttak( @JsonProperty(value = "perioder", required = true) @Valid @NotNull Map<Periode, UttakPeriodeInfo> perioder ) {
        this.perioder = perioder;
    }

    public Map<Periode, UttakPeriodeInfo> getPerioder() {
        return perioder;
    }

    public void setPerioder(Map<Periode, UttakPeriodeInfo> perioder) {
        this.perioder = perioder;
    }
}
