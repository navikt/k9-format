package no.nav.k9.innsyn.inntektsmelding;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Beløp representerer kombinasjon av kroner og øre på standardisert format
 */
public record Beløp(

        @JsonProperty(value = "verdi")
        @Digits(integer = 8, fraction = 4)
        @DecimalMin("-10000000.00")
        @DecimalMax("10000000.00")
        BigDecimal verdi
) {
    private static final RoundingMode AVRUNDINGSMODUS = RoundingMode.HALF_EVEN;

    private BigDecimal skalertVerdi() {
        return verdi == null ? null : verdi.setScale(2, AVRUNDINGSMODUS);
    }

    @Override
    public String toString() {
        return "Beløp{" +
                "verdi=" + verdi +
                ", skalertVerdi=" + skalertVerdi() +
                '}';
    }
}
