package no.nav.k9.innsyn.inntektsmelding;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.felles.type.Periode;

public record Gradering(
        @Valid
        @NotNull
        @JsonProperty(value = "periode", required = true)
        Periode periode,

        @Valid
        @NotNull
        @JsonProperty(value = "arbeidstidProsent", required = true)
        Stillingsprosent arbeidstidProsent
) implements Comparable<Gradering> {

    @Override
    public String toString() {
        return "GraderingEntitet{" +
                "periode=" + periode +
                ", arbeidstidProsent=" + arbeidstidProsent +
                '}';
    }

    @Override
    public int compareTo(Gradering o) {
        return o == null ? 1 : this.periode.compareTo(o.periode);
    }
}
