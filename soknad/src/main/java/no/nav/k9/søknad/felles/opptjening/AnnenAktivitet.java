package no.nav.k9.søknad.felles.opptjening;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.AnnenAktivitetType;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)

public class AnnenAktivitet {

    @JsonProperty("periode")
    @Valid
    @NotNull
    private ÅpenPeriode periode;

    @JsonProperty(value = "annenAktivitetType")
    @Valid
    @NotNull
    private AnnenAktivitetType annenAktivitetType;


    @JsonCreator
    public AnnenAktivitet(@JsonProperty(value = "periode") @Valid @NotNull ÅpenPeriode periode,
                          @JsonProperty(value = "annenAktivitetType") @Valid @NotNull AnnenAktivitetType annenAktivitetType) {
        this.periode = periode;
        this.annenAktivitetType = annenAktivitetType;
    }

    public AnnenAktivitet() {
    }

    public ÅpenPeriode getPeriode() {
        return periode;
    }

    public void setPeriode(ÅpenPeriode periode) {
        this.periode = periode;
    }

    public AnnenAktivitetType getAnnenAktivitetType() {
        return annenAktivitetType;
    }

    public void setAnnenAktivitetType(AnnenAktivitetType annenAktivitetType) {
        this.annenAktivitetType = annenAktivitetType;
    }
}
