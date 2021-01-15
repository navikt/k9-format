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
            return new Periode(inntekterSøknadsperiode.firstKey().fraOgMed, inntekterSøknadsperiode.lastKey().tilOgMed);
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
