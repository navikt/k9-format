package no.nav.k9.søknad.ytelse.psb.v1.tilsyn;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;
import no.nav.k9.søknad.felles.validering.periode.GyldigePerioderMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Tilsynsordning {

    @JsonProperty(value = "perioder", required = true)
    @NotNull
    @Valid
    @GyldigePerioderMap(krevFomDato = true, krevTomDato = true, payload = {AvbrytendeValideringsfeil.class})
    private Map<@NotNull Periode, @NotNull TilsynPeriodeInfo> perioder = new TreeMap<>();

    public Tilsynsordning() {
    }

    public Tilsynsordning(Tilsynsordning t) {
        this.perioder = new TreeMap<>(t.getPerioder().entrySet()
                .stream()
                .map(e -> Map.entry(
                        e.getKey(),
                        new TilsynPeriodeInfo(e.getValue())
                ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    public Map<Periode, TilsynPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Tilsynsordning medPerioder(Map<Periode, TilsynPeriodeInfo> perioder) {
        this.perioder = Objects.requireNonNull(perioder, "perioder");
        return this;
    }

    public Tilsynsordning leggeTilPeriode(Periode periode, TilsynPeriodeInfo tilsynPeriodeInfo) {
        Objects.requireNonNull(periode, "periode");
        Objects.requireNonNull(tilsynPeriodeInfo, "tilsynPeriodeInfo");
        this.perioder.put(periode, tilsynPeriodeInfo);
        return this;
    }

    public Tilsynsordning leggeTilPeriode(Map<Periode, TilsynPeriodeInfo> perioder) {
        Objects.requireNonNull(perioder, "perioder");
        this.perioder.putAll(perioder);
        return this;
    }

}
