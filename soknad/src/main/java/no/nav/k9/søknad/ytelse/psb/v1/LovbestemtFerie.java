package no.nav.k9.søknad.ytelse.psb.v1;

import static java.util.Collections.unmodifiableMap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class LovbestemtFerie {


    @JsonProperty(value="perioder", required = true)
    @Valid
    private Map<Periode, LovbestemtFeriePeriodeInfo> perioder = new TreeMap<>();

    public LovbestemtFerie() {
    }

    public Map<Periode, LovbestemtFeriePeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public LovbestemtFerie medPerioder(Map<Periode, LovbestemtFeriePeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public LovbestemtFerie leggeTilPeriode(Periode periode, LovbestemtFeriePeriodeInfo tilsynPeriodeInfo) {
        this.perioder.put(periode, tilsynPeriodeInfo);
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LovbestemtFeriePeriodeInfo {

        @JsonProperty(value="skalHaFerie")
        @Valid
        private Boolean skalHaFerie;

        public LovbestemtFeriePeriodeInfo() {
        }

        public LovbestemtFeriePeriodeInfo medSkalHaFerie(Boolean skalHaFerie) {
            this.skalHaFerie = skalHaFerie;
            return this;
        }

        public Boolean isSkalHaFerie() {
            return skalHaFerie == null || skalHaFerie;
        }
    }

}
