package no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.felles.type.PersonIdent;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class AnnenForelder implements Person {

    @JsonAlias({ "fødselsnummer" })
    @JsonProperty(value = "norskIdentitetsnummer", required = true)
    @NotNull
    @Valid
    private NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonProperty(value = "situasjon", required = true)
    @Valid
    @NotNull
    private SituasjonType situasjonType;

    @JsonProperty(value = "situasjonBeskrivelse")
    @Pattern(regexp = "^[\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}«»\\u2019\\u201C]+$", message = "[${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String situasjonBeskrivelse;

    @JsonProperty(value = "periode")
    @Valid
    private Periode periode;

    public AnnenForelder() {
        //
    }

    @JsonCreator
    public AnnenForelder(
                         @JsonProperty(value = "norskIdentitetsnummer", required = true) @NotNull NorskIdentitetsnummer norskIdentitetsnummer,
                         @JsonProperty(value = "situasjon", required = true) @Valid @NotNull SituasjonType situasjon,
                         @JsonProperty(value = "situasjonBeskrivelse") String beskrivelse,
                         @JsonProperty(value = "periode") @Valid Periode periode) {
        this(norskIdentitetsnummer);
        this.situasjonType = situasjon;
        this.situasjonBeskrivelse = beskrivelse;
        this.periode = periode;

    }

    public AnnenForelder(NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = Objects.requireNonNull(norskIdentitetsnummer, "norskIdentitetsnummer");
    }

    public AnnenForelder medNorskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = Objects.requireNonNull(norskIdentitetsnummer, "norskIdentitetsnummer");
        return this;
    }

    public AnnenForelder medSituasjon(SituasjonType situasjonType, String beskrivelse) {
        this.situasjonType = situasjonType;
        this.situasjonBeskrivelse = beskrivelse;
        return this;
    }

    public AnnenForelder medPeriode(Periode periode) {
        this.periode = Objects.requireNonNull(periode, "periode");
        return this;
    }

    public AnnenForelder medPeriode(LocalDate fom, LocalDate tom) {
        return medPeriode(new Periode(fom, tom));
    }

    @Override
    public int hashCode() {
        return Objects.hash(norskIdentitetsnummer);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !getClass().equals(obj.getClass()))
            return false;
        var other = (Barn) obj;
        return Objects.equals(getPersonIdent(), other.getPersonIdent());
    }

    @Override
    public PersonIdent getPersonIdent() {
        return norskIdentitetsnummer;
    }

    public String getSituasjonBeskrivelse() {
        return situasjonBeskrivelse;
    }

    public SituasjonType getSituasjonType() {
        return situasjonType;
    }

    public Periode getPeriode() {
        return periode;
    }

    public static enum SituasjonType {
        INNLAGT_I_HELSEINSTITUSJON,
        UTØVER_VERNEPLIKT,
        FENGSEL,
        SYKDOM,
        ANNET,
        ;
    }
}
