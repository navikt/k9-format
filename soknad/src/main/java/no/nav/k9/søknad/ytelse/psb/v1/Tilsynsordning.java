package no.nav.k9.søknad.ytelse.psb.v1;

import static java.util.Collections.unmodifiableMap;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Tilsynsordning {

    @JsonProperty(value="perioder", required = true)
    @NotNull
    @Valid
    private Map<@NotNull Periode, @NotNull TilsynPeriodeInfo> perioder = new TreeMap<>();

    public Tilsynsordning() {
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
    public static class TilsynPeriodeInfo {

        @Valid
        @DurationMin(hours = 0, message = "[ugyldigVerdi] Må være større eller lik 0.")
        @DurationMax(hours = 7, minutes = 30, message = "[ugyldigVerdi] Må være lavere eller lik 7 timer 30 minutter.")
        @JsonProperty(value = "etablertTilsynTimerPerDag", required = true)
        private Duration etablertTilsynTimerPerDag;

        public TilsynPeriodeInfo() {
        }

        public Duration getEtablertTilsynTimerPerDag() {
            return etablertTilsynTimerPerDag;
        }

        public TilsynPeriodeInfo medEtablertTilsynTimerPerDag(Duration etablertTilsynTimerPerDag) {
            this.etablertTilsynTimerPerDag = Objects.requireNonNull(etablertTilsynTimerPerDag, "TilsynPeriodeInfo.etablertTilsynTimerPerDag");
            return this;
        }
    }
}
