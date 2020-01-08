package no.nav.k9.soknad.felles;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Periode {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo")
    public final LocalDate fraOgMed;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo")
    public final LocalDate tilOgMed;

    private Periode(
            @JsonProperty("fraOgMed")
            LocalDate fraOgMed,
            @JsonProperty("tilOgMed")
            LocalDate tilOgMed) {
        this.fraOgMed = fraOgMed;
        this.tilOgMed = tilOgMed;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private LocalDate fraOgMed;
        private LocalDate tilOgMed;

        private Builder() {}

        public Builder fraOgMed(LocalDate fraOgMed) {
            this.fraOgMed = fraOgMed;
            return this;
        }

        public Builder tilOgMed(LocalDate tilOgMed) {
            this.tilOgMed = tilOgMed;
            return this;
        }

        public Periode build() {
            return new Periode(fraOgMed, tilOgMed);
        }
    }
}
