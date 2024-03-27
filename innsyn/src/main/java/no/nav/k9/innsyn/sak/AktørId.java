package no.nav.k9.innsyn.sak;


import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AktørId(
        @JsonValue
        @NotNull
        @Size(max = 20)
        @Pattern(regexp = "^\\d+$", message = "AktørId [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
        String id
) {}
