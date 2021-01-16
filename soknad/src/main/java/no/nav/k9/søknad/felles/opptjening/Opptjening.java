package no.nav.k9.søknad.felles.opptjening;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.opptjening.snf.Frilanser;
import no.nav.k9.søknad.felles.opptjening.snf.SelvstendigNæringsdrivende;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Opptjening {

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "selvstendigNæringsdrivende")
    @Valid
    private final List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "frilanser")
    @Valid
    private final Frilanser frilanser;

    @JsonCreator
    private Opptjening(@JsonProperty(value = "selvstendigNæringsdrivende") List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende,
                       @JsonProperty(value = "frilanser") Frilanser frilanser) {
        this.selvstendigNæringsdrivende = (selvstendigNæringsdrivende == null) ? emptyList() : unmodifiableList(selvstendigNæringsdrivende);
        this.frilanser = frilanser;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<SelvstendigNæringsdrivende> getSelvstendigNæringsdrivende() {
        return selvstendigNæringsdrivende;
    }

    public Frilanser getFrilanser() {
        return frilanser;
    }

    public static final class Builder {
        private List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende;
        private Frilanser frilanser;

        private Builder() {
            selvstendigNæringsdrivende = new ArrayList<>();
            frilanser = null;
        }

        public Builder selvstendigNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende) {
            this.selvstendigNæringsdrivende.addAll(selvstendigNæringsdrivende);
            return this;
        }

        public Builder selvstendigNæringsdrivende(SelvstendigNæringsdrivende selvstendigNæringsdrivende) {
            this.selvstendigNæringsdrivende.add(selvstendigNæringsdrivende);
            return this;
        }

        public Builder frilanser(Frilanser frilanser) {
            this.frilanser = frilanser;
            return this;
        }

        public Opptjening build() {
            return new Opptjening(selvstendigNæringsdrivende, frilanser);
        }
    }
}
