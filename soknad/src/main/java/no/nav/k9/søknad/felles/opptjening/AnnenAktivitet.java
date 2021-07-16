package no.nav.k9.søknad.felles.opptjening;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.VirksomhetType;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)

public class AnnenAktivitet {

    @JsonProperty("periode")
    @Valid
    @NotNull
    private Periode periode;

    @JsonProperty(value = "virksomhetstype")
    @Valid
    @NotNull
    private VirksomhetType virksomhetType;


    @JsonCreator
    public AnnenAktivitet(@JsonProperty(value = "periode") @Valid @NotNull Periode periode,
                          @JsonProperty(value = "virksomhetstype") @Valid @NotNull VirksomhetType virksomhetType) {
        this.periode = periode;
        this.virksomhetType = virksomhetType;
    }

    public AnnenAktivitet() {
    }

    public Periode getPeriode() {
        return periode;
    }

    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    public VirksomhetType getVirksomhetType() {
        return virksomhetType;
    }

    public void setVirksomhetType(VirksomhetType virksomhetType) {
        this.virksomhetType = virksomhetType;
    }
}
