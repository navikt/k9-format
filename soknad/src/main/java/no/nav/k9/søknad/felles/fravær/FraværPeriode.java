package no.nav.k9.søknad.felles.fravær;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotNull;
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
    @NotNull
    @JsonProperty("periode")
    private Periode periode;

    @JsonProperty("duration")
    private Duration duration; //OBS sattes tidligere direkte ved delvis fravær, men utledes nå fra verdier i feltet delvisFravær. Indikerer hvor mye fravær det er, etter at det er omregnet til 7.5 times normalarbeidsdag.

    @Valid
    @JsonProperty("delvisFravær")
    private DelvisFravær delvisFravær; //denne brukes hvis det er mindre enn 100% fravær fra arbeid i aktuell periode

    @Valid
    @JsonProperty(value = "årsak", required = true)
    private FraværÅrsak årsak;

    @Valid
    @JsonProperty(value = "søknadÅrsak")
    private SøknadÅrsak søknadÅrsak;

    @Valid
    @Size(min = 1, max = 2)
    @JsonProperty(value = "aktivitetFravær", required = true)
    private List<AktivitetFravær> aktivitetFravær;

    @JsonProperty(value = "arbeidsgiverOrgNr")
    @Valid
    private Organisasjonsnummer arbeidsgiverOrgNr;

    @JsonProperty(value = "arbeidsforholdId")
    @Valid
    private String arbeidsforholdId;

    public FraværPeriode() {
    }

    @JsonCreator
    public FraværPeriode(
            @JsonProperty("periode") @Valid Periode periode,
            @JsonProperty("duration") Duration duration,
            @JsonProperty("delvisFravær") DelvisFravær delvisFravær,
            @JsonProperty("årsak") FraværÅrsak årsak,
            @JsonProperty("søknadÅrsak") SøknadÅrsak søknadÅrsak,
            @JsonProperty("aktivitetFravær") List<AktivitetFravær> aktivitetFravær,
            @JsonProperty("organisasjonsnummer") Organisasjonsnummer arbeidsgiverOrgNr,
            @JsonProperty("arbeidsforholdId") String arbeidsforholdId) {
        this.periode = periode;
        this.duration = duration;
        this.delvisFravær = delvisFravær;
        this.årsak = årsak;
        this.søknadÅrsak = søknadÅrsak;
        this.aktivitetFravær = aktivitetFravær.stream().sorted().toList(); //sorterer for å få enklere equals og hashcode
        this.arbeidsgiverOrgNr = arbeidsgiverOrgNr;
        this.arbeidsforholdId = arbeidsforholdId;
    }

    @AssertFalse(message = "Mangler fra-og-med-dato for perioden")
    boolean isPeriodeManglerFom(){
        return periode != null && periode.getFraOgMed() == null;
    }

    @AssertFalse(message = "Mangler til-og-med-dato for perioden")
    boolean isPeriodeManglerTom(){
        return periode != null && periode.getTilOgMed() == null;
    }

    public FraværPeriode medPeriode(Periode periode) {
        this.periode = periode;
        return this;
    }

    public FraværPeriode medSøknadsårsak(SøknadÅrsak søknadÅrsak) {
        this.søknadÅrsak = søknadÅrsak;
        return this;
    }

    public FraværPeriode medFraværÅrsak(FraværÅrsak fraværÅrsak) {
        this.årsak = fraværÅrsak;
        return this;
    }

    public FraværPeriode medNulling() {
        this.delvisFravær = null;
        this.duration = Duration.ZERO;
        return this;
    }

    public FraværPeriode medDelvisFravær(DelvisFravær delvisFravær) {
        this.delvisFravær = delvisFravær;
        oppdaterDuration();
        return this;
    }

    public FraværPeriode medNormalarbeidstid(Duration normalarbeidstid) {
        DelvisFravær nyDelvisFravær = new DelvisFravær(normalarbeidstid, this.delvisFravær != null ? this.delvisFravær.getFravær() : null);
        this.delvisFravær = nyDelvisFravær.getNormalarbeidstid() == null && nyDelvisFravær.getFravær() == null
                ? null
                : nyDelvisFravær;
        oppdaterDuration();
        return this;
    }

    public FraværPeriode medFravær(Duration fravær) {
        DelvisFravær nyDelvisFravær = new DelvisFravær(this.delvisFravær != null ? this.delvisFravær.getNormalarbeidstid() : null, fravær);
        this.delvisFravær = nyDelvisFravær.getNormalarbeidstid() == null && nyDelvisFravær.getFravær() == null
                ? null
                : nyDelvisFravær;
        oppdaterDuration();
        return this;
    }

    private void oppdaterDuration() {
        this.duration = delvisFravær == null || delvisFravær.getFravær() == null || delvisFravær.getNormalarbeidstid() == null || delvisFravær.getNormalarbeidstid().isZero()
                ? null
                : delvisFravær.normalisertTilStandarddag();
    }

    public FraværPeriode medAktivitetFravær(List<AktivitetFravær> aktivitetFravær) {
        this.aktivitetFravær = aktivitetFravær.stream().sorted().toList(); //sorterer for enklere equals
        return this;
    }

    public FraværPeriode medArbeidsgiverOrgNr(Organisasjonsnummer arbeidsgiverOrgNr) {
        this.arbeidsgiverOrgNr = arbeidsgiverOrgNr;
        return this;
    }

    public FraværPeriode medArbeidsforholdId(String arbeidsforholdId) {
        this.arbeidsforholdId = arbeidsforholdId;
        return this;
    }

    public Periode getPeriode() {
        return periode;
    }

    public Duration getDuration() {
        return duration;
    }

    public DelvisFravær getDelvisFravær() {
        return delvisFravær;
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
                Objects.equals(delvisFravær, that.delvisFravær) &&
                Objects.equals(årsak, that.årsak) &&
                Objects.equals(aktivitetFravær, that.aktivitetFravær) &&
                Objects.equals(søknadÅrsak, that.søknadÅrsak) &&
                Objects.equals(arbeidsgiverOrgNr, that.arbeidsgiverOrgNr) &&
                Objects.equals(arbeidsforholdId, that.arbeidsforholdId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periode, duration, delvisFravær, årsak, aktivitetFravær, søknadÅrsak, arbeidsgiverOrgNr, arbeidsforholdId);
    }

    @Override
    public int compareTo(FraværPeriode b) {
        return this.getPeriode().compareTo(b.getPeriode());
    }

    @Override
    public String toString() {
        return "FraværPeriode{" +
                "periode=" + periode +
                (duration != null ? ", duration=" + duration : "") +
                (delvisFravær != null ? ", delvisFravær=" + delvisFravær : "") +
                ", årsak=" + årsak +
                ", søknadÅrsak=" + søknadÅrsak +
                ", fraværFraAktivitet=" + aktivitetFravær +
                (arbeidsgiverOrgNr != null ? ", arbeidsgiverOrgNr=MASKERT" : "") +
                (arbeidsforholdId != null ? ", arbeidsforholdId=MASKERT" : "") +
                '}';
    }

}
