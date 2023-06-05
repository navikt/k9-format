package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class DataBruktTilUtledning {

    /**
     * @deprecated flyttes til  {@link no.nav.k9.søknad.ytelse.DataBruktTilUtledning}
     */
    @Deprecated(forRemoval = true, since = "8.2.1")
    @JsonProperty(value = "harForståttRettigheterOgPlikter")
    @Valid
    private Boolean harForståttRettigheterOgPlikter;

    /**
     * @deprecated flyttes til  {@link no.nav.k9.søknad.ytelse.DataBruktTilUtledning}
     */
    @Deprecated(forRemoval = true, since = "8.2.1")
    @JsonProperty(value = "harBekreftetOpplysninger")
    @Valid
    private Boolean harBekreftetOpplysninger;

    @JsonProperty(value = "samtidigHjemme")
    @Valid
    private Boolean samtidigHjemme;

    @JsonProperty(value = "harMedsøker")
    @Valid
    private Boolean harMedsøker;

    /**
     * @deprecated flyttes til  {@link no.nav.k9.søknad.ytelse.DataBruktTilUtledning}
     */
    @Deprecated(forRemoval = true, since = "8.2.1")
    @JsonProperty(value = "soknadDialogCommitSha")
    @Valid
    private String soknadDialogCommitSha;

    // Utgår?
    @JsonProperty(value = "bekrefterPeriodeOver8Uker")
    @Valid
    private Boolean bekrefterPeriodeOver8Uker;

    @JsonProperty(value = "ukjenteArbeidsforhold")
    @Valid
    private List<UkjentArbeidsforhold> ukjenteArbeidsforhold;

    @JsonCreator
    public DataBruktTilUtledning(
            /** @deprecated flyttes til  {@link no.nav.k9.søknad.ytelse.DataBruktTilUtledning} */
            @Deprecated(forRemoval = true, since = "8.2.1")
            @JsonProperty(value = "harForståttRettigheterOgPlikter") @Valid Boolean harForståttRettigheterOgPlikter,

            /** @deprecated flyttes til  {@link no.nav.k9.søknad.ytelse.DataBruktTilUtledning} */
            @Deprecated(forRemoval = true, since = "8.2.1")
            @JsonProperty(value = "harBekreftetOpplysninger") @Valid Boolean harBekreftetOpplysninger,

            @JsonProperty(value = "samtidigHjemme") @Valid Boolean samtidigHjemme,
            @JsonProperty(value = "harMedsøker") @Valid Boolean harMedsøker,

            /** @deprecated flyttes til  {@link no.nav.k9.søknad.ytelse.DataBruktTilUtledning} */
            @Deprecated(forRemoval = true, since = "8.2.1")
            @JsonProperty(value = "soknadDialogCommitSha") @Valid String soknadDialogCommitSha,

            @JsonProperty(value = "bekrefterPeriodeOver8Uker") @Valid Boolean bekrefterPeriodeOver8Uker,

            @JsonProperty(value = "ukjenteArbeidsforhold") @Valid List<UkjentArbeidsforhold> ukjenteArbeidsforhold
    ) {

        this.harForståttRettigheterOgPlikter = harForståttRettigheterOgPlikter;
        this.harBekreftetOpplysninger = harBekreftetOpplysninger;
        this.samtidigHjemme = samtidigHjemme;
        this.harMedsøker = harMedsøker;
        this.soknadDialogCommitSha = soknadDialogCommitSha;
        this.bekrefterPeriodeOver8Uker = bekrefterPeriodeOver8Uker;
        this.ukjenteArbeidsforhold = ukjenteArbeidsforhold;
    }

    public DataBruktTilUtledning() {
    }

    /**
     * @deprecated flyttes til  {@link no.nav.k9.søknad.ytelse.DataBruktTilUtledning}
     */
    @Deprecated(forRemoval = true, since = "8.2.1")
    public Boolean getHarForståttRettigheterOgPlikter() {
        return harForståttRettigheterOgPlikter;
    }

    /**
     * @deprecated flyttes til  {@link no.nav.k9.søknad.ytelse.DataBruktTilUtledning}
     */
    @Deprecated(forRemoval = true, since = "8.2.1")
    public DataBruktTilUtledning medHarForståttRettigheterOgPlikter(Boolean harForståttRettigheterOgPlikter) {
        this.harForståttRettigheterOgPlikter = harForståttRettigheterOgPlikter;
        return this;
    }

    /**
     * @deprecated flyttes til  {@link no.nav.k9.søknad.ytelse.DataBruktTilUtledning}
     */
    @Deprecated(forRemoval = true, since = "8.2.1")
    public Boolean getHarBekreftetOpplysninger() {
        return harBekreftetOpplysninger;
    }

    /**
     * @deprecated flyttes til  {@link no.nav.k9.søknad.ytelse.DataBruktTilUtledning}
     */
    @Deprecated(forRemoval = true, since = "8.2.1")
    public DataBruktTilUtledning medHarBekreftetOpplysninger(Boolean harBekreftetOpplysninger) {
        this.harBekreftetOpplysninger = harBekreftetOpplysninger;
        return this;
    }

    public Boolean getSamtidigHjemme() {
        return samtidigHjemme;
    }

    public DataBruktTilUtledning medSamtidigHjemme(Boolean samtidigHjemme) {
        this.samtidigHjemme = samtidigHjemme;
        return this;
    }

    public Boolean getHarMedsøker() {
        return harMedsøker;
    }

    public DataBruktTilUtledning medHarMedsøker(Boolean harMedsøker) {
        this.harMedsøker = harMedsøker;
        return this;
    }

    /**
     * @deprecated flyttes til  {@link no.nav.k9.søknad.ytelse.DataBruktTilUtledning}
     */
    @Deprecated(forRemoval = true, since = "8.2.1")
    public String getSoknadDialogCommitSha() {
        return this.soknadDialogCommitSha;
    }

    /**
     * @deprecated flyttes til  {@link no.nav.k9.søknad.ytelse.DataBruktTilUtledning}
     */
    @Deprecated(forRemoval = true, since = "8.2.1")
    public DataBruktTilUtledning medSoknadDialogCommitSha(String soknadDialogCommitSha) {
        this.soknadDialogCommitSha = soknadDialogCommitSha;
        return this;
    }

    public Boolean getBekrefterPeriodeOver8Uker() {
        return bekrefterPeriodeOver8Uker;
    }

    public DataBruktTilUtledning medBekrefterPeriodeOver8Uker(Boolean bekrefterPeriodeOver8Uker) {
        this.bekrefterPeriodeOver8Uker = bekrefterPeriodeOver8Uker;
        return this;
    }

    public List<UkjentArbeidsforhold> getUkjenteArbeidsforhold() {
        return ukjenteArbeidsforhold;
    }

    public DataBruktTilUtledning medUkjenteArbeidsforhold(List<UkjentArbeidsforhold> ukjenteArbeidsforhold) {
        this.ukjenteArbeidsforhold = ukjenteArbeidsforhold;
        return this;
    }
}
