package no.nav.k9.søknad.ytelse.psb.v1;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;
import no.nav.k9.søknad.felles.validering.periode.GyldigePerioderMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Nattevåk {

    @JsonProperty(value = "perioder", required = true)
    @Valid
    @GyldigePerioderMap(payload = {AvbrytendeValideringsfeil.class})
    @NotNull
    private Map<@NotNull Periode, @NotNull @Valid NattevåkPeriodeInfo> perioder = new TreeMap<>();

    //TODO list?
    @JsonProperty(value = "perioderSomSkalSlettes", required = true)
    @Valid
    @GyldigePerioderMap(payload = {AvbrytendeValideringsfeil.class})
    private Map<@NotNull Periode, @NotNull @Valid NattevåkPeriodeInfo> perioderSomSkalSlettes = new TreeMap<>();

    public Nattevåk() {
    }

    public Map<Periode, NattevåkPeriodeInfo> getPerioder() {
        return Collections.unmodifiableMap(perioder);
    }

    public Nattevåk medPerioder(Map<Periode, NattevåkPeriodeInfo> perioder) {
        this.perioder = Objects.requireNonNull(perioder, "perioder");
        return this;
    }

    public Nattevåk leggeTilPeriode(Periode periode, NattevåkPeriodeInfo nattevåkPeriodeInfo) {
        Objects.requireNonNull(periode, "periode");
        Objects.requireNonNull(nattevåkPeriodeInfo, "nattevåkPeriodeInfo");
        this.perioder.put(periode, nattevåkPeriodeInfo);
        return this;
    }

    public Nattevåk leggeTilPeriode(Map<Periode, NattevåkPeriodeInfo> perioder) {
        Objects.requireNonNull(perioder, "perioder");
        this.perioder.putAll(perioder);
        return this;
    }

    public Map<Periode, NattevåkPeriodeInfo> getPerioderSomSkalSlettes() {
        return Collections.unmodifiableMap(perioderSomSkalSlettes);
    }

    public Nattevåk medPerioderSomSkalSlettes(Map<Periode, NattevåkPeriodeInfo> perioderSomSkalSlettes) {
        this.perioderSomSkalSlettes = Objects.requireNonNull(perioderSomSkalSlettes, "perioderSomSkalSlettes");
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class NattevåkPeriodeInfo {

        @JsonProperty(value = "tilleggsinformasjon", required = true)
        @Valid
        private String tilleggsinformasjon;

        public NattevåkPeriodeInfo() {
        }

        public String getTilleggsinformasjon() {
            return tilleggsinformasjon;
        }

        public NattevåkPeriodeInfo medTilleggsinformasjon(String tilleggsinformasjon) {
            this.tilleggsinformasjon = Objects.requireNonNull(tilleggsinformasjon, "NattevåkPeriodeInfo.tilleggsinformasjon");
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
