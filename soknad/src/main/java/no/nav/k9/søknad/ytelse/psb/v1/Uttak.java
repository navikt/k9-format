package no.nav.k9.søknad.ytelse.psb.v1;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Uttak {

    @Valid
    @JsonProperty(value = "perioder")
    private Map<@Valid Periode, @Valid UttakPeriodeInfo> perioder = new TreeMap<>();

    @JsonProperty(value="perioderSomSkalSlettes")
    @Valid
    private Map<@Valid Periode, @Valid UttakPeriodeInfo> perioderSomSkalSlettes = new TreeMap<>();

    public Uttak() {
    }

    public Map<Periode, UttakPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Uttak medPerioder(Map<Periode, UttakPeriodeInfo> perioder) {
        this.perioder = (perioder == null ) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public Uttak leggeTilPeriode(Periode periode, UttakPeriodeInfo uttakPeriodeInfo) {
        this.perioder.put(periode, uttakPeriodeInfo);
        return this;
    }

    public Map<Periode, UttakPeriodeInfo> getPerioderSomSkalSlettes() {
        return unmodifiableMap(perioderSomSkalSlettes);
    }

    public Uttak medPerioderSomSkalSlettes(Map<Periode, UttakPeriodeInfo> perioderSomSkalSlettes) {
        this.perioderSomSkalSlettes = (perioderSomSkalSlettes == null) ? new TreeMap<>() : new TreeMap<>(perioderSomSkalSlettes);
        return this;
    }
}
