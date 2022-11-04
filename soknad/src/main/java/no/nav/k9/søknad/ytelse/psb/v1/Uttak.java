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
import no.nav.k9.søknad.felles.type.validering.GyldigePerioderMap;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Uttak {

    @Valid
    @NotNull
    @GyldigePerioderMap
    @JsonProperty(value = "perioder")
    private Map<@NotNull Periode, @NotNull UttakPeriodeInfo> perioder = new TreeMap<>();

    public Uttak() {
    }

    public Map<Periode, UttakPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Uttak medPerioder(Map<Periode, UttakPeriodeInfo> perioder) {
        this.perioder = Objects.requireNonNull(perioder, "perioder");
        return this;
    }

    public Uttak leggeTilPeriode(Periode periode, UttakPeriodeInfo uttakPeriodeInfo) {
        Objects.requireNonNull(periode, "periode");
        Objects.requireNonNull(uttakPeriodeInfo, "uttakPeriodeInfo");
        this.perioder.put(periode, uttakPeriodeInfo);
        return this;
    }

    public Uttak leggeTilPeriode(Map<Periode, UttakPeriodeInfo> perioder) {
        Objects.requireNonNull(perioder, "perioder");
        this.perioder.putAll(perioder);
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
    public static class UttakPeriodeInfo {

        @Valid
        @NotNull
        @DurationMin(hours = 0, message = "[ugyldigVerdi] Må være større eller lik 0.")
        @DurationMax(hours = 7, minutes = 30, message = "[ugyldigVerdi] Må være lavere eller lik 7 timer 30 minutter.")
        @JsonProperty(value = "timerPleieAvBarnetPerDag", required = true)
        private Duration timerPleieAvBarnetPerDag;

        public UttakPeriodeInfo(Duration timerPleieAvBarnetPerDag) {
            this.timerPleieAvBarnetPerDag = Objects.requireNonNull(timerPleieAvBarnetPerDag, "timerPleieAvBarnetPerDag");
        }

        public UttakPeriodeInfo() {
        }

        public Duration getTimerPleieAvBarnetPerDag() {
            return timerPleieAvBarnetPerDag;
        }

        public UttakPeriodeInfo medTimerPleieAvBarnetPerDag(Duration timerPleieAvBarnetPerDag) {
            this.timerPleieAvBarnetPerDag = Objects.requireNonNull(timerPleieAvBarnetPerDag, "timerPleieAvBarnetPerDag");
            return this;
        }
    }
}
