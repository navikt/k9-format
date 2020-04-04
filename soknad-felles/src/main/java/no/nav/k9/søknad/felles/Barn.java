package no.nav.k9.søknad.felles;

import java.time.LocalDate;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Barn {

    @Valid
    public final NorskIdentitetsnummer norskIdentitetsnummer;

    @Valid
    public final LocalDate fødselsdato;

    public static Builder builder() {
        return new Builder();
    }

    @JsonCreator
    private Barn(
            @JsonProperty("norskIdentitetsnummer")
            NorskIdentitetsnummer norskIdentitetsnummer,
            @JsonProperty("fødselsdato")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo")
            LocalDate fødselsdato) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
        this.fødselsdato = fødselsdato;
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
