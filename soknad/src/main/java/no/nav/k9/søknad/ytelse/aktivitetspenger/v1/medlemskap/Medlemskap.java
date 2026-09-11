package no.nav.k9.søknad.ytelse.aktivitetspenger.v1.medlemskap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Medlemskap(
        @Valid
        Utenlandsopphold utenlandsopphold
) {

    public Medlemskap {
        if (utenlandsopphold == null) {
            utenlandsopphold = Utenlandsopphold.tomt();
        }
    }

    public static Medlemskap tomt() {
        return new Medlemskap(null);
    }
}
