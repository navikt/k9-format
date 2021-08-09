package no.nav.k9.søknad.ytelse.psb.v1;

import static java.util.Collections.unmodifiableMap;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.time.DurationMin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.TidsserieValidator;
import no.nav.k9.søknad.felles.Feil;
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

    @Size(max = 0, message = "${validatedValue}")
    private List<Feil> getValiderPerioder() {
        if (perioder == null)
            List.of();
        return TidsserieValidator.validerOverlappendePerioder(perioder, "uttak.perioder");
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
