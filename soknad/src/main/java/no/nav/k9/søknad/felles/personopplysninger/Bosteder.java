package no.nav.k9.søknad.felles.personopplysninger;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPerioder;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Bosteder {

    @JsonProperty(value = "perioder")
    @Valid
    @JsonInclude(value = Include.ALWAYS)
    private Map<Periode, BostedPeriodeInfo> perioder;

    @JsonProperty(value = "perioderSomSkalSlettes")
    @Valid
    @JsonInclude(value = Include.ALWAYS)
    private Map<Periode, BostedPeriodeInfo> perioderSomSkalSlettes;

    @JsonCreator
    public Bosteder(
                    @JsonProperty("perioder") Map<Periode, BostedPeriodeInfo> perioder) {
        this.perioder = (perioder == null ) ? new TreeMap<>() : new TreeMap<>(perioder);
    }

    public Bosteder() {

    }

    public Map<Periode, BostedPeriodeInfo> getPerioder() {
        return unmodifiableMap(perioder);
    }

    public Bosteder medPerioder(Map<Periode, BostedPeriodeInfo> perioder) {
        this.perioder = (perioder == null ) ? new TreeMap<>() : new TreeMap<>(perioder);
        return this;
    }

    public Map<Periode, BostedPeriodeInfo> getPerioderSomSkalSlettes() {
        return unmodifiableMap(perioderSomSkalSlettes);
    }

    public Bosteder medPerioderSomSkalSlettes(Map<Periode, BostedPeriodeInfo> perioderSomSkalSlettes) {
        this.perioderSomSkalSlettes = (perioderSomSkalSlettes == null ) ? new TreeMap<>() : new TreeMap<>(perioderSomSkalSlettes);
        return this;
    }

    /**@deprecated brukt ctor.*/
    @Deprecated(forRemoval = true)
    public static Builder builder() {
        return new Builder();
    }

    /**@deprecated brukt ctor.*/
    @Deprecated(forRemoval = true)
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
                perioder);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
    public static class BostedPeriodeInfo {

        @JsonProperty(value = "land")
        private Landkode land;

        @JsonCreator
        public BostedPeriodeInfo(@JsonProperty(value = "land") Landkode land) {
            this.land = land;
        }

        public BostedPeriodeInfo() {

        }

        public Landkode getLand() {
            return land;
        }

        public BostedPeriodeInfo medLand(Landkode land) {
            this.land = land;
            return this;
        }

        /**@deprecated brukt ctor.*/
        @Deprecated(forRemoval = true)
        public static Builder builder() {
            return new Builder();
        }

        /**@deprecated brukt ctor.*/
        @Deprecated(forRemoval = true)
        public static final class Builder {
            private Landkode land;

            private Builder() {
            }

            public Builder land(Landkode land) {
                this.land = land;
                return this;
            }

            public BostedPeriodeInfo build() {
                return new BostedPeriodeInfo(
                    land);
            }
        }
    }
}
