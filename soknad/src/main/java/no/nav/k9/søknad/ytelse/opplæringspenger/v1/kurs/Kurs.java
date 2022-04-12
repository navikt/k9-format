package no.nav.k9.søknad.ytelse.opplæringspenger.v1.kurs;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Kurs {

    @JsonProperty(value = "holder", required = true)
    @NotNull
    @Valid
    private String holder;

    @JsonProperty(value = "formål", required = true)
    @NotNull
    @Valid
    private String formål;

    @JsonProperty(value = "periode", required = true)
    @NotNull
    @Valid
    private Periode periode;

    public Kurs() {
    }

    public Kurs(String kursholder, String formålMedKurset, Periode periode) {
        this.holder = kursholder;
        this.formål = formålMedKurset;
        this.periode = periode;
    }

    public String getHolder() {
        return holder;
    }

    public String getFormål() {
        return formål;
    }

    public Periode getPeriode() {
        return periode;
    }
}
