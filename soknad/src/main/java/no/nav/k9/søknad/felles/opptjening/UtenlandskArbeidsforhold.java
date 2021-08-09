package no.nav.k9.søknad.felles.opptjening;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Landkode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)

public class UtenlandskArbeidsforhold {

    @JsonProperty("ansettelsePeriode")
    @Valid
    @NotNull
    private ÅpenPeriode ansettelsePeriode;

    @JsonProperty(value = "land", required = true)
    @Valid
    @NotNull
    private Landkode land;

    @JsonProperty(value = "arbeidsgiversnavn")
    @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}]+$", message = "'${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
    private String arbeidsgiversnavn;


    @JsonCreator
    public UtenlandskArbeidsforhold(@JsonProperty(value = "ansettelsePeriode") @Valid @NotNull ÅpenPeriode ansettelsePeriode,
                                    @JsonProperty(value = "land") @Valid @NotNull Landkode land,
                                    @JsonProperty(value = "arbeidsgiversnavn") String arbeidsgiversnavn
    ) {
        this.ansettelsePeriode = ansettelsePeriode;
        this.land = land;
        this.arbeidsgiversnavn = arbeidsgiversnavn;
    }

    public UtenlandskArbeidsforhold() {
    }

    public ÅpenPeriode getAnsettelsePeriode() {
        return ansettelsePeriode;
    }

    public void setAnsettelsePeriode(ÅpenPeriode ansettelsePeriode) {
        this.ansettelsePeriode = ansettelsePeriode;
    }

    public Landkode getLand() {
        return land;
    }

    public void setLand(Landkode land) {
        this.land = land;
    }

    public String getArbeidsgiversnavn() {
        return arbeidsgiversnavn;
    }

    public void setArbeidsgiversnavn(String arbeidsgiversnavn) {
        this.arbeidsgiversnavn = arbeidsgiversnavn;
    }
}
