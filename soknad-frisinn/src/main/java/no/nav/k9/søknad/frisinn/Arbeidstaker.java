package no.nav.k9.søknad.frisinn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.ÅpenPeriode;

import javax.validation.Valid;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Arbeidstaker {

    /**
     * Inntekter i periode som arbeidstaker.
     */
    @JsonProperty(value = "inntekterSøknadsperiode")
    @Valid
    private NavigableMap<ÅpenPeriode, PeriodeInntekt> inntekterSøknadsperiode;


    @JsonCreator
    public Arbeidstaker(@JsonProperty(value = "inntekterSøknadsperiode") Map<ÅpenPeriode, PeriodeInntekt> inntekterSøknadsperiode) {
        this.inntekterSøknadsperiode = (inntekterSøknadsperiode == null) ? Collections.emptyNavigableMap()
                : Collections.unmodifiableNavigableMap(new TreeMap<>(inntekterSøknadsperiode));
    }

    public static Builder builder() {
        return new Builder();
    }

    public NavigableMap<ÅpenPeriode, PeriodeInntekt> getInntekterSøknadsperiode() {
        return inntekterSøknadsperiode;
    }

    public ÅpenPeriode getMaksSøknadsperiode() {
        if (inntekterSøknadsperiode.isEmpty()) {
            return null;
        } else {
            return new ÅpenPeriode(inntekterSøknadsperiode.firstKey().getFraOgMed(), inntekterSøknadsperiode.lastKey().getTilOgMed());
        }
    }

    public static final class Builder {
        private Map<ÅpenPeriode, PeriodeInntekt> inntekterSøknadsperiode = new LinkedHashMap<>();

        private Builder() {
        }

        public Builder inntekterSøknadsperiode(Map<ÅpenPeriode, PeriodeInntekt> inntekter) {
            inntekterSøknadsperiode.putAll(inntekter);
            return this;
        }

        public Arbeidstaker build() {
            return new Arbeidstaker(inntekterSøknadsperiode);
        }
    }
}
