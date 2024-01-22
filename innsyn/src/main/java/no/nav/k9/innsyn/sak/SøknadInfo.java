package no.nav.k9.innsyn.sak;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public record SøknadInfo(

        @JsonProperty(value = "status", required = true)
        @Valid
        @NotNull
        SøknadStatus status,

        @JsonProperty(value = "søknadId", required = true)
        @Valid
        @NotNull
        String søknadId,

        @JsonProperty(value = "mottattTidspunkt", required = true)
        @Valid
        @NotNull
        ZonedDateTime mottattTidspunkt
) {
}
