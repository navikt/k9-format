package no.nav.k9.søknad.ytelse.psb.v1;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class InfoFraPunsj {

    @JsonProperty(value = "harForståttRettigheterOgPlikter")
    @Valid
    private Boolean søknadenInneholderInfomasjonSomIkkeKanPunsjes;

    public InfoFraPunsj() {
    }

    public Boolean getSøknadenInneholderInfomasjonSomIkkeKanPunsjes() {
        return søknadenInneholderInfomasjonSomIkkeKanPunsjes;
    }

    public InfoFraPunsj medSøknadenInneholderInfomasjonSomIkkeKanPunsjes(Boolean søknadenInneholderInfomasjonSomIkkeKanPunsjes) {
        this.søknadenInneholderInfomasjonSomIkkeKanPunsjes = søknadenInneholderInfomasjonSomIkkeKanPunsjes;
        return this;
    }
}