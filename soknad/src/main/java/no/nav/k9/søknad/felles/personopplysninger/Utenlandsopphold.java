package no.nav.k9.søknad.felles.personopplysninger;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Periode;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPerioder;

import javax.validation.Valid;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Utenlandsopphold {

    @JsonInclude(value = Include.ALWAYS)
    @JsonProperty(value = "perioder")
    private Map<Periode, UtenlandsoppholdPeriodeInfo> perioder;

    @JsonInclude(value = Include.ALWAYS)
    @JsonProperty(value = "perioderSomSkalSlettes")
    private Map<Periode, UtenlandsoppholdPeriodeInfo> perioderSomSkalSlettes;

    @JsonCreator
    public Utenlandsopphold(
                            @JsonProperty("perioder") Map<Periode, UtenlandsoppholdPeriodeInfo> perioder) {
        this.perioder = (perioder == null ) ? new TreeMap<>() : new TreeMap<>(perioder);
    }

    public Utenlandsopphold() {

    }

    public Map<Periode, UtenlandsoppholdPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Utenlandsopphold medPerioder(Map<Periode, UtenlandsoppholdPeriodeInfo> perioder) {
        this.perioder = (perioder == null ) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public Map<Periode, UtenlandsoppholdPeriodeInfo> getPerioderSomSkalSlettes() {
        return unmodifiableMap(perioderSomSkalSlettes);
    }

    public Utenlandsopphold medPerioderSomSkalSlettes(Map<Periode, UtenlandsoppholdPeriodeInfo> perioderSomSkalSlettes) {
        this.perioderSomSkalSlettes = (perioderSomSkalSlettes == null ) ? new TreeMap<>() : new TreeMap<>(perioderSomSkalSlettes);
        return this;
    }

    /**@deprecated brukt ctor.*/
    @Deprecated(forRemoval = true)
    public static Utenlandsopphold.Builder builder() {
        return new Builder();
    }

    /**@deprecated brukt ctor.*/
    @Deprecated(forRemoval = true)
    public static final class Builder {
        private Map<Periode, UtenlandsoppholdPeriodeInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, UtenlandsoppholdPeriodeInfo> perioder) {
            leggTilPerioder(this.perioder, perioder);
            return this;
        }

        public Builder periode(Periode periode, UtenlandsoppholdPeriodeInfo utenlandsoppholdPeriodeInfo) {
            leggTilPeriode(this.perioder, periode, utenlandsoppholdPeriodeInfo);
            return this;
        }

        public Utenlandsopphold build() {
            return new Utenlandsopphold(
                perioder);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
    public static class UtenlandsoppholdPeriodeInfo {

        @JsonProperty(value = "land", required = true)
        private Landkode land;

        @JsonProperty(value = "årsak")
        private UtenlandsoppholdÅrsak årsak;

        @JsonCreator
        private UtenlandsoppholdPeriodeInfo(
                                            @JsonProperty("land") Landkode land,
                                            @JsonProperty("årsak") UtenlandsoppholdÅrsak årsak) {
            this.land = land;
            this.årsak = årsak;
        }

        public UtenlandsoppholdPeriodeInfo() {

        }

        public Landkode getLand() {
            return land;
        }

        public UtenlandsoppholdPeriodeInfo medLand(Landkode land) {
            this.land = land;
            return this;
        }
        
        public UtenlandsoppholdÅrsak getÅrsak() {
            return årsak;
        }

        public UtenlandsoppholdPeriodeInfo medÅrsak(UtenlandsoppholdÅrsak årsak) {
            this.årsak = årsak;
            return this;
        }
        
        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private Landkode land;
            private UtenlandsoppholdÅrsak årsak;

            private Builder() {
            }

            public Builder land(Landkode land) {
                this.land = land;
                return this;
            }

            public Builder årsak(UtenlandsoppholdÅrsak årsak) {
                this.årsak = årsak;
                return this;
            }

            public UtenlandsoppholdPeriodeInfo build() {
                return new UtenlandsoppholdPeriodeInfo(
                    land,
                    årsak);
            }
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
