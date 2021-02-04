package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Omsorg {

    @JsonProperty(value = "relasjonTilBarnet")
    @Valid
    private String relasjonTilBarnet;

    @JsonProperty(value = "samtykketOmsorgForBarnet")
    @Valid
    private Boolean samtykketOmsorgForBarnet;

    @JsonProperty(value = "beskrivelseAvOmsorgsrollen")
    @Valid
    private String beskrivelseAvOmsorgsrollen;

    @JsonCreator
    public Omsorg(@JsonProperty(value = "relasjonTilBarnet") @Valid String relasjonTilBarnet,
                  @JsonProperty(value = "samtykketOmsorgForBarnet") @Valid Boolean samtykketOmsorgForBarnet,
                  @JsonProperty(value = "beskrivelseAvOmsorgsrollen") @Valid String beskrivelseAvOmsorgsrollen) {
        this.relasjonTilBarnet = relasjonTilBarnet;
        this.samtykketOmsorgForBarnet = samtykketOmsorgForBarnet;
        this.beskrivelseAvOmsorgsrollen = beskrivelseAvOmsorgsrollen;
    }

    public Omsorg() {
    }

    public String getRelasjonTilBarnet() {
        return relasjonTilBarnet;
    }

    public Omsorg medRelasjonTilBarnet(String relasjonTilBarnet) {
        this.relasjonTilBarnet = relasjonTilBarnet;
        return this;
    }

    public Boolean getSamtykketOmsorgForBarnet() {
        return samtykketOmsorgForBarnet;
    }

    public Omsorg medSamtykketOmsorgForBarnet(Boolean samtykketOmsorgForBarnet) {
        this.samtykketOmsorgForBarnet = samtykketOmsorgForBarnet;
        return this;
    }

    public String getBeskrivelseAvOmsorgsrollen() {
        return beskrivelseAvOmsorgsrollen;
    }

    public Omsorg medBeskrivelseAvOmsorgsrollen(String beskrivelseAvOmsorgsrollen) {
        this.beskrivelseAvOmsorgsrollen = beskrivelseAvOmsorgsrollen;
        return this;
    }
}
