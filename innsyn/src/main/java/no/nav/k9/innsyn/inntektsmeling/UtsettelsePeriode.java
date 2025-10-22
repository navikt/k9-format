package no.nav.k9.innsyn.inntektsmeling;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.felles.type.Periode;

public record UtsettelsePeriode(
        @Valid
        @NotNull
        @JsonProperty(value = "periode", required = true)
        Periode periode,

        @Valid
        @NotNull
        @JsonProperty(value = "årsak", required = true)
        UtsettelseÅrsak årsak
) {
}
