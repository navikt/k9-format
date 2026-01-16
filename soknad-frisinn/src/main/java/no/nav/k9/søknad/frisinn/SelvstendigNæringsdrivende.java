package no.nav.k9.søknad.frisinn;

import static java.util.Collections.emptyNavigableMap;
import static java.util.Collections.unmodifiableNavigableMap;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class SelvstendigNæringsdrivende {

    /**
     * Hvorvidt bruker ønsker inntekter i samme periode som angitt inntekter i søknadsperiode kompensert mot tidligere opptjent
     * beregningsgrunnlag.
     */
    @JsonProperty(value = "søkerKompensasjon")
    private final boolean søkerKompensasjon;
    @JsonInclude(value = Include.NON_EMPTY)
    @JsonProperty(value = "inntekterFør")
    private NavigableMap<@Valid Periode, @Valid PeriodeInntekt> inntekterFør;
    /**
     * Inntekter i periode som skal kompenseres. Periode må være innenfor søknadsperiode. Hvis ingen inntekt i periode som kompenseres, sett
     * inntekt = 0
     */
    @JsonInclude(value = Include.ALWAYS)
    @JsonProperty(value = "inntekterSøknadsperiode", required = true)
    private NavigableMap<@Valid Periode, @Valid PeriodeInntekt> inntekterSøknadsperiode;

    @JsonProperty("regnskapsførerNavn")
    @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}]+$", message = "[ugyldigSyntaks] matcher ikke tillatt pattern '{regexp}'")
    private String regnskapsførerNavn;

    @JsonProperty("regnskapsførerTlf")
    @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}]+$", message = "[ugyldigSyntaks] matcher ikke tillatt pattern '{regexp}'")
    private String regnskapsførerTlf;

    @JsonCreator
    public SelvstendigNæringsdrivende(@JsonProperty(value = "inntekterFør") Map<Periode, PeriodeInntekt> inntekterFør,
                                      @JsonProperty(value = "inntekterSøknadsperiode") Map<Periode, PeriodeInntekt> inntekterSøknadsperiode,
                                      @JsonProperty(value = "søkerKompensasjon") Boolean søkerKompensasjon,
                                      @JsonProperty(value = "regnskapsførerNavn") String regnskapsførerNavn,
                                      @JsonProperty(value = "regnskapsførerTlf") String regnskapsførerTlf) {
        this.regnskapsførerNavn = regnskapsførerNavn;
        this.regnskapsførerTlf = regnskapsførerTlf;

        this.inntekterSøknadsperiode = (inntekterSøknadsperiode == null) ? emptyNavigableMap()
                : unmodifiableNavigableMap(new TreeMap<>(inntekterSøknadsperiode));
        this.inntekterFør = (inntekterFør == null) ? emptyNavigableMap() : unmodifiableNavigableMap(new TreeMap<>(inntekterFør));
        validerPerioder(this.inntekterFør, this.inntekterSøknadsperiode);
        this.søkerKompensasjon = søkerKompensasjon == null ? true : søkerKompensasjon;
    }

    public static Builder builder() {
        return new Builder();
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
            return new Periode(inntekterSøknadsperiode.firstKey().getFraOgMed(), inntekterSøknadsperiode.lastKey().getTilOgMed());
        }
    }

    public Periode getMaksPeriodeInntekterFør() {
        if (inntekterFør.isEmpty()) {
            return null;
        } else {
            return new Periode(inntekterFør.firstKey().getFraOgMed(), inntekterFør.lastKey().getTilOgMed());
        }
    }

    public Map<Periode, PeriodeInntekt> getInntekterSøknadsperiode() {
        return inntekterSøknadsperiode;
    }

    public Map<Periode, PeriodeInntekt> getInntekterFør() {
        return inntekterFør;
    }

    public String getRegnskapsførerNavn() {
        return regnskapsførerNavn;
    }

    public String getRegnskapsførerTlf() {
        return regnskapsførerTlf;
    }

    public boolean getSøkerKompensasjon() {
        return søkerKompensasjon;
    }

    public static final class Builder {
        private Map<Periode, PeriodeInntekt> inntekterFør = new LinkedHashMap<>();
        private Map<Periode, PeriodeInntekt> inntekterSøknadsperiode = new LinkedHashMap<>();
        private boolean søkerKompensasjon = true;
        private String regnskapsførerNavn;
        private String regnskapsførerTlf;

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

        public Builder søkerKompensasjon(boolean søkerKompensasjon) {
            this.søkerKompensasjon = søkerKompensasjon;
            return this;
        }

        public Builder regnskapsfører(String navn, String tlf) {
            this.regnskapsførerNavn = navn;
            this.regnskapsførerTlf = tlf;
            return this;
        }

        public SelvstendigNæringsdrivende build() {
            return new SelvstendigNæringsdrivende(inntekterFør, inntekterSøknadsperiode, søkerKompensasjon, regnskapsførerNavn, regnskapsførerTlf);
        }
    }

}
