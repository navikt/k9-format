package no.nav.k9.innsyn.inntektsmelding;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record Refusjon(
        @JsonProperty(value = "refusjonsbeløpMnd", required = true)
        @NotNull
        @Valid
        Beløp refusjonsbeløpMnd,

        @JsonProperty(value = "fom", required = true)
        @NotNull
        LocalDate fom
) {
}
