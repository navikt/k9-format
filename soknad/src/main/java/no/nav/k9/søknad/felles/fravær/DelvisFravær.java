package no.nav.k9.søknad.felles.fravær;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class DelvisFravær {

    @JsonProperty("normalarbeidstid")
    @DurationMin(hours = 0, minutes = 1, message = "[ugyldigVerdi] Må være større enn 0.")
    @DurationMax(hours = 24, message = "[ugyldigVerdi] Må være lavere eller lik 24 timer.")
    @NotNull
    private final Duration normalarbeidstid;

    @JsonProperty("fravær")
    @DurationMin(hours = 0, message = "[ugyldigVerdi] Må være større enn 0.")
    @DurationMax(hours = 24, message = "[ugyldigVerdi] Må være lavere eller lik 24 timer.")
    @NotNull
    private final Duration fravær;

    @JsonCreator
    public DelvisFravær(@JsonProperty("normalarbeidstid") Duration normalarbeidstid,
                        @JsonProperty("fravær") Duration fravær) {
        this.normalarbeidstid = normalarbeidstid;
        this.fravær = fravær;
    }

    public Duration getNormalarbeidstid() {
        return normalarbeidstid;
    }

    public Duration getFravær() {
        return fravær;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DelvisFravær that = (DelvisFravær) o;
        return normalarbeidstid.equals(that.normalarbeidstid) && fravær.equals(that.fravær);
    }

    @Override
    public int hashCode() {
        return Objects.hash(normalarbeidstid, fravær);
    }

    @Override
    public String toString() {
        return "DelvisFravær{" +
                "normalarbeidstid=" + normalarbeidstid +
                ", fravær=" + fravær +
                '}';
    }

    public Duration normalisertTilStandarddag() {
        Duration standardDag = Duration.ofHours(7).plusMinutes(30);

        // normalisert (antall sekunder) = varighetStandarddag * oppgittFravær / oppgittNormalarbeidstid.
        // normalisert (antall halvtimer) = varighetStandarddag * oppgittFravær / oppgittNormalarbeidstid / 1800.  siden det er 1800 sekund i en halvtime
        // normalisert (antall halvtimer) = varighetStandarddag * oppgittFravær / (oppgittNormalarbeidstid * 1800) for å ha kun en avrunding (ved divisjon)

        long normalisertAntallHalvtimer = BigDecimal.valueOf(standardDag.getSeconds() * getFravær().getSeconds())
                .divide(BigDecimal.valueOf(getNormalarbeidstid().getSeconds() * 1800), 0, RoundingMode.UP).longValue();
        long timer = normalisertAntallHalvtimer / 2;
        int minutter = normalisertAntallHalvtimer % 2 == 1 ? 30 : 0;
        return Duration.ofHours(timer).plusMinutes(minutter);
    }
}
