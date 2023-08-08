package no.nav.k9.søknad.felles.opptjening;

import java.util.Objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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

    public Periode getPeriode() {
        return periode;
    }

    public AnnenAktivitet medPeriode(Periode periode) {
        this.periode = Objects.requireNonNull(periode, "periode");
        return this;
    }

    public AnnenAktivitetType getAnnenAktivitetType() {
        return annenAktivitetType;
    }

    public AnnenAktivitet medAnnenAktivitetType(AnnenAktivitetType annenAktivitetType) {
        this.annenAktivitetType = Objects.requireNonNull(annenAktivitetType, "annenAktivitetType");
        return this;
    }
}
