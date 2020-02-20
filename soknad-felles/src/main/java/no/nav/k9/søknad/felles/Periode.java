package no.nav.k9.søknad.felles;

import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalDate;
import java.util.Objects;

public class Periode {
    static final String ÅPEN = "..";
    static final String SKILLE = "/";

    public final LocalDate fraOgMed;

    public final LocalDate tilOgMed;

    @JsonValue
    public final String iso8601;

    private Periode(String iso8601) {
        verifiserKanVæreGyldigPeriode(iso8601);
        String[] split = iso8601.split(SKILLE);
        this.fraOgMed = parse(split[0]);
        this.tilOgMed = parse(split[1]);
        this.iso8601 = iso8601;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Periode periode = (Periode) o;

        if (!Objects.equals(fraOgMed, periode.fraOgMed)) return false;
        return Objects.equals(tilOgMed, periode.tilOgMed);
    }

    @Override
    public int hashCode() {
        int result = fraOgMed != null ? fraOgMed.hashCode() : 0;
        result = 31 * result + (tilOgMed != null ? tilOgMed.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return iso8601;
    }

    private static void verifiserKanVæreGyldigPeriode(String iso8601) {
        if (iso8601 == null || iso8601.split(SKILLE).length != 2) {
            throw new IllegalArgumentException("Periode på ugylig format '" + iso8601 + "'.");
        }
    }

    private static LocalDate parse(String iso8601) {
        if (ÅPEN.equals(iso8601)) return null;
        else return LocalDate.parse(iso8601);
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

        public Builder enkeltDag(LocalDate enkeltDag) {
            this.fraOgMed = enkeltDag;
            this.tilOgMed = enkeltDag;
            return this;
        }

        private String toIso8601(LocalDate dato) {
            if (dato == null) return Periode.ÅPEN;
            else return dato.toString();
        }

        public Periode build() {
            return new Periode(toIso8601(fraOgMed) + SKILLE + toIso8601(tilOgMed));
        }
    }
}
