package no.nav.k9.søknad.felles.fravær;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class FraværPeriode implements Comparable<FraværPeriode> {

    @Valid
    @JsonProperty("periode")
    private final Periode periode;
    @JsonProperty("duration")
    private final Duration duration;

    @Valid
    @JsonProperty(value = "årsak", required = true)
    private final FraværÅrsak årsak;

    @Valid
    @JsonProperty(value = "søknadÅrsak")
    private SøknadÅrsak søknadÅrsak;

    @Valid
    @Size(min = 1, max = 2)
    @JsonProperty(value = "aktivitetFravær", required = true)
    private final List<AktivitetFravær> aktivitetFravær;

    @JsonProperty(value = "arbeidsgiverOrgNr")
    @Valid
    private Organisasjonsnummer arbeidsgiverOrgNr;

    @JsonProperty(value = "arbeidsforholdId")
    @Valid
    private String arbeidsforholdId;

    @JsonCreator
    public FraværPeriode(
            @JsonProperty("periode") @Valid Periode periode,
            @JsonProperty("duration") Duration duration,
            @JsonProperty("årsak") FraværÅrsak årsak,
            @JsonProperty("søknadÅrsak") SøknadÅrsak søknadÅrsak,
            @JsonProperty("aktivitetFravær") List<AktivitetFravær> aktivitetFravær,
            @JsonProperty("organisasjonsnummer") Organisasjonsnummer arbeidsgiverOrgNr,
            @JsonProperty("arbeidsforholdId") String arbeidsforholdId) {
        this.periode = periode;
        this.duration = duration;
        this.årsak = årsak;
        this.søknadÅrsak = søknadÅrsak;
        this.aktivitetFravær = aktivitetFravær.stream().sorted().collect(Collectors.toList()); //sorterer for å få enklere equals og hashcode
        this.arbeidsgiverOrgNr = arbeidsgiverOrgNr;
        this.arbeidsforholdId = arbeidsforholdId;
    }

    public Periode getPeriode() {
        return periode;
    }

    public Duration getDuration() {
        return duration;
    }

    public FraværÅrsak getÅrsak() {
        return årsak;
    }

    public SøknadÅrsak getSøknadÅrsak() {
        return søknadÅrsak;
    }

    public List<AktivitetFravær> getAktivitetFravær() {
        return aktivitetFravær;
    }

    public Organisasjonsnummer getArbeidsgiverOrgNr() {
        return arbeidsgiverOrgNr;
    }

    public String getArbeidsforholdId() {
        return arbeidsforholdId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FraværPeriode that = (FraværPeriode) o;
        return periode.equals(that.periode) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(årsak, that.årsak) &&
                Objects.equals(aktivitetFravær, that.aktivitetFravær) &&
                Objects.equals(arbeidsgiverOrgNr, that.arbeidsgiverOrgNr) &&
                Objects.equals(aktivitetFravær, that.arbeidsforholdId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periode, duration, årsak, aktivitetFravær, arbeidsgiverOrgNr, arbeidsforholdId);
    }

    @Override
    public int compareTo(FraværPeriode b) {
        return this.getPeriode().compareTo(b.getPeriode());
    }

    @Override
    public String toString() {
        return "FraværPeriode{" +
                "periode=" + periode +
                ", duration=" + duration +
                ", årsak=" + årsak +
                ", søknadÅrsak=" + søknadÅrsak +
                ", fraværFraAktivitet=" + aktivitetFravær +
                (arbeidsgiverOrgNr != null ? ", arbeidsgiverOrgNr=MASKERT" : "") +
                (arbeidsforholdId != null ? ", arbeidsforholdId=MASKERT" : "") +
                '}';
    }

}
