package no.nav.k9.søknad.felles.fravær;

import java.time.Duration;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class FraværPeriode implements Comparable<FraværPeriode> {

    @Valid
    @JsonProperty("periode")
    private final Periode periode;
    @JsonProperty("duration")
    private final Duration duration;

    @NotNull
    @JsonProperty(value = "fraværÅrsak", required = true)
    private final FraværÅrsak fraværÅrsak;

    @JsonCreator
    public FraværPeriode(
            @JsonProperty("periode") @Valid Periode periode,
            @JsonProperty("duration") Duration duration,
            @JsonProperty("fraværÅrsak") FraværÅrsak fraværÅrsak
    ) {
        this.periode = periode;
        this.duration = duration;
        this.fraværÅrsak = fraværÅrsak;
    }

    public Periode getPeriode() {
        return periode;
    }

    public Duration getDuration() {
        return duration;
    }

    public FraværÅrsak getFraværÅrsak() { return fraværÅrsak; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FraværPeriode that = (FraværPeriode) o;
        return periode.equals(that.periode) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(fraværÅrsak, that.fraværÅrsak);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periode, duration, fraværÅrsak);
    }

    @Override
    public int compareTo(FraværPeriode b) {
        return this.getPeriode().compareTo(b.getPeriode());
    }

    @Override
    public String toString() {
        return "FraværPeriode{" +
                "periode=" + periode +
                ", duration=" + duration +
                ", fraværÅrsak=" + fraværÅrsak +
                '}';
    }
}
