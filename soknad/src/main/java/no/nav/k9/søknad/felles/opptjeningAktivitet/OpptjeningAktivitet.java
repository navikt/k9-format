package no.nav.k9.søknad.felles.opptjeningAktivitet;

import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class OpptjeningAktivitet {

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "selvstendigNæringsdrivende")
    @Valid
    private List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "frilanser")
    @Valid
    private Frilanser frilanser;

    public OpptjeningAktivitet() {
        //
    }

    @JsonCreator
    public OpptjeningAktivitet(@JsonProperty(value = "selvstendigNæringsdrivende") List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende,
                            @JsonProperty(value = "frilanser") Frilanser frilanser) {
        this.selvstendigNæringsdrivende = (selvstendigNæringsdrivende == null) ? emptyList() : unmodifiableList(selvstendigNæringsdrivende);
        this.frilanser = frilanser;
    }

    @Deprecated
    public static Builder builder() {
        return new Builder();
    }

    public List<SelvstendigNæringsdrivende> getSelvstendigNæringsdrivende() {
        return selvstendigNæringsdrivende;
    }

    public Frilanser getFrilanser() {
        return frilanser;
    }

    public OpptjeningAktivitet medSelvstendigNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende) {
        if (this.selvstendigNæringsdrivende == null)
            this.selvstendigNæringsdrivende = new ArrayList<>();
        this.selvstendigNæringsdrivende.addAll(selvstendigNæringsdrivende);
        return this;
    }

    public OpptjeningAktivitet medSelvstendigNæringsdrivende(SelvstendigNæringsdrivende selvstendigNæringsdrivende) {
        if (this.selvstendigNæringsdrivende == null)
            this.selvstendigNæringsdrivende = new ArrayList<>();
        this.selvstendigNæringsdrivende.add(selvstendigNæringsdrivende);
        return this;
    }

    public OpptjeningAktivitet medFrilanser(Frilanser frilanser) {
        this.frilanser = frilanser;
        return this;
    }

    @Deprecated
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

        public OpptjeningAktivitet build() {
            return new OpptjeningAktivitet(selvstendigNæringsdrivende, frilanser);
        }
    }
}
