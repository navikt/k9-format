package no.nav.k9.søknad.ytelse.pls.v1;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;

import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.felles.type.PersonIdent;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Pleietrengende implements Person {

    @JsonAlias({ "fødselsnummer", "norskIdentifikator", "identitetsnummer", "fnr" })
    @JsonProperty(value = "norskIdentitetsnummer")
    @Valid
    private NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonProperty(value = "fødselsdato")
    @Valid
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo")
    @PastOrPresent(message = "[ugyldigFødselsdato] Fødselsdato kan ikke være fremtidig")
    private LocalDate fødselsdato;

    @JsonProperty("ukjent")
    private boolean ukjent = false;

    @Deprecated
    public Pleietrengende(@JsonProperty(value = "norskIdentitetsnummer") @JsonAlias({ "fødselsnummer", "norskIdentifikator", "identitetsnummer", "fnr" }) NorskIdentitetsnummer norskIdentitetsnummer,
                          @JsonProperty("fødselsdato") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo") LocalDate fødselsdato) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
        this.fødselsdato = fødselsdato;
    }

    public Pleietrengende() {
    }

    @Override
    public PersonIdent getPersonIdent() {
        return norskIdentitetsnummer;
    }

    public LocalDate getFødselsdato() {
        return fødselsdato;
    }

    public boolean isUkjent() {
        return ukjent;
    }

    public Pleietrengende medUkjentPleietrengende() {
        this.ukjent = true;
        return this;
    }

    public Pleietrengende medNorskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = Objects.requireNonNull(norskIdentitetsnummer, "norskIdentitetsnummer");
        return this;
    }

    public Pleietrengende medFødselsdato(LocalDate fødselsdato) {
        this.fødselsdato = Objects.requireNonNull(fødselsdato, "fødselsdato");
        return this;
    }

    @AssertTrue(message = "norskIdentitetsnummer eller fødselsdato må være satt")
    private boolean isOk() {
        return (norskIdentitetsnummer != null && norskIdentitetsnummer.getVerdi() != null)
                || (fødselsdato != null)
                ||  ukjent;
    }

    @AssertFalse(message = "[ikkeEntydig] Ikke entydig, må oppgi enten fnr/dnr eller fødselsdato.")
    private boolean isEntydig() {
        return this.getPersonIdent() != null && this.fødselsdato != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var other = (Pleietrengende) obj;

        return Objects.equals(norskIdentitetsnummer, other.norskIdentitetsnummer)
                && Objects.equals(fødselsdato, other.fødselsdato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(norskIdentitetsnummer, fødselsdato);
    }

}
