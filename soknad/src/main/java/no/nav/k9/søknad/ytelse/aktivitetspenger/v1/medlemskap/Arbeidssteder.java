package no.nav.k9.søknad.ytelse.aktivitetspenger.v1.medlemskap;

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
public class Arbeidssteder {

    @JsonProperty(value = "perioder")
    @GyldigePerioderMap(krevFomDato = true, krevTomDato = true, payload = {AvbrytendeValideringsfeil.class})
    @JsonInclude(value = Include.ALWAYS)
    private Map<@NotNull Periode, @Valid @NotNull ArbeidsstedPeriodeInfo> perioder = new TreeMap<>();

    public Map<Periode, ArbeidsstedPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Arbeidssteder medPerioder(Map<Periode, ArbeidsstedPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
    public static class ArbeidsstedPeriodeInfo {

        @JsonProperty(value = "land", required = true)
        @NotNull
        @Valid
        private Landkode land;

        @JsonProperty(value = "identitetsnummer")
        @NotNull
        @Valid
        private String identitetsnummer;

        public Landkode getLand() {
            return land;
        }

        public ArbeidsstedPeriodeInfo medLand(Landkode land) {
            this.land = Objects.requireNonNull(land, "land");
            return this;
        }

        public String getIdentitetsnummer() {
            return identitetsnummer;
        }

        public ArbeidsstedPeriodeInfo medIdentitetsnummer(String identitetsnummer) {
            this.identitetsnummer = identitetsnummer;
            return this;
        }

        @Override
        public String toString() {
            return "ArbeidsstedPeriodeInfo{land=" + land + "}";
        }
    }
}
