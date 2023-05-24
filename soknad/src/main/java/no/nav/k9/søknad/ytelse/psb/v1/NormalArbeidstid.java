package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;

public class NormalArbeidstid {
    @JsonProperty(value = "timerPerUke", required = true)
    @DurationMin(hours = 0, minutes = 1, message = "[ugyldigVerdi] Må være større enn 0.")
    @DurationMax(hours = 40, message = "[ugyldigVerdi] Må være lavere eller lik 40 timer.")
    @NotNull
    @Valid
    private Duration timerPerUke;

    public NormalArbeidstid(@JsonProperty(value = "timerPerUke", required = true) @Valid Duration timerPerUke) {
        this.timerPerUke = timerPerUke;
    }

    public NormalArbeidstid() {
    }

    public Duration getTimerPerUke() {
        return timerPerUke;
    }

    public NormalArbeidstid medTimerPerUke(Duration timerPerUke) {
        this.timerPerUke = timerPerUke;
        return this;
    }
}
