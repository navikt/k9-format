package no.nav.k9.søknad.ytelse.psb.v1;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;
import no.nav.k9.søknad.felles.validering.periode.GyldigePerioderMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class LovbestemtFerie {

    @JsonProperty(value = "perioder", required = true)
    @Valid
    @GyldigePerioderMap(krevFomDato = true, krevTomDato = true, payload = {AvbrytendeValideringsfeil.class})
    @NotNull
    private Map<@NotNull Periode, @NotNull @Valid LovbestemtFeriePeriodeInfo> perioder = new TreeMap<>();

    public LovbestemtFerie() {
    }

    public Map<Periode, LovbestemtFeriePeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public LovbestemtFerie medPerioder(Map<Periode, LovbestemtFeriePeriodeInfo> perioder) {
        this.perioder = Objects.requireNonNull(perioder, "perioder");
        return this;
    }

    public LovbestemtFerie leggeTilPeriode(Periode periode, LovbestemtFeriePeriodeInfo tilsynPeriodeInfo) {
        Objects.requireNonNull(periode, "periode");
        Objects.requireNonNull(tilsynPeriodeInfo, "tilsynPeriodeInfo");
        this.perioder.put(periode, tilsynPeriodeInfo);
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LovbestemtFeriePeriodeInfo {

        @JsonProperty(value = "skalHaFerie")
        @Valid
        private Boolean skalHaFerie;

        public LovbestemtFeriePeriodeInfo() {
        }

        public LovbestemtFeriePeriodeInfo medSkalHaFerie(Boolean skalHaFerie) {
            this.skalHaFerie = Objects.requireNonNull(skalHaFerie, "skalHaFerie");
            return this;
        }

        public Boolean isSkalHaFerie() {
            return skalHaFerie == null || skalHaFerie;
        }

        @Override
        public int hashCode() {
            return Objects.hash(skalHaFerie);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            LovbestemtFeriePeriodeInfo other = (LovbestemtFeriePeriodeInfo) obj;
            return Objects.equals(skalHaFerie, other.skalHaFerie);
        }
    }

}
