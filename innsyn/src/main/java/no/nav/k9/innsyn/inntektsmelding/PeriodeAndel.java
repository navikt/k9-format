package no.nav.k9.innsyn.inntektsmelding;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import no.nav.k9.søknad.felles.type.Periode;

import java.time.Duration;

public record PeriodeAndel(

        @JsonProperty(value = "periode")
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
