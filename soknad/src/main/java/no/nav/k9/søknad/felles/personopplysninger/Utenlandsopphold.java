package no.nav.k9.søknad.felles.personopplysninger;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Periode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPerioder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Utenlandsopphold {

    @JsonInclude(value = Include.ALWAYS)
    @JsonProperty(value = "perioder")
    public final Map<Periode, UtenlandsoppholdPeriodeInfo> perioder;

    @JsonCreator
    public Utenlandsopphold(
                            @JsonProperty("perioder") Map<Periode, UtenlandsoppholdPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public Map<Periode, UtenlandsoppholdPeriodeInfo> getPerioder() {
        return perioder;
    }

    public static Utenlandsopphold.Builder builder() {
        return new Builder();
    }

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
        public final Landkode land;

        @JsonProperty(value = "årsak")
        public final UtenlandsoppholdÅrsak årsak;

        @JsonCreator
        private UtenlandsoppholdPeriodeInfo(
                                            @JsonProperty("land") Landkode land,
                                            @JsonProperty("årsak") UtenlandsoppholdÅrsak årsak) {
            this.land = land;
            this.årsak = årsak;
        }

        public Landkode getLand() {
            return land;
        }
        
        public UtenlandsoppholdÅrsak getÅrsak() {
            return årsak;
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
        public final String dto;

        UtenlandsoppholdÅrsak(String dto) {
            this.dto = dto;
        }

        @JsonCreator
        public static UtenlandsoppholdÅrsak of(String value) {
            if (value == null || value.isBlank()) {
                return null;
            }
            for (UtenlandsoppholdÅrsak årsak : values()) {
                if (årsak.dto.equals(value)) {
                    return årsak;
                }
            }
            throw new IllegalArgumentException("Ikke støttet årsak '" + value + "'");
        }
    }
}
