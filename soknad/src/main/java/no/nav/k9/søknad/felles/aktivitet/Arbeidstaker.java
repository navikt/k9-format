package no.nav.k9.søknad.felles.aktivitet;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPerioder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Arbeidstaker {

    @JsonProperty(value = "norskIdentitetsnummer")
    @Valid
    private NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonProperty(value = "organisasjonsnummer")
    @Valid
    private Organisasjonsnummer organisasjonsnummer;

    @JsonProperty(value = "arbeidstidInfo")
    @Valid
    private ArbeidstidInfo arbeidstidInfo;

    @JsonCreator
    public Arbeidstaker(@JsonProperty(value = "norskIdentitetsnummer") NorskIdentitetsnummer norskIdentitetsnummer,
                         @JsonProperty(value = "organisasjonsnummer") Organisasjonsnummer organisasjonsnummer,
                        @JsonProperty(value = "arbeidstidInfo") ArbeidstidInfo arbeidstidInfo) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
        this.organisasjonsnummer = organisasjonsnummer;
        this.arbeidstidInfo = arbeidstidInfo;
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

    public ArbeidstidInfo getArbeidstidInfo() {
        return arbeidstidInfo;
    }

    public void setArbeidstidInfo(ArbeidstidInfo arbeidstidInfo) {
        this.arbeidstidInfo = arbeidstidInfo;
    }
}
