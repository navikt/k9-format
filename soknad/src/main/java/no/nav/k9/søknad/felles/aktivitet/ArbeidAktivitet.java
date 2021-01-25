package no.nav.k9.søknad.felles.aktivitet;

import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class ArbeidAktivitet {

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "selvstendigNæringsdrivende")
    @Valid
    private final List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "frilanser")
    @Valid
    private final Frilanser frilanser;

    @Valid
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "arbeidstaker")
    private List<Arbeidstaker> arbeidstaker;

    @JsonCreator
    private ArbeidAktivitet(@JsonProperty(value = "arbeidstaker") @Valid List<Arbeidstaker> arbeidstaker,
                      @JsonProperty(value = "selvstendigNæringsdrivende") List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende,
                      @JsonProperty(value = "frilanser") Frilanser frilanser) {
        this.arbeidstaker = arbeidstaker;
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

    public List<Arbeidstaker> getArbeidstaker() {
        return arbeidstaker;
    }


    public static final class Builder {
        private List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende = new ArrayList<>();
        private Frilanser frilanser;
        private List<Arbeidstaker> arbeidstaker = new ArrayList<>();

        private Builder() {
        }

        public Builder selvstendigNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende) {
            this.selvstendigNæringsdrivende.addAll(selvstendigNæringsdrivende);
            return this;
        }

        public Builder arbeidstaker(Arbeidstaker arbeidstaker) {
            this.arbeidstaker.add(arbeidstaker);
            return this;
        }

        public Builder arbeidstaker(List<Arbeidstaker> arbeidstaker) {
            this.arbeidstaker.addAll(arbeidstaker);
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

        public ArbeidAktivitet build() {
            return new ArbeidAktivitet(arbeidstaker, selvstendigNæringsdrivende, frilanser);
        }
    }
}
