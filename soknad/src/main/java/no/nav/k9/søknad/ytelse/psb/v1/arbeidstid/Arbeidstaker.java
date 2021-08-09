package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonProperty(value = "arbeidstidInfo")
    @Valid
    private ArbeidstidInfo arbeidstidInfo;

    @JsonCreator
    public Arbeidstaker(@JsonProperty(value = "norskIdentitetsnummer") @Valid NorskIdentitetsnummer norskIdentitetsnummer,
                        @JsonProperty(value = "organisasjonsnummer") @Valid Organisasjonsnummer organisasjonsnummer,
                        @JsonProperty(value = "arbeidstidInfo") @Valid ArbeidstidInfo arbeidstidInfo) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
        this.organisasjonsnummer = organisasjonsnummer;
        this.arbeidstidInfo = arbeidstidInfo;
    }

    public Arbeidstaker() {

    }

    protected boolean erEntydigPåID() {
        return this.norskIdentitetsnummer != null && this.organisasjonsnummer != null;
    }

    protected boolean manglerIkkeID() {
        return (this.norskIdentitetsnummer == null && this.organisasjonsnummer == null);
    }

    @AssertFalse(message = "Ikke entydig ID på Arbeidsgiver, må oppgi enten norskIdentitetsnummer eller organisasjonsnummer.")
    private boolean isUniquelyIdentified() {
        return erEntydigPåID();
    }

    @AssertFalse(message = "Mangler ID på Arbeidsgiver, må oppgi en av norskIdentitetsnummer eller organisasjonsnummer.")
    private boolean isIdentified() {
        return manglerIkkeID();
    }

    public NorskIdentitetsnummer getNorskIdentitetsnummer() {
        return norskIdentitetsnummer;
    }

    public void setNorskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
    }

    public Organisasjonsnummer getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    public void setOrganisasjonsnummer(Organisasjonsnummer organisasjonsnummer) {
        this.organisasjonsnummer = organisasjonsnummer;
    }

    public ArbeidstidInfo getArbeidstidInfo() {
        return arbeidstidInfo;
    }

    public void setArbeidstidInfo(ArbeidstidInfo arbeidstidInfo) {
        this.arbeidstidInfo = arbeidstidInfo;
    }
}
