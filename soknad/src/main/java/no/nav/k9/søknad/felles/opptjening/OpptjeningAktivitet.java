package no.nav.k9.søknad.felles.opptjening;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class OpptjeningAktivitet {

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "selvstendigNæringsdrivende", required = true)
    @Valid
    @NotNull
    private List<@NotNull @Valid SelvstendigNæringsdrivende> selvstendigNæringsdrivende = new ArrayList<>();

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "frilanser")
    @Valid
    private Frilanser frilanser;

    @Valid
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "utenlandskeArbeidsforhold")
    @NotNull
    private List<UtenlandskArbeidsforhold> utenlandskeArbeidsforhold = new ArrayList<>();

    @Valid
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "andreAktiviteter", required = true)
    private List<AnnenAktivitet> andreAktiviteter = new ArrayList<>();

    public OpptjeningAktivitet() {

    }

    @Deprecated
    public OpptjeningAktivitet(@JsonProperty(value = "selvstendigNæringsdrivende") @Valid List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende,
                           @JsonProperty(value = "frilanser") @Valid Frilanser frilanser,
                           @JsonProperty(value = "utenlandskeArbeidsforhold") @Valid List<UtenlandskArbeidsforhold> utenlandskeArbeidsforhold,
                           @JsonProperty(value = "andreAktiviteter") @Valid List<AnnenAktivitet> andreAktiviteter) {
        this.selvstendigNæringsdrivende = unmodifiableList(Objects.requireNonNull(selvstendigNæringsdrivende, "selvstendigNæringsdrivende"));
        this.frilanser = frilanser;
        this.utenlandskeArbeidsforhold = unmodifiableList(Objects.requireNonNull(utenlandskeArbeidsforhold, "utenlandskeArbeidsforhold"));
        this.andreAktiviteter = unmodifiableList(Objects.requireNonNull(andreAktiviteter, "andreAktiviteter"));
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

    public List<AnnenAktivitet> getAndreAktiviteter() {
        return andreAktiviteter;
    }

    public List<UtenlandskArbeidsforhold> getUtenlandskeArbeidsforhold() {
        return utenlandskeArbeidsforhold;
    }

    public OpptjeningAktivitet medSelvstendigNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende) {
        this.selvstendigNæringsdrivende = unmodifiableList(Objects.requireNonNull(selvstendigNæringsdrivende, "selvstendigNæringsdrivende"));
        return this;
    }

    public OpptjeningAktivitet medSelvstendigNæringsdrivende(SelvstendigNæringsdrivende selvstendigNæringsdrivende) {
        Objects.requireNonNull(selvstendigNæringsdrivende, "selvstendigNæringsdrivende");
        this.selvstendigNæringsdrivende = List.of(selvstendigNæringsdrivende);
        return this;
    }

    public OpptjeningAktivitet medFrilanser(Frilanser frilanser) {
        this.frilanser = frilanser;
        return this;
    }

    public OpptjeningAktivitet medUtenlandskArbeidsforhold(UtenlandskArbeidsforhold utenlandskArbeidsforhold) {
        this.utenlandskeArbeidsforhold = unmodifiableList(Objects.requireNonNull(utenlandskeArbeidsforhold, "utenlandskeArbeidsforhold"));
        return this;
    }

    public OpptjeningAktivitet medUtenlandskeArbeidsforhold(List<UtenlandskArbeidsforhold> utenlandskeArbeidsforhold) {
        this.utenlandskeArbeidsforhold = unmodifiableList(Objects.requireNonNull(utenlandskeArbeidsforhold, "utenlandskeArbeidsforhold"));
        return this;
    }

    public OpptjeningAktivitet medAnnenAktivitet(AnnenAktivitet annenAktivitet) {
        this.andreAktiviteter = unmodifiableList(Objects.requireNonNull(andreAktiviteter, "andreAktiviteter"));
        return this;
    }

    public OpptjeningAktivitet medAndreAktiviteter(List<AnnenAktivitet> andreAktiviteter) {
        this.andreAktiviteter = unmodifiableList(Objects.requireNonNull(andreAktiviteter, "andreAktiviteter"));
        return this;
    }

    @Deprecated
    public static final class Builder {
        private List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende = new ArrayList<>();
        private Frilanser frilanser;
        private List<UtenlandskArbeidsforhold> utenlandskeArbeidsforhold = new ArrayList<>();
        private List<AnnenAktivitet> andreAktiviteter = new ArrayList<>();

        private Builder() {
        }

        public Builder selvstendigNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigNæringsdrivende) {
            this.selvstendigNæringsdrivende.addAll(Objects.requireNonNull(selvstendigNæringsdrivende, "selvstendigNæringsdrivende"));
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
            return new OpptjeningAktivitet(selvstendigNæringsdrivende, frilanser, utenlandskeArbeidsforhold, andreAktiviteter);
        }
    }
}
