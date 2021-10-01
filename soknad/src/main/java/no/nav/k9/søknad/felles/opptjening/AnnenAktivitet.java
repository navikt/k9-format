package no.nav.k9.søknad.felles.opptjening;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.AnnenAktivitetType;
import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class AnnenAktivitet {

    @JsonProperty(value = "periode", required = true)
    @Valid
    @NotNull
    private Periode periode;

    @JsonProperty(value = "annenAktivitetType", required = true)
    @Valid
    @NotNull
    private AnnenAktivitetType annenAktivitetType;


    @JsonCreator
    public AnnenAktivitet(@JsonProperty(value = "periode", required = true) @Valid @NotNull Periode periode,
                          @JsonProperty(value = "annenAktivitetType", required = true) @Valid @NotNull AnnenAktivitetType annenAktivitetType) {
        this.periode = periode;
        this.annenAktivitetType = annenAktivitetType;
    }

    public AnnenAktivitet() {
    }

    public Periode getPeriode() {
        return periode;
    }

    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    public AnnenAktivitetType getAnnenAktivitetType() {
        return annenAktivitetType;
    }

    public void setAnnenAktivitetType(AnnenAktivitetType annenAktivitetType) {
        this.annenAktivitetType = annenAktivitetType;
    }
}
