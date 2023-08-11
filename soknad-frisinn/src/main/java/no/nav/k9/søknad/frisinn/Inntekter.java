package no.nav.k9.søknad.frisinn;

import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Inntekter {

    @JsonInclude(value = Include.NON_EMPTY)
    @JsonProperty(value = "frilanser")
    @Valid
    private Frilanser frilanser;

    @JsonInclude(value = Include.NON_EMPTY)
    @JsonProperty(value = "selvstendig")
    @Valid
    private SelvstendigNæringsdrivende selvstendig;

    @JsonInclude(value = Include.NON_EMPTY)
    @JsonProperty(value = "arbeidstaker")
    @Valid
    private Arbeidstaker arbeidstaker;

    @JsonCreator
    public Inntekter(@JsonProperty(value = "frilanser") Frilanser frilanser,
                     @JsonProperty(value = "selvstendig") SelvstendigNæringsdrivende selvstendig,
                     @JsonProperty(value = "arbeidstaker") Arbeidstaker arbeidstaker) {
        if (frilanser == null && selvstendig == null) {
            throw new IllegalArgumentException("Må spesifisere enten frilanser eller selvstendig næringsdrivende");
        }
        this.frilanser = frilanser;
        this.selvstendig = selvstendig;
        this.arbeidstaker = arbeidstaker;
    }

    public Frilanser getFrilanser() {
        return frilanser;
    }

    public SelvstendigNæringsdrivende getSelvstendig() {
        return selvstendig;
    }

    public Arbeidstaker getArbeidstaker() {
        return arbeidstaker;
    }

}
