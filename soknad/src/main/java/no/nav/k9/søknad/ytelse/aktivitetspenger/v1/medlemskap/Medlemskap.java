package no.nav.k9.søknad.ytelse.aktivitetspenger.v1.medlemskap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Medlemskap {

    @Valid
    @JsonProperty(value = "bostederUtenforNorge", required = true)
    @JsonInclude(value = Include.ALWAYS)
    private Bosteder bostederUtenforNorge = new Bosteder();

    @Valid
    @JsonProperty(value = "arbeidsstederUtenforNorge", required = true)
    @JsonInclude(value = Include.ALWAYS)
    private Arbeidssteder arbeidsstederUtenforNorge = new Arbeidssteder();

    public Bosteder getBostederUtenforNorge() {
        return bostederUtenforNorge;
    }

    public Medlemskap medBostederUtenforNorge(Bosteder bostederUtenforNorge) {
        this.bostederUtenforNorge = Objects.requireNonNull(bostederUtenforNorge, "bostederUtenforNorge");
        return this;
    }

    public Arbeidssteder getArbeidsstederUtenforNorge() {
        return arbeidsstederUtenforNorge;
    }

    public Medlemskap medArbeidsstederUtenforNorge(Arbeidssteder arbeidssteder) {
        this.arbeidsstederUtenforNorge = Objects.requireNonNull(arbeidssteder, "arbeidsstederUtenforNorge");
        return this;
    }
}
