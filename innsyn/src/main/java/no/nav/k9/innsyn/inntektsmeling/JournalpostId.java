package no.nav.k9.innsyn.inntektsmeling;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * Journalpostid refererer til journalpost registret i Joark.
 */
public record JournalpostId(
        @JsonValue
        @NotNull
        @Size(max = 50, min = 3)
        @Pattern(regexp = GYLDIG, message = "Saksnummer [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
        String journalpostId // NOSONAR
) implements Serializable {

    private static final String GYLDIG = "^[\\p{Alnum}]+$";
}
