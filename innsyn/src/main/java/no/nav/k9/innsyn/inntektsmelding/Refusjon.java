package no.nav.k9.innsyn.inntektsmelding;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;

import java.time.LocalDate;

public record Refusjon(
        @JsonProperty(value = "refusjonsbeløpMnd")
        @Valid
        Beløp refusjonsbeløpMnd,

        @JsonProperty(value = "fom")
        LocalDate fom
) {
}
