package no.nav.k9.søknad.felles.personopplysninger;

import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;
import no.nav.k9.søknad.felles.validering.periode.GyldigePerioderMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Utenlandsopphold {

    @Valid
    @GyldigePerioderMap(krevFomDato = true, krevTomDato = true, payload = {AvbrytendeValideringsfeil.class})
    @JsonInclude(value = Include.ALWAYS)
    @JsonProperty(value = "perioder")
    private Map<@NotNull Periode, @NotNull UtenlandsoppholdPeriodeInfo> perioder = new TreeMap<>();

    @Valid
    @GyldigePerioderMap(krevFomDato = true, krevTomDato = true, payload = {AvbrytendeValideringsfeil.class})
    @JsonInclude(value = Include.ALWAYS)
    @JsonProperty(value = "perioderSomSkalSlettes")
    private Map<@NotNull Periode, @NotNull UtenlandsoppholdPeriodeInfo> perioderSomSkalSlettes = new TreeMap<>();

    public Map<Periode, UtenlandsoppholdPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Utenlandsopphold medPerioder(Map<Periode, UtenlandsoppholdPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public Map<Periode, UtenlandsoppholdPeriodeInfo> getPerioderSomSkalSlettes() {
        return unmodifiableMap(perioderSomSkalSlettes);
    }

    public Utenlandsopphold medPerioderSomSkalSlettes(Map<Periode, UtenlandsoppholdPeriodeInfo> perioderSomSkalSlettes) {
        this.perioderSomSkalSlettes = (perioderSomSkalSlettes == null) ? new TreeMap<>() : new TreeMap<>(perioderSomSkalSlettes);
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
    public static class UtenlandsoppholdPeriodeInfo {

        @JsonProperty(value = "land", required = true)
        @NotNull
        private Landkode land;

        @JsonProperty(value = "årsak")
        private UtenlandsoppholdÅrsak årsak;

        public Landkode getLand() {
            return land;
        }

        public UtenlandsoppholdPeriodeInfo medLand(Landkode land) {
            this.land = Objects.requireNonNull(land, "UtenlandsoppholdPeriodeInfo.land");
            return this;
        }

        public UtenlandsoppholdÅrsak getÅrsak() {
            return årsak;
        }

        public UtenlandsoppholdPeriodeInfo medÅrsak(UtenlandsoppholdÅrsak årsak) {
            this.årsak = Objects.requireNonNull(årsak, "UtenlandsoppholdPeriodeInfo.årsak");
            return this;
        }
    }

    public enum UtenlandsoppholdÅrsak {
        BARNET_INNLAGT_I_HELSEINSTITUSJON_FOR_NORSK_OFFENTLIG_REGNING("barnetInnlagtIHelseinstitusjonForNorskOffentligRegning"),
        BARNET_INNLAGT_I_HELSEINSTITUSJON_DEKKET_ETTER_AVTALE_MED_ET_ANNET_LAND_OM_TRYGD(
                "barnetInnlagtIHelseinstitusjonDekketEtterAvtaleMedEtAnnetLandOmTrygd");

        @JsonValue
        private final String årsak;

        UtenlandsoppholdÅrsak(String dto) {
            this.årsak = dto;
        }

        public String getÅrsak() {
            return årsak;
        }

        @JsonCreator
        public static UtenlandsoppholdÅrsak of(String value) {
            if (value == null || value.isBlank()) {
                return null;
            }
            for (UtenlandsoppholdÅrsak årsak : values()) {
                if (årsak.årsak.equals(value)) {
                    return årsak;
                }
            }
            throw new IllegalArgumentException("Ikke støttet årsak '" + value + "'");
        }
    }
}
