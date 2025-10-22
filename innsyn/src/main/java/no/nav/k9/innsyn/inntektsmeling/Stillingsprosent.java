package no.nav.k9.innsyn.inntektsmeling;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Stillingsprosent slik det er oppgitt i arbeidsavtalen.
 */
public record Stillingsprosent(
        @JsonValue
        @DecimalMin("0.00")
        @DecimalMax("500.00")
        BigDecimal verdi
) {

    private static final RoundingMode AVRUNDINGSMODUS = RoundingMode.HALF_EVEN;

    private BigDecimal skalertVerdi() {
        return verdi.setScale(2, AVRUNDINGSMODUS);
    }


    @Override
    public String toString() {
        return "Stillingsprosent{" +
                "verdi=" + verdi +
                ", skalertVerdi=" + skalertVerdi() +
                '}';
    }
}
