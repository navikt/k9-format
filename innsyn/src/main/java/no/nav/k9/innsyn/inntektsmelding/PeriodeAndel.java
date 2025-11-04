package no.nav.k9.innsyn.inntektsmelding;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.felles.type.Periode;

public record PeriodeAndel(

        @JsonProperty(value = "periode", required = true)
        @NotNull
        @Valid
        Periode periode,

        /**
         * antall timer per dag (og minutter). Hvis null antas hel arbeidsdag skal telles.
         */
        @JsonProperty(value = "varighetPerDag")
        @Valid
        Duration varighetPerDag
) {
}
