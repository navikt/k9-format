package no.nav.k9.søknad.pleiepengerbarn;

import static java.util.Collections.emptyNavigableMap;
import static java.util.Collections.unmodifiableNavigableMap;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

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
    private NavigableMap<Periode, PeriodeInntekt> inntekterFør;

    @JsonInclude(value = Include.ALWAYS)
    @JsonProperty(value = "inntekterEtter", required = true)
    @Valid
    private NavigableMap<Periode, PeriodeInntekt> inntekterEtter;

    @JsonProperty(value = "organisasjonsnummer", required = true)
    @Valid
    @NotNull
    private Organisasjonsnummer organisasjonsnummer;

    @JsonCreator
    public SelvstendigNæringsdrivende(@JsonProperty(value = "organisasjonsnummer", required = true) Organisasjonsnummer organisasjonsnummer,
                                       @JsonProperty(value = "inntektstapStartet", required = true) LocalDate inntekstapStartet,
                                       @JsonProperty(value = "inntekterFør") Map<Periode, PeriodeInntekt> inntekterFør,
                                       @JsonProperty(value = "inntekterEtter") Map<Periode, PeriodeInntekt> inntekterEtter) {
        this.organisasjonsnummer = Objects.requireNonNull(organisasjonsnummer, "organisasjonsnummer");
        this.inntektstapStartet = inntekstapStartet;
        this.inntekterEtter = (inntekterEtter == null) ? emptyNavigableMap() : unmodifiableNavigableMap(new TreeMap<>(inntekterEtter));
        this.inntekterFør = (inntekterFør == null) ? emptyNavigableMap() : unmodifiableNavigableMap(new TreeMap<>(inntekterFør));

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

    public Organisasjonsnummer getorganisasjonsnummer() {
        return organisasjonsnummer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<Periode, PeriodeInntekt> inntekterFør = new LinkedHashMap<>();
        private Map<Periode, PeriodeInntekt> inntekterEtter = new LinkedHashMap<>();
        private LocalDate inntektstapStartet;
        private Organisasjonsnummer organisasjonsnummer;

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

        public Builder organisasjonsnummer(Organisasjonsnummer organisasjonsnummer) {
            this.organisasjonsnummer = organisasjonsnummer;
            return this;
        }

        public Builder organisasjonsnummer(String organisasjonsnummer) {
            this.organisasjonsnummer = Organisasjonsnummer.of(organisasjonsnummer);
            return this;
        }
        
        public Builder inntektstapStartet(LocalDate dato) {
            this.inntektstapStartet = dato;
            return this;
        }

        public SelvstendigNæringsdrivende build() {
            return new SelvstendigNæringsdrivende(organisasjonsnummer, inntektstapStartet, inntekterFør, inntekterEtter);
        }
    }

}
