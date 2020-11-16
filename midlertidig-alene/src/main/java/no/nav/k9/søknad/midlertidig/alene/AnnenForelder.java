package no.nav.k9.søknad.midlertidig.alene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnenForelder {
    @JsonProperty(value="norskIdentitetsnummer", required = true)
    public final NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonProperty(value="navn", required = true)
    public final String navn;

    @JsonProperty(value="situasjon", required = true)
    public final Situasjon situasjon;

    @JsonProperty(value = "situasjonBeskrivelse")
    public final String situasjonBeskrivelse;

    @JsonProperty(value = "periodeOver6Måneder")
    public final Boolean periodeOver6Måneder;

    @JsonProperty(value = "periodeFraOgMed")
    public final LocalDate periodeFraOgMed;

    @JsonProperty(value = "periodeTilOgMed")
    public final LocalDate periodeTilOgMed;

    @JsonCreator
    private AnnenForelder(
            @JsonProperty(value = "norskIdentitetsnummer", required = true) NorskIdentitetsnummer norskIdentitetsnummer,
            @JsonProperty(value = "navn", required = true) String navn,
            @JsonProperty(value = "situasjon", required = true) Situasjon situasjon,
            @JsonProperty(value = "situasjonBeskrivelse") String situasjonBeskrivelse,
            @JsonProperty(value = "periodeOver6Måneder") Boolean periodeOver6Måneder,
            @JsonProperty("periodeFraOgMed") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo") LocalDate periodeFraOgMed,
            @JsonProperty("periodeTilOgMed") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo") LocalDate periodeTilOgMed) {

        this.norskIdentitetsnummer = norskIdentitetsnummer;
        this.navn = navn;
        this.situasjon = situasjon;
        this.situasjonBeskrivelse = situasjonBeskrivelse;
        this.periodeOver6Måneder = periodeOver6Måneder;
        this.periodeFraOgMed = periodeFraOgMed;
        this.periodeTilOgMed = periodeTilOgMed;

    }

    public static AnnenForelder.Builder builder() {
        return new AnnenForelder.Builder();
    }

    public static final class Builder {
        private NorskIdentitetsnummer norskIdentitetsnummer;
        private String navn;
        private Situasjon situasjon;
        private String situasjonBeskrivelse;
        private Boolean periodeOver6Måneder;
        private LocalDate periodeFraOgMed;
        private LocalDate periodeTilOgMed;

        private Builder() {}

        public AnnenForelder.Builder norskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
            this.norskIdentitetsnummer = norskIdentitetsnummer;
            return this;
        }

        public AnnenForelder.Builder navn(String navn) {
            this.navn = navn;
            return this;
        }

        public AnnenForelder.Builder situasjon(Situasjon situasjon) {
            this.situasjon = situasjon;
            return this;
        }

        public AnnenForelder.Builder situasjonBeskrivelse(String situasjonBeskrivelse) {
            this.situasjonBeskrivelse = situasjonBeskrivelse;
            return this;
        }

        public AnnenForelder.Builder periodeOver6Måneder(Boolean periodeOver6Måneder) {
            this.periodeOver6Måneder = periodeOver6Måneder;
            return this;
        }

        public AnnenForelder.Builder periodeFraOgMed(LocalDate periodeFraOgMed) {
            this.periodeFraOgMed = periodeFraOgMed;
            return this;
        }

        public AnnenForelder.Builder periodeTilOgMed(LocalDate periodeTilOgMed) {
            this.periodeTilOgMed = periodeTilOgMed;
            return this;
        }


        public AnnenForelder build() {
            return new AnnenForelder(
                    norskIdentitetsnummer,
                    navn,
                    situasjon,
                    situasjonBeskrivelse,
                    periodeOver6Måneder,
                    periodeFraOgMed,
                    periodeTilOgMed);
        }
    }

}