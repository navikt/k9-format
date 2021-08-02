package no.nav.k9.søknad.ytelse.psb.v1;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public class LukketPeriode implements Comparable<LukketPeriode> {
    static final String ÅPEN = "..";
    static final String SKILLE = "/";

    @Valid
    @NotNull
    private LocalDate fraOgMed;

    @Valid
    @NotNull
    private LocalDate tilOgMed;

    @Valid
    @NotNull
    @JsonValue
    private String iso8601;

    @JsonCreator
    public LukketPeriode(@Valid @NotNull String iso8601) {
        verifiserKanVæreGyldigPeriode(iso8601);
        String[] split = iso8601.split(SKILLE);
        this.fraOgMed = parseLocalDate(split[0]);
        this.tilOgMed = parseLocalDate(split[1]);
        this.iso8601 = iso8601;
    }

    public LukketPeriode(@Valid @NotNull LocalDate fraOgMed, @Valid @NotNull LocalDate tilOgMed) {
        this.fraOgMed = fraOgMed;
        this.tilOgMed = tilOgMed;
        this.iso8601 = toIso8601(fraOgMed) + SKILLE + toIso8601(tilOgMed);
    }

    @AssertFalse(message = "Fra og med (FOM) må være før eller lik til og med (TOM).")
    private boolean isValidPeriode() {
        return fomErFørTom();
    }

    private boolean fomErFørTom() {
        if (tilOgMed == null || fraOgMed == null) {
            return false;
        }
        return tilOgMed.isBefore(fraOgMed);
    }

    public LocalDate getFraOgMed() {
        return fraOgMed;
    }

    public LocalDate getTilOgMed() {
        return tilOgMed;
    }

    public void setFraOgMed(LocalDate fraOgMed) {
        this.fraOgMed = fraOgMed;
    }

    public void setTilOgMed(LocalDate tilOgMed) {
        this.tilOgMed = tilOgMed;
    }

    public String getIso8601() {
        return iso8601;
    }

    public void setIso8601(String iso8601) {
        this.iso8601 = iso8601;
    }

    public static LukketPeriode parse(String iso8601) {
        return new LukketPeriode(iso8601);
    }



    @Override
    public int compareTo(LukketPeriode o) {
        return this.iso8601.compareTo(o.iso8601);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        var periode = (LukketPeriode) o;
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
            return LukketPeriode.ÅPEN;
        else
            return dato.toString();
    }



}
