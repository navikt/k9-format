package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Nattevåk {

    @JsonProperty(value="perioder")
    @Valid
    @NotEmpty
    private Map<Periode, NattevåkPeriodeInfo> perioder;

    @JsonCreator
    public Nattevåk(@JsonProperty("perioder") @Valid @NotEmpty Map<Periode, NattevåkPeriodeInfo> perioder) {
        this.perioder = perioder;
    }

    public Map<Periode, NattevåkPeriodeInfo> getPerioder() {
        return perioder;
    }

    public void setPerioder(Map<Periode, NattevåkPeriodeInfo> perioder) {
        this.perioder = perioder;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class NattevåkPeriodeInfo {

        @JsonProperty(value="tilleggsinformasjon")
        @Valid
        @NotNull
        private String tilleggsinformasjon;

        @JsonCreator
        public NattevåkPeriodeInfo(
                @JsonProperty(value="tilleggsinformasjon")
                String tilleggsinformasjon) {
            this.tilleggsinformasjon = tilleggsinformasjon;
        }

        public String getTilleggsinformasjon() {
            return tilleggsinformasjon;
        }

        public void setTilleggsinformasjon(String tilleggsinformasjon) {
            this.tilleggsinformasjon = tilleggsinformasjon;
        }
    }
}
