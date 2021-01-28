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

    public SøknadInfo() {
    }

    public String getRelasjonTilBarnet() {
        return relasjonTilBarnet;
    }

    public SøknadInfo medRelasjonTilBarnet(String relasjonTilBarnet) {
        this.relasjonTilBarnet = relasjonTilBarnet;
        return this;
    }

    public Boolean getSamtykketOmsorgForBarnet() {
        return samtykketOmsorgForBarnet;
    }

    public SøknadInfo medSamtykketOmsorgForBarnet(Boolean samtykketOmsorgForBarnet) {
        this.samtykketOmsorgForBarnet = samtykketOmsorgForBarnet;
        return this;
    }

    public String getBeskrivelseAvOmsorgsrollen() {
        return beskrivelseAvOmsorgsrollen;
    }

    public SøknadInfo medBeskrivelseAvOmsorgsrollen(String beskrivelseAvOmsorgsrollen) {
        this.beskrivelseAvOmsorgsrollen = beskrivelseAvOmsorgsrollen;
        return this;
    }

    public Boolean getHarForståttRettigheterOgPlikter() {
        return harForståttRettigheterOgPlikter;
    }

    public SøknadInfo medHarForståttRettigheterOgPlikter(Boolean harForståttRettigheterOgPlikter) {
        this.harForståttRettigheterOgPlikter = harForståttRettigheterOgPlikter;
        return this;
    }

    public Boolean getHarBekreftetOpplysninger() {
        return harBekreftetOpplysninger;
    }

    public SøknadInfo medHarBekreftetOpplysninger(Boolean harBekreftetOpplysninger) {
        this.harBekreftetOpplysninger = harBekreftetOpplysninger;
        return this;
    }

    public Boolean getFlereOmsorgspersoner() {
        return flereOmsorgspersoner;
    }

    public SøknadInfo medFlereOmsorgspersoner(Boolean flereOmsorgspersoner) {
        this.flereOmsorgspersoner = flereOmsorgspersoner;
        return this;
    }

    public Boolean getSamtidigHjemme() {
        return samtidigHjemme;
    }

    public SøknadInfo medSamtidigHjemme(Boolean samtidigHjemme) {
        this.samtidigHjemme = samtidigHjemme;
        return this;
    }

    public Boolean getHarMedsøker() {
        return harMedsøker;
    }

    public SøknadInfo medHarMedsøker(Boolean harMedsøker) {
        this.harMedsøker = harMedsøker;
        return this;
    }

    public Boolean getBekrefterPeriodeOver8Uker() {
        return bekrefterPeriodeOver8Uker;
    }

    public SøknadInfo medBekrefterPeriodeOver8Uker(Boolean bekrefterPeriodeOver8Uker) {
        this.bekrefterPeriodeOver8Uker = bekrefterPeriodeOver8Uker;
        return this;
    }
}