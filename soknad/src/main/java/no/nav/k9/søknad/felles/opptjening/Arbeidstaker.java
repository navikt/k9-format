package no.nav.k9.søknad.felles.opptjening;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)

public class Arbeidstaker {

    @JsonProperty(value = "norskIdentitetsnummer")
    @Valid
    private NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonProperty(value = "organisasjonsnummer")
    @Valid
    private Organisasjonsnummer organisasjonsnummer;

    @JsonCreator
    public Arbeidstaker(@JsonProperty(value = "norskIdentitetsnummer") @Valid NorskIdentitetsnummer norskIdentitetsnummer,
                        @JsonProperty(value = "organisasjonsnummer") @Valid Organisasjonsnummer organisasjonsnummer) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
        this.organisasjonsnummer = organisasjonsnummer;
    }

    public Arbeidstaker() {

    }
    protected boolean erEntydigPåID() {
        return this.norskIdentitetsnummer != null && this.organisasjonsnummer != null;
    }

    protected boolean manglerIkkeID() {
        return (this.norskIdentitetsnummer == null && this.organisasjonsnummer == null);
    }

    public void valider(String felt, List<Feil> feilList) {
        if(erEntydigPåID()) {
            feilList.add(new Feil(felt, "illegalArgument",  "Ikke entydig ID på Arbeidsgiver, må oppgi enten norskIdentitetsnummer eller organisasjonsnummer."));
        }
        if (manglerIkkeID()) {
            feilList.add(new Feil(felt, "illegalArgument",  "Mangler ID på Arbeidsgiver, må oppgi en av norskIdentitetsnummer eller organisasjonsnummer."));
        }
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
}
