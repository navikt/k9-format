package no.nav.k9.søknad.frisinn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import jakarta.validation.Valid;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Arbeidstaker {

    /**
     * Inntekter i periode som arbeidstaker.
     */
    @JsonProperty(value = "inntekterSøknadsperiode")
    @Valid
    private NavigableMap<Periode, PeriodeInntekt> inntekterSøknadsperiode;


    @JsonCreator
    public Arbeidstaker(@JsonProperty(value = "inntekterSøknadsperiode") Map<Periode, PeriodeInntekt> inntekterSøknadsperiode) {
        this.inntekterSøknadsperiode = (inntekterSøknadsperiode == null) ? Collections.emptyNavigableMap()
                : Collections.unmodifiableNavigableMap(new TreeMap<>(inntekterSøknadsperiode));
    }

    public static Builder builder() {
        return new Builder();
    }

    public NavigableMap<Periode, PeriodeInntekt> getInntekterSøknadsperiode() {
        return inntekterSøknadsperiode;
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

        private Builder() {
        }

        public Builder inntekterSøknadsperiode(Map<Periode, PeriodeInntekt> inntekter) {
            inntekterSøknadsperiode.putAll(inntekter);
            return this;
        }

        public Arbeidstaker build() {
            return new Arbeidstaker(inntekterSøknadsperiode);
        }
    }
}
