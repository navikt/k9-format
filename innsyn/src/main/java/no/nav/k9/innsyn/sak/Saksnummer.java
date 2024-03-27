package no.nav.k9.innsyn.sak;

import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record Saksnummer(
        @JsonValue
        @NotNull
        @Pattern(regexp = "^\\p{Alnum}+$", message = "Saksnummer [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
        String verdi
) {}
