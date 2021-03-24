package no.nav.k9.søknad.felles.opptjening.arbeidstaker;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.opptjening.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)

public class PsbArbeidstaker extends Arbeidstaker{

    @JsonProperty(value = "arbeidstidInfo")
    @Valid
    private ArbeidstidInfo arbeidstidInfo;

    @JsonCreator
    public PsbArbeidstaker(@JsonProperty(value = "norskIdentitetsnummer") @Valid NorskIdentitetsnummer norskIdentitetsnummer,
                           @JsonProperty(value = "organisasjonsnummer") @Valid Organisasjonsnummer organisasjonsnummer,
                           @JsonProperty(value = "arbeidstidInfo") @Valid ArbeidstidInfo arbeidstidInfo) {
        setNorskIdentitetsnummer(norskIdentitetsnummer);
        setOrganisasjonsnummer(organisasjonsnummer);
        this.arbeidstidInfo = arbeidstidInfo;
    }

    public PsbArbeidstaker() {

    }

    public void valider(String felt, List<Feil> feilList) {
        if(erEntydigPåID()) {
            feilList.add(new Feil(felt, "illegalArgument",  "Ikke entydig ID på Arbeidsgiver, må oppgi enten norskIdentitetsnummer eller organisasjonsnummer."));
        }
        if (manglerIkkeID()) {
            feilList.add(new Feil(felt, "illegalArgument",  "Mangler ID på Arbeidsgiver, må oppgi en av norskIdentitetsnummer eller organisasjonsnummer."));
        }
    }

    public ArbeidstidInfo getArbeidstidInfo() {
        return arbeidstidInfo;
    }

    public void setArbeidstidInfo(ArbeidstidInfo arbeidstidInfo) {
        this.arbeidstidInfo = arbeidstidInfo;
    }
}
