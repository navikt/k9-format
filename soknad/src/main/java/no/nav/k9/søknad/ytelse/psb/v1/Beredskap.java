package no.nav.k9.søknad.ytelse.psb.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.unmodifiableMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Beredskap {

    @JsonProperty(value="perioder")
    @Valid
    @NotEmpty
    private Map<Periode, BeredskapPeriodeInfo> perioder;

    @JsonCreator
    public Beredskap(
            @JsonProperty(value = "perioder") @NotEmpty @Valid Map<Periode, BeredskapPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
    }

    public Beredskap() {
    }

    public Map<Periode, BeredskapPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Beredskap medPerioder(Map<Periode, BeredskapPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public static final class BeredskapPeriodeInfo {

        @Valid
        @JsonProperty(value = "tilleggsinformasjon")
        private String tilleggsinformasjon;

        @JsonCreator
        public BeredskapPeriodeInfo(@JsonProperty(value = "tilleggsinformasjon")
                                                     @NotNull @Valid String tilleggsinformasjon) {
            this.tilleggsinformasjon = tilleggsinformasjon;
        }

        public BeredskapPeriodeInfo() {
        }

        public String getTilleggsinformasjon() {
            return tilleggsinformasjon;
        }

        public BeredskapPeriodeInfo medTilleggsinformasjon(String tilleggsinformasjon) {
            this.tilleggsinformasjon = tilleggsinformasjon;
            return this;
        }
    }
}
