package no.nav.k9.søknad.ytelse.psb.v1;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Beredskap {

    @JsonProperty(value="perioder", required = true)
    @Valid
    @NotNull
    private Map<@Valid LukketPeriode, @Valid BeredskapPeriodeInfo> perioder = new TreeMap<>();

    @JsonProperty(value="perioderSomSkalSlettes")
    @Valid
    private Map<@Valid LukketPeriode, @Valid BeredskapPeriodeInfo> perioderSomSkalSlettes = new TreeMap<>();

    public Beredskap() {
    }

    public Map<LukketPeriode, BeredskapPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Beredskap medPerioder(Map<LukketPeriode, BeredskapPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public Map<LukketPeriode, BeredskapPeriodeInfo> getPerioderSomSkalSlettes() {
        return unmodifiableMap(perioderSomSkalSlettes);
    }

    public Beredskap medPerioderSomSkalSlettes(Map<LukketPeriode, BeredskapPeriodeInfo> perioderSomSkalSlettes) {
        this.perioderSomSkalSlettes = (perioderSomSkalSlettes == null) ? new TreeMap<>() : new TreeMap<>(perioderSomSkalSlettes);
        return this;
    }

    public Beredskap leggeTilPeriode(LukketPeriode periode, BeredskapPeriodeInfo beredskapPeriodeInfo) {
        this.perioder.put(periode, beredskapPeriodeInfo);
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class BeredskapPeriodeInfo {

        @JsonProperty(value = "tilleggsinformasjon", required = true)
        @Valid
        @NotNull
        private String tilleggsinformasjon;

        public BeredskapPeriodeInfo() {
        }

        public String getTilleggsinformasjon() {
            return tilleggsinformasjon;
        }

        public BeredskapPeriodeInfo medTilleggsinformasjon(String tilleggsinformasjon) {
            this.tilleggsinformasjon = Objects.requireNonNull(tilleggsinformasjon, "BeredskapPeriodeInfo.tilleggsinformasjon");
            return this;
        }
    }
}
