package no.nav.k9.søknad.ytelse.psb.v1.tilsyn;

import static java.util.Collections.unmodifiableMap;
import static no.nav.k9.søknad.TidsserieValidator.validerOverlappendePerioder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Tilsynsordning {

    @JsonProperty(value="perioder", required = true)
    @Valid
    private Map<@Valid Periode, @Valid TilsynPeriodeInfo> perioder = new TreeMap<>();

    @Deprecated
    @JsonProperty(value="perioderSomSkalSlettes", required = true)
    @Valid
    private Map<@Valid Periode, @Valid TilsynPeriodeInfo> perioderSomSkalSlettes = new TreeMap<>();

    public Tilsynsordning() {
    }

    public Map<Periode, TilsynPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Tilsynsordning medPerioder(Map<Periode, TilsynPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public Tilsynsordning leggeTilPeriode(Periode periode, TilsynPeriodeInfo tilsynPeriodeInfo) {
        this.perioder.put(periode, tilsynPeriodeInfo);
        return this;
    }

    public Map<Periode, TilsynPeriodeInfo> getPerioderSomSkalSlettes() {
        return Collections.unmodifiableMap(perioderSomSkalSlettes);
    }

    public Tilsynsordning medPerioderSomSkalSlettes(Map<Periode, TilsynPeriodeInfo> perioderSomSkalSlettes) {
        this.perioderSomSkalSlettes = (perioderSomSkalSlettes == null) ? new TreeMap<>() : new TreeMap<>(perioderSomSkalSlettes);
        return this;
    }

    @Size(max = 0, message = "${validatedValue}")
    private List<Feil> getValiderPerioder() {
        if (perioder == null)
            List.of();
        return validerOverlappendePerioder(perioder, "tilsynsordning.perioder");
    }
}
