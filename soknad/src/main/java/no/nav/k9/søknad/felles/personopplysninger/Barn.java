package no.nav.k9.søknad.felles.personopplysninger;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.felles.type.PersonIdent;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Barn implements Person {

    @JsonProperty(value = "norskIdentitetsnummer")
    @Valid
    public final NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonProperty(value = "fødselsdato")
    @Valid
    public final LocalDate fødselsdato;

    public static Builder builder() {
        return new Builder();
    }

    @JsonCreator
    public Barn(
                @JsonProperty("norskIdentitetsnummer") NorskIdentitetsnummer norskIdentitetsnummer,
                @JsonProperty("fødselsdato") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo") LocalDate fødselsdato) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
        this.fødselsdato = fødselsdato;
    }

    @Override
    public PersonIdent getPersonIdent() {
        return norskIdentitetsnummer;
    }

    @AssertTrue(message = "Enten fnr/dnr eller fødselsdato må oppgis")
    private boolean isFnrEllerFødselsdato() {
        return getPersonIdent() != null || fødselsdato != null;
    }

    public static final class Builder {
        private NorskIdentitetsnummer norskIdentitetsnummer;
        private LocalDate fødselsdato;

        private Builder() {
        }

        public Builder norskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
            this.norskIdentitetsnummer = norskIdentitetsnummer;
            return this;
        }

        public Builder fødselsdato(LocalDate fødselsdato) {
            this.fødselsdato = fødselsdato;
            return this;
        }

        public Barn build() {
            return new Barn(norskIdentitetsnummer, fødselsdato);
        }
    }
}
