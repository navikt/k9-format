package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import java.util.Objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Arbeidstaker {

    @JsonProperty(value = "norskIdentitetsnummer")
    @Valid
    private NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonProperty(value = "organisasjonsnummer")
    @Valid
    private Organisasjonsnummer organisasjonsnummer;

    @JsonProperty(value = "organisasjonsnavn")
    @Valid
    private String organisasjonsnavn; // brukes ikke ved saksbehandling

    @JsonProperty(value = "arbeidstidInfo", required = true)
    @Valid
    @NotNull
    private ArbeidstidInfo arbeidstidInfo;

    protected boolean erEntydigPåID() {
        return this.norskIdentitetsnummer != null && this.organisasjonsnummer != null;
    }

    protected boolean manglerIkkeID() {
        return (this.norskIdentitetsnummer == null && this.organisasjonsnummer == null);
    }

    public NorskIdentitetsnummer getNorskIdentitetsnummer() {
        return norskIdentitetsnummer;
    }

    public Arbeidstaker medNorskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = Objects.requireNonNull(norskIdentitetsnummer, "norskIdentitetsnummer");
        return this;
    }

    public Organisasjonsnummer getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    public Arbeidstaker medOrganisasjonsnummer(Organisasjonsnummer organisasjonsnummer) {
        this.organisasjonsnummer = Objects.requireNonNull(organisasjonsnummer, "organisasjonsnummer");
        return this;
    }

    public Arbeidstaker medOrganisasjonsnavn(String organisasjonsnavn) {
        this.organisasjonsnavn = organisasjonsnavn;
        return this;
    }

    public String getOrganisasjonsnavn() {
        return organisasjonsnavn;
    }

    public ArbeidstidInfo getArbeidstidInfo() {
        return arbeidstidInfo;
    }

    public Arbeidstaker medArbeidstidInfo(ArbeidstidInfo arbeidstidInfo) {
        this.arbeidstidInfo = Objects.requireNonNull(arbeidstidInfo, "arbeidstidInfo");
        return this;
    }

    @AssertFalse(message = "[ikkeEntydig] Ikke entydig ID på Arbeidsgiver, må oppgi enten norskIdentitetsnummer eller organisasjonsnummer.")
    private boolean isUniquelyIdentified() {
        return erEntydigPåID();
    }
    @AssertFalse(message = "[påkrevd] Mangler ID på Arbeidsgiver, må oppgi en av norskIdentitetsnummer eller organisasjonsnummer.")
    private boolean isIdentified() {
        return manglerIkkeID();
    }
}
