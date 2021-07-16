package no.nav.k9.søknad.felles.opptjening;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @Valid
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "arbeidstaker")
    private List<Arbeidstaker> arbeidstaker;

    @Valid
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "utenlandskeArbeidsforhold")
    private List<UtenlandskArbeidsforhold> utenlandskeArbeidsforhold;

    @Valid
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "andreAktiviteter")
    private List<AnnenAktivitet> andreAktiviteter;

    public OpptjeningAktivitet() {
        //
    }

    @JsonCreator
    public OpptjeningAktivitet(@JsonProperty(value = "arbeidstaker") @Valid List<Arbeidstaker> arbeidstaker,
                           @JsonProperty(value = "selvstendigNæringsdrivende") @Valid List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende,
                           @JsonProperty(value = "frilanser") @Valid Frilanser frilanser,
                           @JsonProperty(value = "utenlandskeArbeidsforhold") List<UtenlandskArbeidsforhold> utenlandskeArbeidsforhold,
                           @JsonProperty(value = "utenlandskeArbeidsforhold") List<AnnenAktivitet> andreAktiviteter) {
        this.arbeidstaker = arbeidstaker;
        this.selvstendigNæringsdrivende = (selvstendigNæringsdrivende == null) ? emptyList() : unmodifiableList(selvstendigNæringsdrivende);
        this.frilanser = frilanser;
        this.utenlandskeArbeidsforhold = utenlandskeArbeidsforhold;
        this.andreAktiviteter = andreAktiviteter;
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

    public List<Arbeidstaker> getArbeidstaker() {
        return arbeidstaker;
    }

    public List<AnnenAktivitet> getAndreAktiviteter() {
        return andreAktiviteter;
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

    public OpptjeningAktivitet medArbeidstaker(Arbeidstaker arbeidstaker) {
        if (this.arbeidstaker == null)
            this.arbeidstaker = new ArrayList<>();
        this.arbeidstaker.add(arbeidstaker);
        return this;
    }

    public OpptjeningAktivitet medArbeidstaker(List<Arbeidstaker> arbeidstaker) {
        if (this.arbeidstaker == null)
            this.arbeidstaker = new ArrayList<>();
        this.arbeidstaker.addAll(arbeidstaker);
        return this;
    }

    public List<UtenlandskArbeidsforhold> getUtenlandskeArbeidsforhold() {
        return utenlandskeArbeidsforhold;
    }

    public OpptjeningAktivitet medUtenlandskArbeidsforhold(UtenlandskArbeidsforhold utenlandskArbeidsforhold) {
        if (this.utenlandskeArbeidsforhold == null)
            this.utenlandskeArbeidsforhold = new ArrayList<>();
        this.utenlandskeArbeidsforhold.add(utenlandskArbeidsforhold);
        return this;
    }

    public OpptjeningAktivitet medUtenlandskeArbeidsforhold(List<UtenlandskArbeidsforhold> utenlandskeArbeidsforhold) {
        if (this.utenlandskeArbeidsforhold == null)
            this.utenlandskeArbeidsforhold = new ArrayList<>();
        this.utenlandskeArbeidsforhold.addAll(utenlandskeArbeidsforhold);
        return this;
    }

    @Deprecated
    public static final class Builder {
        private List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende = new ArrayList<>();
        private Frilanser frilanser;
        private List<Arbeidstaker> arbeidstaker = new ArrayList<>();
        private List<UtenlandskArbeidsforhold> utenlandskeArbeidsforhold = new ArrayList<>();
        private List<AnnenAktivitet> andreAktiviteter = new ArrayList<>();

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

        public Builder utenlandskeArbeidsforhold(List<UtenlandskArbeidsforhold> utenlandskeArbeidsforhold) {
            this.utenlandskeArbeidsforhold.addAll(utenlandskeArbeidsforhold);
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

        public Builder andreAktiviteter(List<AnnenAktivitet> andreAktiviteter) {
            this.andreAktiviteter.addAll(andreAktiviteter);
            return this;
        }

        public OpptjeningAktivitet build() {
            return new OpptjeningAktivitet(arbeidstaker, selvstendigNæringsdrivende, frilanser, utenlandskeArbeidsforhold, andreAktiviteter);
        }
    }
}
