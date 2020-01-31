package no.nav.k9.søknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import no.nav.k9.søknad.felles.Landkode;

public class Utenlandsopphold {

    public final Landkode land;

    public final UtenlandsoppholdÅrsak årsak;

    @JsonCreator
    private Utenlandsopphold(
            @JsonProperty("land")
            Landkode land,
            @JsonProperty("årsak")
            UtenlandsoppholdÅrsak årsak) {
        this.land = land;
        this.årsak = årsak;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Landkode land;
        private UtenlandsoppholdÅrsak årsak;

        private Builder() {}

        public Builder land(Landkode land) {
            this.land = land;
            return this;
        }


        public Builder årsak(UtenlandsoppholdÅrsak årsak) {
            this.årsak = årsak;
            return this;
        }

        public Utenlandsopphold build() {
            return new Utenlandsopphold(
                    land,
                    årsak
            );
        }
    }

    public enum UtenlandsoppholdÅrsak {
        BARNET_INNLAGT_I_HELSEINSTITUSJON_FOR_NORSK_OFFENTLIG_REGNING("barnetInnlagtIHelseinstitusjonForNorskOffentligRegning"),
        BARNET_INNLAGT_I_HELSEINSTITUSJON_DEKKET_ETTER_AVTALE_MED_ET_ANNET_LAND_OM_TRYGD("barnetInnlagtIHelseinstitusjonDekketEtterAvtaleMedEtAnnetLandOmTrygd");

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