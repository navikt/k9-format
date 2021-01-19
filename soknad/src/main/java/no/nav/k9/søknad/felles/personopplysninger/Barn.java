package no.nav.k9.søknad.felles.personopplysninger;

import com.fasterxml.jackson.annotation.*;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.PersonIdent;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Barn {

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

    public PersonIdent getPersonIdent() {
        return norskIdentitetsnummer;
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
