package no.nav.k9.søknad.ytelse.psb.v1;

import static java.util.Collections.unmodifiableMap;

import java.time.Duration;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.time.DurationMin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Uttak {

    @Valid
    @JsonProperty(value = "perioder")
    private Map<@Valid LukketPeriode, @Valid UttakPeriodeInfo> perioder = new TreeMap<>();

    @JsonProperty(value="perioderSomSkalSlettes")
    @Valid
    private Map<@Valid LukketPeriode, @Valid UttakPeriodeInfo> perioderSomSkalSlettes = new TreeMap<>();

    public Uttak() {
    }

    public Map<LukketPeriode, UttakPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Uttak medPerioder(Map<LukketPeriode, UttakPeriodeInfo> perioder) {
        this.perioder = (perioder == null ) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public Uttak leggeTilPeriode(LukketPeriode periode, UttakPeriodeInfo uttakPeriodeInfo) {
        this.perioder.put(periode, uttakPeriodeInfo);
        return this;
    }

    public Map<LukketPeriode, UttakPeriodeInfo> getPerioderSomSkalSlettes() {
        return unmodifiableMap(perioderSomSkalSlettes);
    }

    public Uttak medPerioderSomSkalSlettes(Map<LukketPeriode, UttakPeriodeInfo> perioderSomSkalSlettes) {
        this.perioderSomSkalSlettes = (perioderSomSkalSlettes == null) ? new TreeMap<>() : new TreeMap<>(perioderSomSkalSlettes);
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
    public static final class UttakPeriodeInfo {

        @Valid
        @NotNull
        @DurationMin
        @JsonProperty(value = "timerPleieAvBarnetPerDag", required = true)
        private Duration timerPleieAvBarnetPerDag;

        @JsonCreator
        public UttakPeriodeInfo(
                @JsonProperty(value = "timerPleieAvBarnetPerDag", required = true) @Valid @NotNull Duration timerPleieAvBarnetPerDag) {
            this.timerPleieAvBarnetPerDag = timerPleieAvBarnetPerDag;
        }

        public UttakPeriodeInfo() {
        }

        public Duration getTimerPleieAvBarnetPerDag() {
            return timerPleieAvBarnetPerDag;
        }

        public UttakPeriodeInfo setTimerPleieAvBarnetPerDag(Duration timerPleieAvBarnetPerDag) {
            this.timerPleieAvBarnetPerDag = timerPleieAvBarnetPerDag;
            return this;
        }
    }
}
