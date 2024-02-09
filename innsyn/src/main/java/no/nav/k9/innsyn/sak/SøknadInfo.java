package no.nav.k9.innsyn.sak;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.søknad.felles.Kildesystem;

public record SøknadInfo(

        @JsonProperty(value = "status", required = true)
        @Valid
        @NotNull
        SøknadStatus status,

        @JsonProperty(value = "journalpostId", required = true)
        @Valid
        @NotNull
        @Size(max = 50, min = 3)
        @Pattern(regexp = "^[\\\\p{Alnum}]+$", message = "journalpostId [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
        String journalpostId,

        @JsonProperty(value = "mottattTidspunkt", required = true)
        @Valid
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
        ZonedDateTime mottattTidspunkt,

        @Valid
        @JsonProperty(value = "kildesystem")
        Kildesystem kildesystem

) {
}
