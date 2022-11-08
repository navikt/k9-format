package no.nav.k9.søknad.felles.opptjening;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Frilanser {

    @JsonProperty(value = "startdato", required = true)
    @NotNull
    private LocalDate startdato;

    @JsonProperty(value = "sluttdato")
    private LocalDate sluttdato;

    public Frilanser() {

    }

    public Frilanser(@JsonProperty("startdato") LocalDate startdato,
                     @JsonProperty("sluttdato") LocalDate sluttdato) {
        this.startdato = startdato;
        this.sluttdato = sluttdato;
    }

    public Frilanser medStartDato(LocalDate startdato) {
        this.startdato = Objects.requireNonNull(startdato, "startdato");
        return this;
    }

    public Frilanser medSluttDato(LocalDate sluttdato) {
        this.sluttdato = Objects.requireNonNull(sluttdato, "sluttdato");
        return this;
    }

    public LocalDate getStartdato() {
        return startdato;
    }
    public LocalDate getSluttdato() { return sluttdato; }

}
