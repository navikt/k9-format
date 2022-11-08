package no.nav.k9.søknad.felles.personopplysninger;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.felles.type.PersonIdent;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Barn implements Person {

    @JsonAlias({ "fødselsnummer", "norskIdentifikator", "identitetsnummer", "fnr" })
    @JsonProperty(value = "norskIdentitetsnummer", required = true)
    @Valid
    private NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonProperty(value = "fødselsdato", required = false)
    @Valid
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo")
    @PastOrPresent(message = "[ugyldigFødselsdato] Fødselsdato kan ikke være fremtidig")
    private LocalDate fødselsdato;

    @Override
    public PersonIdent getPersonIdent() {
        return norskIdentitetsnummer;
    }

    public LocalDate getFødselsdato() {
        return fødselsdato;
    }

    public Barn medNorskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
        return this;
    }

    public Barn medFødselsdato(LocalDate fødselsdato) {
        this.fødselsdato = fødselsdato;
        return this;
    }

    @AssertTrue(message = "norskIdentitetsnummer eller fødselsdato må være satt")
    private boolean isOk() {
        return (norskIdentitetsnummer != null && norskIdentitetsnummer.getVerdi() != null)
                || (fødselsdato != null);
    }

    /* Deaktivert pga søknader med feil
    @AssertFalse(message = "[ikkeEntydig] Ikke entydig, må oppgi enten fnr/dnr eller fødselsdato.")
    private boolean isEntydig() {
        return this.getPersonIdent() != null && this.fødselsdato != null;
    }
     */

    @Override
    public int hashCode() {
        return Objects.hash(norskIdentitetsnummer, fødselsdato);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !getClass().equals(obj.getClass()))
            return false;
        var other = (Barn) obj;
        return Objects.equals(getPersonIdent(), other.getPersonIdent())
            && Objects.equals(getFødselsdato(), other.getFødselsdato());
    }

}
