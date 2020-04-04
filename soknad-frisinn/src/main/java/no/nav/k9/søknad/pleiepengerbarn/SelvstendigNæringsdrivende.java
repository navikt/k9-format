package no.nav.k9.søknad.pleiepengerbarn;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.Organisasjonsnummer;
import no.nav.k9.søknad.felles.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class SelvstendigNæringsdrivende {

    /** dato inntektstap startet. Påkrevd kun første søknad. */
    @JsonInclude(value = Include.NON_EMPTY)
    @JsonAlias("skjæringstidspunkt")
    @JsonProperty(value = "inntektstapStartet")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo")
    @Valid
    private LocalDate inntektstapStartet;

    @JsonInclude(value = Include.NON_EMPTY)
    @JsonProperty(value = "inntekterFør")
    @Valid
    private Map<Periode, PeriodeInntekt> inntekterFør;

    @JsonInclude(value = Include.ALWAYS)
    @JsonProperty(value = "inntekterEtter", required = true)
    @Valid
    private Map<Periode, PeriodeInntekt> inntekterEtter;

    @JsonProperty(value = "orgnummer", required = true)
    @Valid
    @NotNull
    private Organisasjonsnummer orgnummer;

    @JsonCreator
    public SelvstendigNæringsdrivende(@JsonProperty(value = "orgnummer", required = true) Organisasjonsnummer orgnummer,
                                       @JsonProperty(value = "inntektstapStartet", required = true) LocalDate inntekstapStartet,
                                       @JsonProperty(value = "inntekterFør") Map<Periode, PeriodeInntekt> inntekterFør,
                                       @JsonProperty(value = "inntekterEtter") Map<Periode, PeriodeInntekt> inntekterEtter) {
        this.orgnummer = Objects.requireNonNull(orgnummer, "orgnummer");
        this.inntektstapStartet = inntekstapStartet;
        this.inntekterEtter = (inntekterEtter == null) ? emptyMap() : unmodifiableMap(inntekterEtter);
        this.inntekterFør = (inntekterFør == null) ? emptyMap() : unmodifiableMap(inntekterFør);

    }

    public Map<Periode, PeriodeInntekt> getInntekterEtter() {
        return inntekterEtter;
    }

    public Map<Periode, PeriodeInntekt> getInntekterFør() {
        return inntekterFør;
    }

    public LocalDate getInntektstapStartet() {
        return inntektstapStartet;
    }

    public Organisasjonsnummer getOrgnummer() {
        return orgnummer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, PeriodeInntekt> inntekterFør = new LinkedHashMap<>();
        private Map<Periode, PeriodeInntekt> inntekterEtter = new LinkedHashMap<>();
        private LocalDate inntektstapStartet;
        private Organisasjonsnummer orgnummer;

        private Builder() {
        }

        public Builder inntekterFør(Map<Periode, PeriodeInntekt> inntekter) {
            inntekterFør.putAll(inntekter);
            return this;
        }

        public Builder inntekterEtter(Map<Periode, PeriodeInntekt> inntekter) {
            inntekterEtter.putAll(inntekter);
            return this;
        }

        public Builder orgnummer(Organisasjonsnummer orgnummer) {
            this.orgnummer = orgnummer;
            return this;
        }

        public Builder orgnummer(String orgnummer) {
            this.orgnummer = Organisasjonsnummer.of(orgnummer);
            return this;
        }
        
        public Builder inntektstapStartet(LocalDate dato) {
            this.inntektstapStartet = dato;
            return this;
        }

        public SelvstendigNæringsdrivende build() {
            return new SelvstendigNæringsdrivende(orgnummer, inntektstapStartet, inntekterFør, inntekterEtter);
        }
    }

}
