package no.nav.k9.søknad.felles.personopplysninger;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPerioder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Bosteder {

    @JsonProperty(value="perioder")
    @Valid
    @JsonInclude(value = Include.ALWAYS)
    public final Map<Periode, BostedPeriodeInfo> perioder;

    @JsonCreator
    public Bosteder(
            @JsonProperty("perioder")
            Map<Periode, BostedPeriodeInfo> perioder) {
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
    }

    public Map<Periode, BostedPeriodeInfo> getPerioder() {
        return perioder;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, BostedPeriodeInfo> perioder;

        private Builder() {
            perioder = new HashMap<>();
        }

        public Builder perioder(Map<Periode, BostedPeriodeInfo> perioder) {
            leggTilPerioder(this.perioder, perioder);
            return this;
        }

        public Builder periode(Periode periode, BostedPeriodeInfo bostedPeriodeInfo) {
            leggTilPeriode(this.perioder, periode, bostedPeriodeInfo);
            return this;
        }

        public Bosteder build() {
            return new Bosteder(
                    perioder
            );
        }
    }


    public static class BostedPeriodeInfo {
        public final Landkode land;

        @JsonCreator
        private BostedPeriodeInfo( @JsonProperty("land") Landkode land) {
            this.land = land;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private Landkode land;

            private Builder() {}

            public Builder land(Landkode land) {
                this.land = land;
                return this;
            }

            public BostedPeriodeInfo build() {
                return new BostedPeriodeInfo(
                        land
                );
            }
        }
    }
}
