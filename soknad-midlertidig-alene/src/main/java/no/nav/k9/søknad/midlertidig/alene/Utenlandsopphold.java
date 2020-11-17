package no.nav.k9.søknad.midlertidig.alene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Utenlandsopphold {
    @JsonProperty(value = "fraOgMed", required = true)
    @Valid
    public final LocalDate fraOgMed;

    @JsonProperty(value = "tilOgMed", required = true)
    @Valid
    public final LocalDate tilOgMed;

    @JsonProperty(value="landnavn", required = true)
    public final String landnavn;


    @JsonCreator
    private Utenlandsopphold(
            @JsonProperty(value = "landnavn", required = true) String landnavn,
            @JsonProperty(value = "fraOgMed",required = true) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo") LocalDate fraOgMed,
            @JsonProperty(value = "tilOgMed",required = true) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo") LocalDate tilOgMed) {

        this.landnavn = landnavn;
        this.fraOgMed = fraOgMed;
        this.tilOgMed = tilOgMed;
    }

    public static Utenlandsopphold.Builder builder() {
        return new Utenlandsopphold.Builder();
    }

    public static final class Builder {
        private String landnavn;
        private LocalDate fraOgMed;
        private LocalDate tilOgMed;

        private Builder() {}

        public Utenlandsopphold.Builder landnavn(String landnavn) {
            this.landnavn = landnavn;
            return this;
        }

        public Utenlandsopphold.Builder fraOgMed(LocalDate fraOgMed) {
            this.fraOgMed = fraOgMed;
            return this;
        }

        public Utenlandsopphold.Builder tilOgMed(LocalDate tilOgMed) {
            this.tilOgMed = tilOgMed;
            return this;
        }

        public Utenlandsopphold build() {
            return new Utenlandsopphold(
                    landnavn,
                    fraOgMed,
                    tilOgMed);
        }
    }

}