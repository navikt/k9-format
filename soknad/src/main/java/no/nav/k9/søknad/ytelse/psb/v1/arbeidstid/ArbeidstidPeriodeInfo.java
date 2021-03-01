package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.ytelse.psb.v1.AlltidPositivDuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class ArbeidstidPeriodeInfo {

    @Valid
    @NotNull
    @JsonProperty(value = "faktiskArbeidTimerPerDag")
    private AlltidPositivDuration faktiskArbeidTimerPerDag;

    @JsonCreator
    public ArbeidstidPeriodeInfo(
            @JsonProperty(value = "faktiskArbeidTimerPerDag") @Valid @NotNull Duration faktiskArbeidTimerPerDag) {
        this.faktiskArbeidTimerPerDag = new AlltidPositivDuration(faktiskArbeidTimerPerDag);
    }

    public ArbeidstidPeriodeInfo() {
    }

    public Duration getFaktiskArbeidTimerPerDag() {
        return faktiskArbeidTimerPerDag.getDuration();
    }

    public ArbeidstidPeriodeInfo medFaktiskArbeidTimerPerDag(Duration faktiskArbeidTimerPerDag) {
        this.faktiskArbeidTimerPerDag = new AlltidPositivDuration(faktiskArbeidTimerPerDag);
        return this;
    }
}
