package no.nav.k9.søknad.ytelse.psb.v1;

import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Deprecated
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class InfoFraPunsj {

    @JsonProperty(value = "søknadenInneholderInfomasjonSomIkkeKanPunsjes")
    @Valid
    private Boolean søknadenInneholderInfomasjonSomIkkeKanPunsjes;

    @JsonProperty(value = "inneholderMedisinskeOpplysninger")
    @Valid
    private Boolean inneholderMedisinskeOpplysninger;

    public InfoFraPunsj() {
    }

    public Boolean getSøknadenInneholderInfomasjonSomIkkeKanPunsjes() {
        return søknadenInneholderInfomasjonSomIkkeKanPunsjes;
    }

    public InfoFraPunsj medSøknadenInneholderInfomasjonSomIkkeKanPunsjes(Boolean søknadenInneholderInfomasjonSomIkkeKanPunsjes) {
        this.søknadenInneholderInfomasjonSomIkkeKanPunsjes = søknadenInneholderInfomasjonSomIkkeKanPunsjes;
        return this;
    }

    public Boolean getInneholderMedisinskeOpplysninger() {
        return inneholderMedisinskeOpplysninger;
    }

    public InfoFraPunsj medInneholderMedisinskeOpplysninger(Boolean inneholderMedisinskeOpplysninger) {
        this.inneholderMedisinskeOpplysninger = inneholderMedisinskeOpplysninger;
        return this;
    }
}
