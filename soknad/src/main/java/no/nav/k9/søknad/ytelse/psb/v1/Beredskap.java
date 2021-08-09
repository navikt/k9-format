package no.nav.k9.søknad.ytelse.psb.v1;

import static java.util.Collections.unmodifiableMap;
import static no.nav.k9.søknad.TidsserieValidator.validerOverlappendePerioder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Beredskap {

    @JsonProperty(value="perioder", required = true)
    @Valid
    @NotNull
    private Map<@Valid Periode, @Valid BeredskapPeriodeInfo> perioder = new TreeMap<>();

    @Deprecated
    @JsonProperty(value="perioderSomSkalSlettes")
    @Valid
    private Map<@Valid Periode, @Valid BeredskapPeriodeInfo> perioderSomSkalSlettes = new TreeMap<>();

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
        this.perioderSomSkalSlettes = (perioderSomSkalSlettes == null) ? new TreeMap<>() : new TreeMap<>(perioderSomSkalSlettes);
        return this;
    }

    public Beredskap leggeTilPeriode(Periode periode, BeredskapPeriodeInfo beredskapPeriodeInfo) {
        this.perioder.put(periode, beredskapPeriodeInfo);
        return this;
    }

    @Size(max = 0, message = "${validatedValue}")
    private List<Feil> getValiderPerioder() {
        if (perioder == null)
            List.of();
        return validerOverlappendePerioder(perioder, "beredskap.perioder");
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
