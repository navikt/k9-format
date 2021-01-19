package no.nav.k9.søknad.ytelse.psb.v1.arbeid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.aktivitet.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.ArbeidsforholdId;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arbeidstaker {

    @JsonProperty(value = "norskIdentitetsnummer")
    @Valid
    private NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonProperty(value = "organisasjonsnummer")
    @Valid
    private Organisasjonsnummer organisasjonsnummer;

    @JsonProperty(value = "arbeidsforholdId")
    @Valid
    private ArbeidsforholdId arbeidsforholdId;

    @JsonInclude(value = Include.NON_NULL)
    @JsonProperty(value = "perioder")
    @Valid
    @NotEmpty
    private Map<Periode, ArbeidPeriodeInfo> perioder;

    @JsonCreator
    public Arbeidstaker(@JsonProperty(value = "norskIdentitetsnummer") @Valid NorskIdentitetsnummer norskIdentitetsnummer,
                        @JsonProperty(value = "organisasjonsnummer") @Valid Organisasjonsnummer organisasjonsnummer,
                        @JsonProperty(value = "arbeidsforholdId") @Valid ArbeidsforholdId arbeidsforholdId,
                        @JsonProperty(value = "perioder") @Valid @NotEmpty Map<Periode, ArbeidPeriodeInfo> perioder) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
        this.organisasjonsnummer = organisasjonsnummer;
        this.arbeidsforholdId = arbeidsforholdId;
        this.perioder = perioder;
    }

    public NorskIdentitetsnummer getNorskIdentitetsnummer() {
        return norskIdentitetsnummer;
    }

    public void setNorskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
    }

    public Organisasjonsnummer getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    public void setOrganisasjonsnummer(Organisasjonsnummer organisasjonsnummer) {
        this.organisasjonsnummer = organisasjonsnummer;
    }

    public Map<Periode, ArbeidPeriodeInfo> getPerioder() {
        return perioder;
    }

    public void setPerioder(Map<Periode, ArbeidPeriodeInfo> perioder) {
        this.perioder = perioder;
    }

    public ArbeidsforholdId getArbeidsforholdId() {
        return arbeidsforholdId;
    }

    public void setArbeidsforholdId(ArbeidsforholdId arbeidsforholdId) {
        this.arbeidsforholdId = arbeidsforholdId;
    }
}
