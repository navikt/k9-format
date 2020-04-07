package no.nav.k9.søknad.pleiepengerbarn;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class PeriodeInntekt {

    private static final String MIN = "0.00";
    private static final String MAX = "10000000.00";

    private static final BigDecimal MAX_VAL = new BigDecimal(MAX);

    @JsonProperty(value = "beløp", required = true)
    @Valid
    @NotNull
    @DecimalMin(MIN)
    @DecimalMax(MAX)
    @JsonFormat(shape = Shape.STRING)
    private BigDecimal beløp;

    @JsonCreator
    public PeriodeInntekt(@JsonProperty(value = "beløp", required = true) BigDecimal beløp) {
        this.beløp = Objects.requireNonNull(beløp, "beløp");
        if (BigDecimal.ZERO.compareTo(beløp) > 0) {
            throw new IllegalArgumentException("Beløp [" + beløp + "] kan ikke være < 0");
        } else if (MAX_VAL.compareTo(beløp) < 0) {
            throw new IllegalArgumentException("Beløp [" + beløp + "] kan ikke være > " + MAX_VAL);
        }
    }

    public BigDecimal getBeløp() {
        return beløp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var other = (PeriodeInntekt) obj;
        return Objects.equals(beløp, other.beløp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beløp);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "<beløp=" + beløp + ">";
    }
}
