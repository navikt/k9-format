package no.nav.k9.søknad.felles.type;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.AssertFalse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;

public class Periode implements Comparable<Periode> {
    private static final String ÅPEN = "..";
    private static final String SKILLE = "/";

    private final LocalDate fraOgMed;

    private final LocalDate tilOgMed;

    @JsonValue
    private final String iso8601;

    public static Periode parse(String iso8601) {
        return new Periode(iso8601);
    }

    @JsonCreator
    public Periode(String iso8601) {
        verifiserKanVæreGyldigPeriode(iso8601);
        String[] split = iso8601.split(SKILLE);
        this.fraOgMed = parseLocalDate(split[0]);
        this.tilOgMed = parseLocalDate(split[1]);
        this.iso8601 = iso8601;
    }

    public Periode(LocalDate fraOgMed, LocalDate tilOgMed) {
        this.fraOgMed = fraOgMed;
        this.tilOgMed = tilOgMed;
        this.iso8601 = toIso8601(fraOgMed) + SKILLE + toIso8601(tilOgMed);
    }

    @AssertFalse(message = "[ugyldigPeriode] Fra og med (FOM) må være før eller lik til og med (TOM).", payload = AvbrytendeValideringsfeil.class)
    public boolean isTilOgMedFørFraOgMed() {
        return fraOgMed != null && tilOgMed != null && tilOgMed.isBefore(fraOgMed);
    }

    public LocalDate getFraOgMed() {
        return fraOgMed;
    }

    public LocalDate getTilOgMed() {
        return tilOgMed;
    }

    public String getIso8601() {
        return iso8601;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        var periode = (Periode) o;
        return Objects.equals(fraOgMed, periode.fraOgMed)
                && Objects.equals(tilOgMed, periode.tilOgMed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fraOgMed, tilOgMed);
    }

    @Override
    public String toString() {
        return iso8601;
    }

    @Override
    public int compareTo(Periode o) {
        return this.iso8601.compareTo(o.iso8601);
    }

    /**
     * Sjekk om denne perioden inneholder (omslutter) angitt periode.
     */
    public boolean inneholder(Periode periode) {
        return (this.fraOgMed == null || (periode.fraOgMed != null && !this.fraOgMed.isAfter(periode.fraOgMed)))
                && (this.tilOgMed == null || (periode.tilOgMed != null && !this.tilOgMed.isBefore(periode.tilOgMed)));
    }

    private static void verifiserKanVæreGyldigPeriode(String iso8601) {
        if (iso8601 == null || iso8601.split(SKILLE).length != 2) {
            throw new IllegalArgumentException("Periode på ugylig format '" + iso8601 + "'.");
        }
    }

    private static LocalDate parseLocalDate(String iso8601) {
        if (ÅPEN.equals(iso8601))
            return null;
        else
            return LocalDate.parse(iso8601);
    }

    private static String toIso8601(LocalDate dato) {
        if (dato == null)
            return Periode.ÅPEN;
        else
            return dato.toString();
    }
}
