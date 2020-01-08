package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Barn {

    public final NorskIdentitetsnummer norskIdentitetsnummer;

    public final LocalDate foedselsdato;

    public static Builder builder() {
        return new Builder();
    }

    @JsonCreator
    private Barn(
            @JsonProperty("norsk_identitetsnummer")
            NorskIdentitetsnummer norskIdentitetsnummer,
            @JsonProperty("foedselsdato")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo")
            LocalDate foedselsdato) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
        this.foedselsdato = foedselsdato;
    }

    public static final class Builder {
        private NorskIdentitetsnummer norskIdentitetsnummer;
        private LocalDate foedselsdato;

        private Builder() {
        }

        public Builder norskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
            this.norskIdentitetsnummer = norskIdentitetsnummer;
            return this;
        }

        public Builder foedselsdato(LocalDate foedselsdato) {
            this.foedselsdato = foedselsdato;
            return this;
        }

        public Barn build() {
            return new Barn(norskIdentitetsnummer, foedselsdato);
        }
    }
}
