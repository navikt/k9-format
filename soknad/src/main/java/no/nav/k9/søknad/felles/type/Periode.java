package no.nav.k9.søknad.felles.type;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.validation.constraints.AssertFalse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Periode implements Comparable<Periode> {
    static final String ÅPEN = "..";
    static final String SKILLE = "/";

    private LocalDate fraOgMed;

    private LocalDate tilOgMed;

    @JsonValue
    private String iso8601;

    @JsonCreator
    public Periode(String iso8601) {
        verifiserKanVæreGyldigPeriode(iso8601);
        String[] split = iso8601.split(SKILLE);
        this.fraOgMed = parseLocalDate(split[0]);
        this.tilOgMed = parseLocalDate(split[1]);
        this.iso8601 = iso8601;
    }

    public Periode(Periode p) {
        this(p.getFraOgMed(), p.getTilOgMed());
    }

    public Periode(LocalDate fraOgMed, LocalDate tilOgMed) {
        this.fraOgMed = fraOgMed;
        this.tilOgMed = tilOgMed;
        this.iso8601 = toIso8601(fraOgMed) + SKILLE + toIso8601(tilOgMed);
    }

    @AssertFalse(message = "[ugyldigPeriode] Fra og med (FOM) må være før eller lik til og med (TOM).")
    public boolean isTilOgMedFørFraOgMed() {
        return fraOgMed != null && tilOgMed != null && tilOgMed.isBefore(fraOgMed);
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

    public static Periode parse(String iso8601) {
        return new Periode(iso8601);
    }

    public static Periode forsikreLukketPeriode(Periode periode, LocalDate fallbackTilOgMed) {
        Objects.requireNonNull(periode);
        Objects.requireNonNull(periode.fraOgMed);
        Objects.requireNonNull(fallbackTilOgMed);
        return new Periode(periode.fraOgMed, periode.tilOgMed != null ? periode.tilOgMed : fallbackTilOgMed);
    }

    @Override
    public int compareTo(Periode o) {
        return this.iso8601.compareTo(o.iso8601);
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

    /**
     * Sjekk om denne perioden inneholder (omslutter) angitt periode.
     */
    public boolean inneholder(Periode periode) {
        return (this.fraOgMed == null || (periode.fraOgMed != null && !this.fraOgMed.isAfter(periode.fraOgMed)))
                && (this.tilOgMed == null || (periode.tilOgMed != null && !this.tilOgMed.isBefore(periode.tilOgMed)));
    }

    /**
     * Sjekk om denne perioden og annenPeriode har overlappende tidsintervaller.
     */
    public boolean overlapper(Periode annenPeriode) {
        boolean starterFørEllerSamtidigSomAnnenPeriodeSlutter = this.fraOgMed == null || this.fraOgMed != null && annenPeriode.getTilOgMed() == null || this.fraOgMed != null && annenPeriode.getTilOgMed() != null && (this.fraOgMed.isEqual(annenPeriode.getTilOgMed()) || this.fraOgMed.isBefore(annenPeriode.getTilOgMed()));
        if (!starterFørEllerSamtidigSomAnnenPeriodeSlutter) {
            return false;
        } else {
            boolean slutterEtterEllerSamtidigSomPeriodeStarter = this.tilOgMed == null || this.tilOgMed != null && annenPeriode.getFraOgMed() == null || this.tilOgMed != null && annenPeriode.getFraOgMed() != null && (this.tilOgMed.isEqual(annenPeriode.getFraOgMed()) || this.tilOgMed.isAfter(annenPeriode.getFraOgMed()));
            return slutterEtterEllerSamtidigSomPeriodeStarter;
        }
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

    public static final class Utils {
        private Utils() {
        }

        private static final Comparator<Periode> tilOgMedComparator = Comparator.comparing(periode -> periode.tilOgMed);

        public static <PERIODE_INFO> void leggTilPeriode(
                Map<Periode, PERIODE_INFO> perioder,
                Periode nyPeriode,
                PERIODE_INFO nyPeriodeInfo) {
            Objects.requireNonNull(perioder, "perioder");
            Objects.requireNonNull(nyPeriode, "nyPeriode");
            Objects.requireNonNull(nyPeriodeInfo, "nyPeriodeInfo");

            if (perioder.containsKey(nyPeriode)) {
                throw new IllegalArgumentException("Inneholder allerede " + nyPeriode.iso8601);
            }

            perioder.put(nyPeriode, nyPeriodeInfo);
        }

        public static <PERIODE_INFO> void leggTilPerioder(
                Map<Periode, PERIODE_INFO> perioder,
                Map<Periode, PERIODE_INFO> nyePerioder) {
            Objects.requireNonNull(perioder);
            Objects.requireNonNull(nyePerioder);
            var nyeKeys = nyePerioder.keySet();

            var duplikater = perioder
                    .keySet()
                    .stream()
                    .filter(nyeKeys::contains)
                    .collect(Collectors.toSet());

            if (!duplikater.isEmpty()) {
                var duplikatePerioder = String.join(", ", duplikater
                        .stream()
                        .map(it -> it.iso8601)
                        .collect(Collectors.toSet()));
                throw new IllegalArgumentException("Inneholder allerede " + duplikatePerioder);
            }

            perioder.putAll(nyePerioder);
        }

        public static LocalDate sisteTilOgMedTillatÅpnePerioder(Map<Periode, ?> periodeMap) {
            return sisteTilOgMed(periodeMap);
        }

        public static LocalDate sisteTilOgMedBlantLukkedePerioder(Map<Periode, ?> periodeMap) {
            LocalDate sisteTilOgMed = sisteTilOgMed(periodeMap);
            if (sisteTilOgMed == null)
                throw new IllegalStateException("En eller fler av periodene er åpne (uten tilOgMed satt).");
            return sisteTilOgMed;
        }

        private static LocalDate sisteTilOgMed(Map<Periode, ?> periodeMap) {
            if (periodeMap == null || periodeMap.isEmpty()) {
                throw new IllegalStateException("Må være minst en periode for å finne siste tilOgMed");
            }
            if (periodeMap.keySet().stream().anyMatch(periode -> periode.tilOgMed == null)) {
                return null;
            }
            SortedSet<Periode> perioder = new TreeSet<>(tilOgMedComparator);
            perioder.addAll(periodeMap.keySet());
            return perioder.last().tilOgMed;
        }
    }

    private static String toIso8601(LocalDate dato) {
        if (dato == null)
            return Periode.ÅPEN;
        else
            return dato.toString();
    }

}
