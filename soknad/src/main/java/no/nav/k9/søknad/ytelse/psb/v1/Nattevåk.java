package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Nattevåk {

    @JsonProperty(value="perioder")
    @Valid
    @NotEmpty
    private Map<Periode, NattevåkPeriodeInfo> perioder;

    @JsonCreator
    public Nattevåk(@JsonProperty("perioder") @Valid @NotEmpty Map<Periode, NattevåkPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
    }

    public Nattevåk() {
    }

    public Map<Periode, NattevåkPeriodeInfo> getPerioder() {
        return Collections.unmodifiableMap(perioder);
    }

    public Nattevåk medPerioder(Map<Periode, NattevåkPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
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

        public NattevåkPeriodeInfo() {
        }

        public String getTilleggsinformasjon() {
            return tilleggsinformasjon;
        }

        public NattevåkPeriodeInfo medTilleggsinformasjon(String tilleggsinformasjon) {
            this.tilleggsinformasjon = tilleggsinformasjon;
            return this;
        }
    }
}
