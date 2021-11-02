package no.nav.k9.søknad.ytelse.pls.v1;

import java.time.Duration;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class DelvisFravær {

    @Valid
    @NotNull
    @DurationMax(hours = 24)
    @DurationMin(minutes = 30)
    @JsonProperty(value = "normalarbeidstid", required = true)
    private final Duration normalarbeidstid;

    @Valid
    @NotNull
    @DurationMax(hours = 24)
    @DurationMin(minutes = 30)
    @JsonProperty(value = "fravær", required = true)
    private final Duration fravær;

    @JsonCreator
    public DelvisFravær(
            @JsonProperty("normalarbeidstid") Duration normalarbeidstid,
            @JsonProperty("fravær") Duration fravær) {
        this.normalarbeidstid = normalarbeidstid;
        this.fravær = fravær;
    }

    @AssertTrue(message="[ugyldigArbeidstid] Fravær er større enn jobber normalt")
    private boolean isFraværMindreEnnNormalarbeidstid() {
        return fravær.compareTo(normalarbeidstid) < 0;
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
        return Objects.equals(normalarbeidstid, that.normalarbeidstid) && Objects.equals(fravær, that.fravær);
    }

    @Override
    public int hashCode() {
        return Objects.hash(normalarbeidstid, fravær);
    }

    @Override
    public String toString() {
        return "Arbeidstid{" +
                "normalarbeidstid=" + normalarbeidstid +
                ", fravær=" + fravær +
                '}';
    }
}
