package no.nav.k9.søknad.felles.opptjening;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class OpptjeningAktivitet {

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "selvstendigNæringsdrivende", required = true)
    @NotNull
    private List<@NotNull @Valid SelvstendigNæringsdrivende> selvstendigNæringsdrivende = new ArrayList<>();

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "frilanser")
    @Valid
    private Frilanser frilanser;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "utenlandskeArbeidsforhold")
    @NotNull
    private List<@Valid UtenlandskArbeidsforhold> utenlandskeArbeidsforhold = new ArrayList<>();

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "andreAktiviteter", required = true)
    private List<@Valid AnnenAktivitet> andreAktiviteter = new ArrayList<>();

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

    public OpptjeningAktivitet medUtenlandskeArbeidsforhold(List<UtenlandskArbeidsforhold> utenlandskeArbeidsforhold) {
        this.utenlandskeArbeidsforhold = unmodifiableList(Objects.requireNonNull(utenlandskeArbeidsforhold, "utenlandskeArbeidsforhold"));
        return this;
    }

    public OpptjeningAktivitet medAndreAktiviteter(List<AnnenAktivitet> andreAktiviteter) {
        this.andreAktiviteter = unmodifiableList(Objects.requireNonNull(andreAktiviteter, "andreAktiviteter"));
        return this;
    }

}
