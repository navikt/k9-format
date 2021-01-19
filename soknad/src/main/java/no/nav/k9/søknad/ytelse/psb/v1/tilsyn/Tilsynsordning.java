package no.nav.k9.søknad.ytelse.psb.v1.tilsyn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Tilsynsordning {

    @JsonProperty(value="iTilsynsordning", required=true)
    @NotNull
    @Valid
    private TilsynsordningSvar iTilsynsordning;

    @JsonProperty(value="opphold")
    @Valid
    private Map<Periode, TilsynsordningOpphold> opphold;

    @JsonCreator
    public Tilsynsordning(
            @JsonProperty("iTilsynsordning")
            TilsynsordningSvar iTilsynsordning,
            @JsonProperty("opphold")
            Map<Periode, TilsynsordningOpphold> opphold) {
        this.iTilsynsordning = iTilsynsordning;
        this.opphold = opphold;
    }

    public TilsynsordningSvar getiTilsynsordning() {
        return iTilsynsordning;
    }

    public void setiTilsynsordning(TilsynsordningSvar iTilsynsordning) {
        this.iTilsynsordning = iTilsynsordning;
    }

    public Map<Periode, TilsynsordningOpphold> getOpphold() {
        return opphold;
    }

    public void setOpphold(Map<Periode, TilsynsordningOpphold> opphold) {
        this.opphold = opphold;
    }
}
