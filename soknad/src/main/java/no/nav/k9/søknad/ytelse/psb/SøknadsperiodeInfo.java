package no.nav.k9.søknad.ytelse.psb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SøknadsperiodeInfo {
    @JsonProperty(value = "søktPleiepengerProsent")
    public final BigDecimal søktPleiepengerProsent;

    @JsonProperty(value = "flereOmsorgspersoner")
    public final Boolean flereOmsorgspersoner;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "relasjonTilBarnet", required = true)
    public final String relasjonTilBarnet;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "samtykketOmsorgForBarnet")
    public final Boolean samtykketOmsorgForBarnet;

    @JsonProperty(value = "beskrivelseAvOmsorgsrollen")
    public final String beskrivelseAvOmsorgsrollen;

    @JsonCreator
    private SøknadsperiodeInfo(
            @JsonProperty(value = "søktPleiepengerProsent") BigDecimal søktPleiepengerProsent,
            @JsonProperty(value = "flereOmsorgspersoner") Boolean flereOmsorgspersoner,
            @JsonProperty(value = "relasjonTilBarnet", required = true) String relasjonTilBarnet,
            @JsonProperty(value = "samtykketOmsorgForBarnet", required = true) Boolean samtykketOmsorgForBarnet,
            @JsonProperty(value = "beskrivelseAvOmsorgsrollen") String beskrivelseAvOmsorgsrollen) {
        this.søktPleiepengerProsent = søktPleiepengerProsent;
        this.flereOmsorgspersoner = flereOmsorgspersoner;
        this.relasjonTilBarnet = relasjonTilBarnet;
        this.samtykketOmsorgForBarnet = samtykketOmsorgForBarnet;
        this.beskrivelseAvOmsorgsrollen = beskrivelseAvOmsorgsrollen;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private BigDecimal søktPleiepengerProsent;
        private Boolean flereOmsorgspersoner;
        private String relasjonTilBarnet;
        private Boolean samtykketOmsorgForBarnet;
        private String beskrivelseAvOmsorgsrollen;
        private Builder() {}

        public Builder søktPleiepengerProsent(BigDecimal søktPleiepengerProsent) {
            this.søktPleiepengerProsent = søktPleiepengerProsent;
            return this;
        }
        public Builder flereOmsorgspersoner(Boolean flereOmsorgspersoner) {
            this.flereOmsorgspersoner = flereOmsorgspersoner;
            return this;
        }
        public Builder relasjonTilBarnet(String relasjonTilBarnet) {
            this.relasjonTilBarnet = relasjonTilBarnet;
            return this;
        }
        public Builder samtykketOmsorgForBarnet(Boolean samtykketOmsorgForBarnet) {
            this.samtykketOmsorgForBarnet = samtykketOmsorgForBarnet;
            return this;
        }
        public Builder beskrivelseAvOmsorgsrollen(String beskrivelseAvOmsorgsrollen) {
            this.beskrivelseAvOmsorgsrollen = beskrivelseAvOmsorgsrollen;
            return this;
        }

        public SøknadsperiodeInfo build() {
            return new SøknadsperiodeInfo(
                    søktPleiepengerProsent,
                    flereOmsorgspersoner,
                    relasjonTilBarnet,
                    samtykketOmsorgForBarnet,
                    beskrivelseAvOmsorgsrollen);
        }
    }

}
