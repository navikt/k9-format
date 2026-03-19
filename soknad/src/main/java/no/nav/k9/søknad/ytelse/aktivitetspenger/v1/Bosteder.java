package no.nav.k9.søknad.ytelse.aktivitetspenger.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;
import no.nav.k9.søknad.felles.validering.periode.GyldigePerioderMap;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static java.util.Collections.unmodifiableMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Bosteder {

    @JsonProperty(value = "perioder")
    @GyldigePerioderMap(krevFomDato = true, krevTomDato = true, payload = {AvbrytendeValideringsfeil.class})
    @JsonInclude(value = Include.ALWAYS)
    private Map<@NotNull Periode, @Valid @NotNull BostedPeriodeInfo> perioder = new TreeMap<>();

    public Map<Periode, BostedPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Bosteder medPerioder(Map<Periode, BostedPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
    public static class BostedPeriodeInfo {

        @JsonProperty(value = "land", required = true)
        @NotNull
        @Valid
        private Landkode land;

        public Landkode getLand() {
            return land;
        }

        public BostedPeriodeInfo medLand(Landkode land) {
            this.land = Objects.requireNonNull(land, "land");
            return this;
        }
    }
}
