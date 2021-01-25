package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class SøknadInfo {

    @Valid
    @JsonProperty(value = "flereOmsorgspersoner")
    private Boolean flereOmsorgspersoner;

    @Valid
    @JsonProperty(value = "relasjonTilBarnet")
    private String relasjonTilBarnet;

    @Valid
    @JsonProperty(value = "samtykketOmsorgForBarnet")
    private Boolean samtykketOmsorgForBarnet;

    @Valid
    @JsonProperty(value = "beskrivelseAvOmsorgsrollen")
    private String beskrivelseAvOmsorgsrollen;

    @JsonCreator
    public SøknadInfo(@JsonProperty(value = "flereOmsorgspersoner") @Valid Boolean flereOmsorgspersoner,
                      @JsonProperty(value = "relasjonTilBarnet") @Valid String relasjonTilBarnet,
                      @JsonProperty(value = "samtykketOmsorgForBarnet") @Valid Boolean samtykketOmsorgForBarnet,
                      @JsonProperty(value = "beskrivelseAvOmsorgsrollen") @Valid String beskrivelseAvOmsorgsrollen) {
        this.flereOmsorgspersoner = flereOmsorgspersoner;
        this.relasjonTilBarnet = relasjonTilBarnet;
        this.samtykketOmsorgForBarnet = samtykketOmsorgForBarnet;
        this.beskrivelseAvOmsorgsrollen = beskrivelseAvOmsorgsrollen;
    }

    public Boolean getFlereOmsorgspersoner() {
        return flereOmsorgspersoner;
    }

    public void setFlereOmsorgspersoner(Boolean flereOmsorgspersoner) {
        this.flereOmsorgspersoner = flereOmsorgspersoner;
    }

    public String getRelasjonTilBarnet() {
        return relasjonTilBarnet;
    }

    public void setRelasjonTilBarnet(String relasjonTilBarnet) {
        this.relasjonTilBarnet = relasjonTilBarnet;
    }

    public Boolean getSamtykketOmsorgForBarnet() {
        return samtykketOmsorgForBarnet;
    }

    public void setSamtykketOmsorgForBarnet(Boolean samtykketOmsorgForBarnet) {
        this.samtykketOmsorgForBarnet = samtykketOmsorgForBarnet;
    }

    public String getBeskrivelseAvOmsorgsrollen() {
        return beskrivelseAvOmsorgsrollen;
    }

    public void setBeskrivelseAvOmsorgsrollen(String beskrivelseAvOmsorgsrollen) {
        this.beskrivelseAvOmsorgsrollen = beskrivelseAvOmsorgsrollen;
    }
}