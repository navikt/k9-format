package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import com.fasterxml.jackson.annotation.*;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class ArbeidstidInfo {

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "perioder")
    @Valid
    @NotNull
    private Map<Periode, ArbeidstidPeriodeInfo> perioder;

    @JsonCreator
    public ArbeidstidInfo(@JsonProperty(value = "perioder") @Valid @NotNull Map<Periode, ArbeidstidPeriodeInfo> perioder) {
        this.perioder = perioder;
    }

    public Map<Periode, ArbeidstidPeriodeInfo> getPerioder() {
        return perioder;
    }

    public void setPerioder(Map<Periode, ArbeidstidPeriodeInfo> perioder) {
        this.perioder = perioder;
    }
}
