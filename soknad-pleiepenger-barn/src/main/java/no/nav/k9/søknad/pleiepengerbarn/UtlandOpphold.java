package no.nav.k9.søknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import no.nav.k9.søknad.felles.Landkode;

public class UtlandOpphold {

    public final Landkode land;

    public final UtlandOppholdÅrsak årsak;

    @JsonCreator
    private UtlandOpphold(
            @JsonProperty("land")
            Landkode land,
            @JsonProperty("årsak")
            UtlandOppholdÅrsak årsak) {
        this.land = land;
        this.årsak = årsak;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Landkode land;
        private UtlandOppholdÅrsak årsak;

        private Builder() {}

        public Builder land(Landkode land) {
            this.land = land;
            return this;
        }


        public Builder årsak(UtlandOppholdÅrsak årsak) {
            this.årsak = årsak;
            return this;
        }

        public UtlandOpphold build() {
            return new UtlandOpphold(
                    land,
                    årsak
            );
        }
    }

    public enum UtlandOppholdÅrsak {
        BARNET_INNLAGT_I_HELSEINSTITUSJON_FOR_NORSK_OFFENTLIG_REGNING("barnetInnlagtIHelseinstitusjonForNorskOffentligRegning"),
        BARNET_INNLAGT_I_HELSEINSTITUSJON_DEKKET_ETTER_AVTALE_MED_ET_ANNET_LAND_OM_TRYGD("barnetInnlagtIHelseinstitusjonDekketEtterAvtaleMedEtAnnetLandOmTrygd");

        @JsonValue
        public final String dto;

        UtlandOppholdÅrsak(String dto) {
            this.dto = dto;
        }

        @JsonCreator
        public static UtlandOppholdÅrsak of(String value) {
            if (value == null || value.isBlank()) {
                return null;
            }
            for (UtlandOppholdÅrsak årsak : values()) {
                if (årsak.dto.equals(value)) {
                    return årsak;
                }
            }
            throw new IllegalArgumentException("Ikke støttet årsak '" + value + "'");
        }
    }

}