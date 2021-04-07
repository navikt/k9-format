package no.nav.k9.søknad.felles.fravær;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @JsonProperty(value = "årsak", required = true)
    private final FraværÅrsak årsak;

    @NotNull
    @Size(min = 1, max = 2)
    @JsonProperty(value = "aktivitetFravær", required = true)
    private final List<AktivitetFravær> aktivitetFravær;

    @JsonCreator
    public FraværPeriode(
            @JsonProperty("periode") @Valid Periode periode,
            @JsonProperty("duration") Duration duration,
            @JsonProperty("årsak") FraværÅrsak årsak,
            @JsonProperty("aktivitetFravær") List<AktivitetFravær> aktivitetFravær) {
        this.periode = periode;
        this.duration = duration;
        this.årsak = årsak;
        this.aktivitetFravær = aktivitetFravær.stream().sorted().collect(Collectors.toList()); //sorterer for å få enklere equals/hashcode
    }

    public Periode getPeriode() {
        return periode;
    }

    public Duration getDuration() {
        return duration;
    }

    public FraværÅrsak getÅrsak() {
        return årsak;
    }

    public List<AktivitetFravær> getAktivitetFravær() {
        return aktivitetFravær;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FraværPeriode that = (FraværPeriode) o;
        return periode.equals(that.periode) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(årsak, that.årsak) &&
                Objects.equals(aktivitetFravær, that.aktivitetFravær);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periode, duration, årsak, aktivitetFravær);
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
                ", årsak=" + årsak +
                ", fraværFraAktivitet=" + aktivitetFravær +
                '}';
    }

}
