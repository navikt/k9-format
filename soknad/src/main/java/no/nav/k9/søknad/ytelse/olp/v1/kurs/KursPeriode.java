package no.nav.k9.søknad.ytelse.olp.v1.kurs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class KursPeriode {

    @JsonProperty(value = "periode", required = true)
    @NotNull
    @Valid
    private Periode periode;

    public KursPeriode() {
    }

    public KursPeriode(Periode periode) {
        this.periode = periode;
    }

    public Periode getPeriode() {
        return periode;
    }
}
