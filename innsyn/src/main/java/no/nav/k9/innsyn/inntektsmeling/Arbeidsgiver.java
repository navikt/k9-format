package no.nav.k9.innsyn.inntektsmeling;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.innsyn.sak.AktørId;

/**
 * En arbeidsgiver (enten virksomhet eller personlig arbeidsgiver).
 */
public record Arbeidsgiver(
        /**
         * Kun en av denne og {@link #arbeidsgiverAktørId} kan være satt. Sett denne
         * hvis Arbeidsgiver er en Organisasjon.
         */
        @JsonProperty(value = "arbeidsgiverOrgnr")
        @Valid
        @Size(max = 20)
        @Pattern(regexp = "^\\d+$", message = "[${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
        String arbeidsgiverOrgnr,

        /**
         * Kun en av denne og {@link #virksomhet} kan være satt. Sett denne hvis
         * Arbeidsgiver er en Enkelt person.
         */
        @JsonProperty(value = "arbeidsgiverAktørId")
        @Valid
        @Size(max = 20)
        @Pattern(regexp = "^\\d+$", message = "[${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
        String arbeidsgiverAktørId
) {

    public static Arbeidsgiver person(AktørId arbeidsgiverAktørId) {
        return new Arbeidsgiver(null, arbeidsgiverAktørId.id());
    }

    @Override
    public String toString() {
        return toString(this);
    }

    private static String toString(Arbeidsgiver arb) {
        // litt maskering for feilsøking nå
        if (arb.arbeidsgiverOrgnr != null) {
            return "Virksomhet<" + masker(arb.arbeidsgiverOrgnr) + ">";
        } else {
            return "PersonligArbeidsgiver<"
                    + arb.arbeidsgiverAktørId.substring(0, Math.min(arb.arbeidsgiverAktørId.length(), 3)) + "...>";
        }
    }

    private static String masker(String identifikator) {
        return identifikator.substring(0, Math.min(identifikator.length(), 3))
                + "...";
    }
}
