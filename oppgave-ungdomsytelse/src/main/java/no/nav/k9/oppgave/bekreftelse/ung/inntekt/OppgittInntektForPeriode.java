package no.nav.k9.oppgave.bekreftelse.ung.inntekt;

import com.fasterxml.jackson.annotation.*;
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
public class OppgittInntektForPeriode implements Comparable<OppgittInntektForPeriode> {

    private static final String MIN = "0.00";
    private static final String MAX = "10000000.00";

    @JsonProperty(value = "periode", required = true)
    @Valid
    @NotNull
    @GyldigPeriode(krevFomDato = true, krevTomDato = true)
    private Periode periode;

    @JsonProperty(value = "arbeidstakerOgFrilansInntekt", required = false)
    @Valid
    @DecimalMin(MIN)
    @DecimalMax(MAX)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal arbeidstakerOgFrilansInntekt;

    @JsonProperty(value = "ytelse", required = false)
    @Valid
    @DecimalMin(MIN)
    @DecimalMax(MAX)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal ytelse;


    @JsonCreator
    public OppgittInntektForPeriode(@JsonProperty(value = "periode") Periode periode, @JsonProperty(value = "arbeidstakerOgFrilansInntekt") BigDecimal arbeidstakerOgFrilansInntekt,
                                    @JsonProperty(value = "ytelse") BigDecimal ytelse) {
        this.arbeidstakerOgFrilansInntekt = arbeidstakerOgFrilansInntekt;
        this.ytelse = ytelse;
        this.periode = periode;
    }

    public static Builder builder(Periode periode) {
        return new Builder(periode);
    }

    public Periode getPeriode() {
        return periode;
    }

    public BigDecimal getArbeidstakerOgFrilansInntekt() {
        return arbeidstakerOgFrilansInntekt;
    }

    public BigDecimal getYtelse() {
        return ytelse;
    }

    @Override
    public int compareTo(OppgittInntektForPeriode o) {
        return this.periode.compareTo(o.periode);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OppgittInntektForPeriode that = (OppgittInntektForPeriode) o;
        return Objects.equals(periode, that.periode) &&
                Objects.equals(arbeidstakerOgFrilansInntekt, that.arbeidstakerOgFrilansInntekt) &&
                Objects.equals(ytelse, that.ytelse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periode, arbeidstakerOgFrilansInntekt, ytelse);
    }

    public static final class Builder {
        private BigDecimal arbeidstakerOgFrilansInntekt;
        private BigDecimal ytelse;
        private Periode periode;

        private Builder(Periode periode) {
            this.periode = periode;
        }

        public Builder medArbeidstakerOgFrilansinntekt(BigDecimal inntekt) {
            this.arbeidstakerOgFrilansInntekt = inntekt;
            return this;
        }

        public Builder medYtelse(BigDecimal inntekt) {
            this.ytelse = inntekt;
            return this;
        }

        public OppgittInntektForPeriode build() {
            return new OppgittInntektForPeriode(periode, arbeidstakerOgFrilansInntekt, ytelse);
        }
    }

}
