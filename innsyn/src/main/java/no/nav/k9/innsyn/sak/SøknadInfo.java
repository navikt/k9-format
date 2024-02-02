package no.nav.k9.innsyn.sak;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
        ZonedDateTime mottattTidspunkt
) {
}
