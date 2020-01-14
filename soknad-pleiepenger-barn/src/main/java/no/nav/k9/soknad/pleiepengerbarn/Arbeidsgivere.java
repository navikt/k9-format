package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class Arbeidsgivere {
    public final List<Arbeidstaker> arbeidstaker;
    public final List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende;
    public final List<Frilanser> frilanser;

    @JsonCreator
    private Arbeidsgivere(
            @JsonProperty("arbeidstaker")
            List<Arbeidstaker> arbeidstaker,
            @JsonProperty("selvstendigNæringsdrivende")
            List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende,
            @JsonProperty("frilanser")
            List<Frilanser> frilanser) {
        this.arbeidstaker = arbeidstaker == null ? emptyList() : unmodifiableList(arbeidstaker);
        this.selvstendigNæringsdrivende = selvstendigNæringsdrivende == null ? emptyList() : unmodifiableList(selvstendigNæringsdrivende);
        this.frilanser = frilanser == null ? emptyList() : unmodifiableList(frilanser);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<Arbeidstaker> arbeidstaker;
        private List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende;
        private List<Frilanser> frilanser;

        private Builder() {
            arbeidstaker = new ArrayList<>();
            selvstendigNæringsdrivende = new ArrayList<>();
            frilanser = new ArrayList<>();
        }

        public Builder arbeidstaker(List<Arbeidstaker> arbeidstaker) {
            this.arbeidstaker.addAll(arbeidstaker);
            return this;
        }

        public Builder arbeidstaker(Arbeidstaker arbeidstaker) {
            this.arbeidstaker.add(arbeidstaker);
            return this;
        }

        public Builder selvstendigNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende) {
            this.selvstendigNæringsdrivende.addAll(selvstendigNæringsdrivende);
            return this;
        }

        public Builder selvstendigNæringsdrivende(SelvstendigNæringsdrivende selvstendigNæringsdrivende) {
            this.selvstendigNæringsdrivende.add(selvstendigNæringsdrivende);
            return this;
        }

        public Builder frilanser(List<Frilanser> frilanser) {
            this.frilanser.addAll(frilanser);
            return this;
        }

        public Builder frilanser(Frilanser frilanser) {
            this.frilanser.add(frilanser);
            return this;
        }

        public Arbeidsgivere build() {
            return new Arbeidsgivere(
                    arbeidstaker,
                    selvstendigNæringsdrivende,
                    frilanser
            );
        }
    }
}
