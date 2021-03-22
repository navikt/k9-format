package no.nav.k9.søknad.frisinn;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Frilanser {

    /**
     * Hvorvidt bruker ønsker inntekter i samme periode som angitt inntekter i søknadsperiode kompensert mot tidligere opptjent
     * beregningsgrunnlag.
     */
    @JsonProperty(value = "søkerKompensasjon")
    private final boolean søkerKompensasjon;
    /**
     * Inntekter i periode som skal kompenseres. Periode må være innenfor søknadsperiode. Hvis ingen inntekt i periode som kompenseres, sett
     * inntekt = 0
     */
    @JsonProperty(value = "inntekterSøknadsperiode")
    @Valid
    private NavigableMap<Periode, PeriodeInntekt> inntekterSøknadsperiode;

    @JsonCreator
    public Frilanser(@JsonProperty(value = "inntekterSøknadsperiode") Map<Periode, PeriodeInntekt> inntekterSøknadsperiode,
                     @JsonProperty(value = "søkerKompensasjon") Boolean søkerKompensasjon) {
        this.inntekterSøknadsperiode = (inntekterSøknadsperiode == null) ? Collections.emptyNavigableMap()
                : Collections.unmodifiableNavigableMap(new TreeMap<>(inntekterSøknadsperiode));
        this.søkerKompensasjon = søkerKompensasjon == null ? true : søkerKompensasjon;
    }

    public static Builder builder() {
        return new Builder();
    }

    public NavigableMap<Periode, PeriodeInntekt> getInntekterSøknadsperiode() {
        return inntekterSøknadsperiode;
    }

    public boolean getSøkerKompensasjon() {
        return søkerKompensasjon;
    }

    public Periode getMaksSøknadsperiode() {
        if (inntekterSøknadsperiode.isEmpty()) {
            return null;
        } else {
            return new Periode(inntekterSøknadsperiode.firstKey().getFraOgMed(), inntekterSøknadsperiode.lastKey().getTilOgMed());
        }
    }

    public static final class Builder {
        private Map<Periode, PeriodeInntekt> inntekterSøknadsperiode = new LinkedHashMap<>();
        private boolean søkerKompensasjon = true;

        private Builder() {
        }

        public Builder inntekterSøknadsperiode(Map<Periode, PeriodeInntekt> inntekter) {
            inntekterSøknadsperiode.putAll(inntekter);
            return this;
        }

        public Builder søkerKompensasjon(boolean søkerKompensasjon) {
            this.søkerKompensasjon = søkerKompensasjon;
            return this;
        }

        public Frilanser build() {
            return new Frilanser(inntekterSøknadsperiode, søkerKompensasjon);
        }
    }

}

