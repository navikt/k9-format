package no.nav.k9.søknad.pleiepengerbarn;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

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
    private List<SelvstendigNæringsdrivende> selvstendig;

    @JsonCreator
    public Inntekter(@JsonProperty(value = "frilanser") Frilanser frilanser,
                     @JsonProperty(value = "selvstendig") List<SelvstendigNæringsdrivende> selvstendig) {
        if (frilanser == null && (selvstendig == null || selvstendig.isEmpty())) {
            throw new IllegalArgumentException("Må spesifisere enten frilanser eller selvstendig næringsdrivende");
        }
        this.frilanser = frilanser;
        this.selvstendig = selvstendig == null ? null : Collections.unmodifiableList(selvstendig);
    }

    public Frilanser getFrilanser() {
        return frilanser;
    }

    public List<SelvstendigNæringsdrivende> getSelvstendig() {
        return selvstendig;
    }
}
