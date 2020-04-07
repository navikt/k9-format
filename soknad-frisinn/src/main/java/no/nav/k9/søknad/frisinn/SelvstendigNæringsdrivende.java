package no.nav.k9.søknad.frisinn;

import static java.util.Collections.emptyNavigableMap;
import static java.util.Collections.unmodifiableNavigableMap;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class SelvstendigNæringsdrivende {

    @JsonInclude(value = Include.NON_EMPTY)
    @JsonProperty(value = "inntekterFør")
    @Valid
    private NavigableMap<Periode, PeriodeInntekt> inntekterFør;

    @JsonInclude(value = Include.ALWAYS)
    @JsonProperty(value = "inntekterSøknadsperiode", required = true)
    @Valid
    private NavigableMap<Periode, PeriodeInntekt> inntekterSøknadsperiode;

    @JsonCreator
    public SelvstendigNæringsdrivende(@JsonProperty(value = "inntekterFør") Map<Periode, PeriodeInntekt> inntekterFør,
                                      @JsonProperty(value = "inntekterSøknadsperiode") Map<Periode, PeriodeInntekt> inntekterSøknadsperiode) {
        this.inntekterSøknadsperiode = (inntekterSøknadsperiode == null) ? emptyNavigableMap() : unmodifiableNavigableMap(new TreeMap<>(inntekterSøknadsperiode));
        this.inntekterFør = (inntekterFør == null) ? emptyNavigableMap() : unmodifiableNavigableMap(new TreeMap<>(inntekterFør));
        validerPerioder(this.inntekterFør, this.inntekterSøknadsperiode);
    }

    private void validerPerioder(@Valid NavigableMap<Periode, PeriodeInntekt> inntekterFør,
                                 @Valid NavigableMap<Periode, PeriodeInntekt> inntekterSøknadsperiode) {
        if (!inntekterFør.isEmpty() && !inntekterSøknadsperiode.isEmpty()) {
            LocalDate førTom = inntekterFør.lastKey().getTilOgMed();
            LocalDate søkFom = inntekterSøknadsperiode.firstKey().getFraOgMed();
            if (!førTom.isBefore(søkFom)) {
                throw new IllegalArgumentException("inntekterFør må være tidligere enn inntekterSøknadsperiode: " + førTom + ">=" + søkFom);
            }
        }
    }

    public Periode getMaksSøknadsperiode() {
        if (inntekterSøknadsperiode.isEmpty()) {
            return null;
        } else {
            return new Periode(inntekterSøknadsperiode.firstKey().fraOgMed, inntekterSøknadsperiode.lastKey().tilOgMed);
        }
    }

    public Periode getMaksPeriodeInntekterFør() {
        if (inntekterFør.isEmpty()) {
            return null;
        } else {
            return new Periode(inntekterFør.firstKey().fraOgMed, inntekterFør.lastKey().tilOgMed);
        }
    }

    public Map<Periode, PeriodeInntekt> getInntekterSøknadsperiode() {
        return inntekterSøknadsperiode;
    }

    public Map<Periode, PeriodeInntekt> getInntekterFør() {
        return inntekterFør;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, PeriodeInntekt> inntekterFør = new LinkedHashMap<>();
        private Map<Periode, PeriodeInntekt> inntekterSøknadsperiode = new LinkedHashMap<>();

        private Builder() {
        }

        public Builder inntekterFør(Map<Periode, PeriodeInntekt> inntekter) {
            inntekterFør.putAll(inntekter);
            return this;
        }

        public Builder inntekterSøknadsperiode(Map<Periode, PeriodeInntekt> inntekter) {
            inntekterSøknadsperiode.putAll(inntekter);
            return this;
        }

        public SelvstendigNæringsdrivende build() {
            return new SelvstendigNæringsdrivende(inntekterFør, inntekterSøknadsperiode);
        }
    }

}
