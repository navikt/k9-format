package no.nav.k9.oppgave.bekreftelse.ung.inntekt;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.validering.periode.GyldigPeriode;

import java.math.BigDecimal;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class InntektForPeriode implements Comparable<InntektForPeriode> {

    private static final String MIN = "0.00";
    private static final String MAX = "1000000.00";

    private static final BigDecimal MAX_VAL = new BigDecimal(MAX);

    @JsonProperty(value = "beløp", required = true)
    @Valid
    @NotNull
    @DecimalMin(MIN)
    @DecimalMax(MAX)
    @JsonFormat(shape = Shape.STRING)
    private BigDecimal beløp;

    @JsonProperty(value = "periode", required = true)
    @Valid
    @NotNull
    @GyldigPeriode(krevFomDato = true, krevTomDato = true)
    private Periode periode;

    @JsonCreator
    public InntektForPeriode(@JsonProperty(value = "beløp", required = true) BigDecimal beløp,
                             @JsonProperty(value = "periode", required = true) Periode periode) {
        this.beløp = Objects.requireNonNull(beløp, "beløp");
        this.periode = Objects.requireNonNull(periode, "periode");
        if (BigDecimal.ZERO.compareTo(beløp) > 0) {
            throw new IllegalArgumentException("Beløp [" + beløp + "] kan ikke være < 0");
        } else if (MAX_VAL.compareTo(beløp) < 0) {
            throw new IllegalArgumentException("Beløp [" + beløp + "] kan ikke være > " + MAX_VAL);
        }

        if (periode.getFraOgMed() == null || periode.getTilOgMed() == null) {
            throw new IllegalArgumentException("Periode må ha både tom-dato og fom-dato satt");
        }
    }

    public BigDecimal getBeløp() {
        return beløp;
    }

    public Periode getPeriode() {
        return periode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var other = (InntektForPeriode) obj;
        return Objects.equals(beløp, other.beløp) && Objects.equals(periode, other.periode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beløp, periode);
    }

    @Override
    public String toString() {
        return "InntektForPeriode{" +
                "beløp=" + beløp +
                ", periode=" + periode +
                '}';
    }

    @Override
    public int compareTo(InntektForPeriode o) {
        return this.getPeriode().compareTo(o.getPeriode());
    }
}
