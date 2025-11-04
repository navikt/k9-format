package no.nav.k9.innsyn.inntektsmelding;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.felles.type.Periode;

public record NaturalYtelse(
        @Valid
        @JsonProperty(value = "periode")
        Periode periode,

        @Valid
        @JsonProperty(value = "beloepPerMnd", required = true)
        @NotNull
        Beløp beloepPerMnd,

        @Valid
        @JsonProperty(value = "type", required = true)
        @NotNull
        NaturalYtelseType type
) {
}
