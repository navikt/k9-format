package no.nav.k9.søknad.felles.opptjeningAktivitet.arbeidstaker;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.k9.søknad.felles.opptjeningAktivitet.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(PsbArbeidstaker.class)
})
public abstract class Arbeidstaker {

    @JsonProperty(value = "norskIdentitetsnummer")
    @Valid
    private NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonProperty(value = "organisasjonsnummer")
    @Valid
    private Organisasjonsnummer organisasjonsnummer;

    protected boolean erEntydigPåID() {
        return this.norskIdentitetsnummer != null && this.organisasjonsnummer != null;
    }

    protected boolean manglerIkkeID() {
        return (this.norskIdentitetsnummer == null && this.organisasjonsnummer == null);
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
