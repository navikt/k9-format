package no.nav.k9.innsyn.inntektsmelding;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record Refusjon(
        @JsonProperty(value = "refusjonsbeløpMnd", required = true)
        @Valid
        @NotNull
        Beløp refusjonsbeløpMnd,

        @JsonProperty(value = "fom", required = true)
        @NotNull
        LocalDate fom
) {
}
