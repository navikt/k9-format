package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Nattevåk {

    @JsonProperty(value="perioder", required = true)
    @Valid
    @NotNull
    private Map<Periode, NattevåkPeriodeInfo> perioder = new TreeMap<>();

    @JsonProperty(value="perioderSomSkalSlettes", required = true)
    @Valid
    private Map<Periode, NattevåkPeriodeInfo> perioderSomSkalSlettes = new TreeMap<>();

    public Nattevåk() {
    }

    public Map<Periode, NattevåkPeriodeInfo> getPerioder() {
        return Collections.unmodifiableMap(perioder);
    }

    public Nattevåk medPerioder(Map<Periode, NattevåkPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public Nattevåk leggeTilPeriode(Periode periode, NattevåkPeriodeInfo nattevåkPeriodeInfo) {
        this.perioder.put(periode, nattevåkPeriodeInfo);
        return this;
    }

    public Map<Periode, NattevåkPeriodeInfo> getPerioderSomSkalSlettes() {
        return Collections.unmodifiableMap(perioderSomSkalSlettes);
    }

    public Nattevåk medPerioderSomSkalSlettes(Map<Periode, NattevåkPeriodeInfo> perioderSomSkalSlettes) {
        this.perioderSomSkalSlettes = (perioderSomSkalSlettes == null) ? new TreeMap<>() : new TreeMap<>(perioderSomSkalSlettes);
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class NattevåkPeriodeInfo {

        @JsonProperty(value="tilleggsinformasjon", required = true)
        @Valid
        @NotNull
        private String tilleggsinformasjon;

        public NattevåkPeriodeInfo() {
        }

        public String getTilleggsinformasjon() {
            return tilleggsinformasjon;
        }

        public NattevåkPeriodeInfo medTilleggsinformasjon(String tilleggsinformasjon) {
            this.tilleggsinformasjon = tilleggsinformasjon;
            return this;
        }
    }
}
