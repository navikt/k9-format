package no.nav.k9.søknad.felles.opptjening;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)

public class UtenlandskArbeidsforhold {

    @JsonProperty("ansettelsePeriode")
    @Valid
    @NotNull
    private Periode ansettelsePeriode;

    @JsonProperty(value = "land", required = true)
    @Valid
    @NotNull
    private Landkode land;

    @JsonProperty(value = "arbeidsgiversnavn", required = true)
    @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}]+$", message = "[ugyldigSyntaks] '${validatedValue}' matcher ikke tillatt pattern '{regexp}'")
    private String arbeidsgiversnavn;

    public UtenlandskArbeidsforhold() {

    }

    @Deprecated
    public UtenlandskArbeidsforhold(@JsonProperty(value = "ansettelsePeriode") @Valid @NotNull Periode ansettelsePeriode,
                                    @JsonProperty(value = "land") @Valid @NotNull Landkode land,
                                    @JsonProperty(value = "arbeidsgiversnavn") String arbeidsgiversnavn
    ) {
        this.ansettelsePeriode = ansettelsePeriode;
        this.land = land;
        this.arbeidsgiversnavn = arbeidsgiversnavn;
    }

    public Periode getAnsettelsePeriode() {
        return ansettelsePeriode;
    }

    public UtenlandskArbeidsforhold medAnsettelsePeriode(Periode ansettelsePeriode) {
        this.ansettelsePeriode = Objects.requireNonNull(ansettelsePeriode, "ansettelsePeriode");
        return this;
    }

    public Landkode getLand() {
        return land;
    }

    public UtenlandskArbeidsforhold medLand(Landkode land) {
        this.land = Objects.requireNonNull(land, "land");
        return this;
    }

    public String getArbeidsgiversnavn() {
        return arbeidsgiversnavn;
    }

    public UtenlandskArbeidsforhold medArbeidsgiversnavn(String arbeidsgiversnavn) {
        this.arbeidsgiversnavn = Objects.requireNonNull(arbeidsgiversnavn, "arbeidsgiversnavn");
        return this;
    }

    @AssertTrue(message = "[ugyldigPeriode] Fom må være satt")
    private boolean isPeriodeFomOK() {
        if (ansettelsePeriode == null) {
            return true;
        }
        return ansettelsePeriode.getFraOgMed() != null;
    }

    @AssertTrue(message = "[ugyldigPeriode] Tom kan ikke være før Fom må være satt")
    private boolean isPeriodeInvers() {
        if (ansettelsePeriode == null ||
                ansettelsePeriode.getFraOgMed() == null ||
                ansettelsePeriode.getTilOgMed() == null) {
            return true;
        }
        return ansettelsePeriode.getFraOgMed().isBefore(ansettelsePeriode.getTilOgMed());
    }


    @AssertTrue(message = "[ugyldigVerdi] Norge kan ikke være en landkode")
    private boolean isLandNotNor() {
        if (land == null) {
            return true;
        }
        return !land.equals(Landkode.NORGE);
    }
}
