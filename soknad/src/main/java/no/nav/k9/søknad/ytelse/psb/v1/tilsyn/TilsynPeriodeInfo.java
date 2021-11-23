package no.nav.k9.søknad.ytelse.psb.v1.tilsyn;

import java.time.Duration;
import java.util.Objects;

import javax.validation.Valid;

import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class TilsynPeriodeInfo {

    @Valid
    @DurationMin(hours = 0, message = "[ugyldigVerdi] Må være større eller lik 0.")
    @DurationMax(hours = 7, minutes = 30, message = "[ugyldigVerdi] Må være lavere eller lik 7 timer 30 minutter.")
    @JsonProperty(value = "etablertTilsynTimerPerDag", required = true)
    private Duration etablertTilsynTimerPerDag;

    public TilsynPeriodeInfo() {
    }
    
    public TilsynPeriodeInfo(TilsynPeriodeInfo t) {
        this.etablertTilsynTimerPerDag = t.getEtablertTilsynTimerPerDag();
    }

    public Duration getEtablertTilsynTimerPerDag() {
        return etablertTilsynTimerPerDag;
    }

    public TilsynPeriodeInfo medEtablertTilsynTimerPerDag(Duration etablertTilsynTimerPerDag) {
        this.etablertTilsynTimerPerDag = Objects.requireNonNull(etablertTilsynTimerPerDag, "TilsynPeriodeInfo.etablertTilsynTimerPerDag");
        return this;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(etablertTilsynTimerPerDag);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TilsynPeriodeInfo other = (TilsynPeriodeInfo) obj;
        return Objects.equals(etablertTilsynTimerPerDag, other.etablertTilsynTimerPerDag);
    }

    @Override
    public String toString() {
        return "TilsynPeriodeInfo [etablertTilsynTimerPerDag=" + etablertTilsynTimerPerDag + "]";
    }
}
