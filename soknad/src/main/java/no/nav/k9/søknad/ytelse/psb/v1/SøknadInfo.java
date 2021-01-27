package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class SøknadInfo {



    @JsonProperty(value = "relasjonTilBarnet")
    @Valid
    private String relasjonTilBarnet;

    @JsonProperty(value = "samtykketOmsorgForBarnet")
    @Valid
    private Boolean samtykketOmsorgForBarnet;

    @JsonProperty(value = "beskrivelseAvOmsorgsrollen")
    @Valid
    private String beskrivelseAvOmsorgsrollen;

    @JsonProperty(value = "harForståttRettigheterOgPlikter")
    @Valid
    private Boolean harForståttRettigheterOgPlikter;

    @JsonProperty(value = "harBekreftetOpplysninger")
    @Valid
    private Boolean harBekreftetOpplysninger;

    @JsonProperty(value = "flereOmsorgspersoner")
    @Valid
    private Boolean flereOmsorgspersoner;

    @JsonProperty(value = "samtidigHjemme")
    @Valid
    private Boolean samtidigHjemme;

    @JsonProperty(value = "harMedsøker")
    @Valid
    private Boolean harMedsøker;

    // Utgår?
    @JsonProperty(value = "bekrefterPeriodeOver8Uker")
    @Valid
    private Boolean bekrefterPeriodeOver8Uker;


    @JsonCreator
    public SøknadInfo( @JsonProperty(value = "relasjonTilBarnet") @Valid String relasjonTilBarnet,
                       @JsonProperty(value = "samtykketOmsorgForBarnet") @Valid Boolean samtykketOmsorgForBarnet,
                       @JsonProperty(value = "beskrivelseAvOmsorgsrollen") @Valid String beskrivelseAvOmsorgsrollen,
                       @JsonProperty(value = "harForståttRettigheterOgPlikter") @Valid Boolean harForståttRettigheterOgPlikter,
                       @JsonProperty(value = "harBekreftetOpplysninger") @Valid Boolean harBekreftetOpplysninger,
                       @JsonProperty(value = "flereOmsorgspersoner") @Valid Boolean flereOmsorgspersoner,
                       @JsonProperty(value = "samtidigHjemme") @Valid Boolean samtidigHjemme,
                       @JsonProperty(value = "harMedsøker") @Valid Boolean harMedsøker,
                       @JsonProperty(value = "bekrefterPeriodeOver8Uker") @Valid Boolean bekrefterPeriodeOver8Uker) {
        this.relasjonTilBarnet = relasjonTilBarnet;
        this.samtykketOmsorgForBarnet = samtykketOmsorgForBarnet;
        this.beskrivelseAvOmsorgsrollen = beskrivelseAvOmsorgsrollen;
        this.harForståttRettigheterOgPlikter = harForståttRettigheterOgPlikter;
        this.harBekreftetOpplysninger = harBekreftetOpplysninger;
        this.flereOmsorgspersoner = flereOmsorgspersoner;
        this.samtidigHjemme = samtidigHjemme;
        this.harMedsøker = harMedsøker;
        this.bekrefterPeriodeOver8Uker = bekrefterPeriodeOver8Uker;
    }
}