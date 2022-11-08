package no.nav.k9.søknad.ytelse.psb.v1;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.validering.GyldigePerioderMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Beredskap {

    @JsonProperty(value="perioder", required = true)
    @Valid
    @GyldigePerioderMap
    @NotNull
    private Map<@NotNull Periode, @NotNull @Valid BeredskapPeriodeInfo> perioder = new TreeMap<>();

    // Hvorfor er dette et map? Dette er vel egentlig en liste med perioder?
    //TODO gjøre om til List
    @JsonProperty(value="perioderSomSkalSlettes")
    @Valid
    @GyldigePerioderMap
    private Map<@NotNull Periode, @NotNull @Valid BeredskapPeriodeInfo> perioderSomSkalSlettes = new TreeMap<>();

    public Beredskap() {
    }

    public Map<Periode, BeredskapPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Beredskap medPerioder(Map<Periode, BeredskapPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public Map<Periode, BeredskapPeriodeInfo> getPerioderSomSkalSlettes() {
        return unmodifiableMap(perioderSomSkalSlettes);
    }

    public Beredskap medPerioderSomSkalSlettes(Map<Periode, BeredskapPeriodeInfo> perioderSomSkalSlettes) {
        this.perioderSomSkalSlettes = Objects.requireNonNull(perioderSomSkalSlettes, "perioderSomSkalSlettes");
        return this;
    }

    public Beredskap leggeTilPeriode(Periode periode, BeredskapPeriodeInfo beredskapPeriodeInfo) {
        Objects.requireNonNull(periode, "periode");
        Objects.requireNonNull(beredskapPeriodeInfo, "beredskapPeriodeInfo");
        this.perioder.put(periode, beredskapPeriodeInfo);
        return this;
    }

    public Beredskap leggeTilPeriode(Map<Periode, BeredskapPeriodeInfo> perioder) {
        Objects.requireNonNull(perioder, "perioder");
        this.perioder.putAll(perioder);
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class BeredskapPeriodeInfo {

        @JsonProperty(value = "tilleggsinformasjon", required = true)
        @Valid
        private String tilleggsinformasjon;

        public BeredskapPeriodeInfo() {
        }

        public String getTilleggsinformasjon() {
            return tilleggsinformasjon;
        }

        public BeredskapPeriodeInfo medTilleggsinformasjon(String tilleggsinformasjon) {
            this.tilleggsinformasjon = Objects.requireNonNull(tilleggsinformasjon, "tilleggsinformasjon");
            return this;
        }

        @AssertTrue(message = "[tomFeil] Feltet kan ikke være tomt")
        private boolean isNotEmpty() {
            if (tilleggsinformasjon == null) {
                return true;
            }
            return !tilleggsinformasjon.isEmpty();
        }
    }
}
