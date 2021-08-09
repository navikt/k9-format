package no.nav.k9.søknad.ytelse.psb.v1;

import static java.util.Collections.unmodifiableMap;
import static no.nav.k9.søknad.TidsserieValidator.validerOverlappendePerioder;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class LovbestemtFerie {


    @JsonProperty(value="perioder", required = true)
    @Valid
    private Map<@Valid Periode, @Valid LovbestemtFeriePeriodeInfo> perioder = new TreeMap<>();

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

    @Size(max = 0, message = "${validatedValue}")
    private List<Feil> getValiderPerioder() {
        if (perioder == null)
            List.of();
        return validerOverlappendePerioder(perioder, "lovbestemtFerie.perioder");
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
