package no.nav.k9.innsyn.inntektsmelding;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.felles.type.Periode;

public record UtsettelsePeriode(
        @JsonProperty(value = "periode", required = true)
        @Valid
        @NotNull
        Periode periode,

        @JsonProperty(value = "årsak", required = true)
        @Valid
        @NotNull
        UtsettelseÅrsak årsak
) {
}
