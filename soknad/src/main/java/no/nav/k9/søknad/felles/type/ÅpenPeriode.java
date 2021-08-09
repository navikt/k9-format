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

@Deprecated
public class ÅpenPeriode implements Comparable<ÅpenPeriode> {
    static final String ÅPEN = "..";
    static final String SKILLE = "/";

    private LocalDate fraOgMed;

    private LocalDate tilOgMed;

    @JsonValue
    private String iso8601;

    @JsonCreator
    public ÅpenPeriode(String iso8601) {
        verifiserKanVæreGyldigPeriode(iso8601);
        String[] split = iso8601.split(SKILLE);
        this.fraOgMed = parseLocalDate(split[0]);
        this.tilOgMed = parseLocalDate(split[1]);
        this.iso8601 = iso8601;
    }

    public ÅpenPeriode(LocalDate fraOgMed, LocalDate tilOgMed) {
        this.fraOgMed = fraOgMed;
        this.tilOgMed = tilOgMed;
        this.iso8601 = toIso8601(fraOgMed) + SKILLE + toIso8601(tilOgMed);
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

    public static ÅpenPeriode parse(String iso8601) {
        return new ÅpenPeriode(iso8601);
    }

    public static ÅpenPeriode forsikreLukketPeriode(ÅpenPeriode periode, LocalDate fallbackTilOgMed) {
        Objects.requireNonNull(periode);
        Objects.requireNonNull(periode.fraOgMed);
        Objects.requireNonNull(fallbackTilOgMed);
        return new ÅpenPeriode(periode.fraOgMed, periode.tilOgMed != null ? periode.tilOgMed : fallbackTilOgMed);
    }

    @AssertFalse(message = "Fra og med (FOM) må være før eller lik til og med (TOM).")
    private boolean isValid() {
        return tilOgMed.isBefore(fraOgMed);
    }

    @Override
    public int compareTo(ÅpenPeriode o) {
        return this.iso8601.compareTo(o.iso8601);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        var periode = (ÅpenPeriode) o;
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

    /** Sjekk om denne perioden inneholder (omslutter) angitt periode. */
    public boolean inneholder(ÅpenPeriode periode) {
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

    @Deprecated
    public static final class Utils {
        private Utils() {
        }

        private static final Comparator<ÅpenPeriode> tilOgMedComparator = Comparator.comparing(periode -> periode.tilOgMed);

        public static <PERIODE_INFO> void leggTilPeriode(
                                                         Map<ÅpenPeriode, PERIODE_INFO> perioder,
                                                         ÅpenPeriode nyPeriode,
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
                                                          Map<ÅpenPeriode, PERIODE_INFO> perioder,
                                                          Map<ÅpenPeriode, PERIODE_INFO> nyePerioder) {
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

        public static LocalDate sisteTilOgMedTillatÅpnePerioder(Map<ÅpenPeriode, ?> periodeMap) {
            return sisteTilOgMed(periodeMap);
        }

        public static LocalDate sisteTilOgMedBlantLukkedePerioder(Map<ÅpenPeriode, ?> periodeMap) {
            LocalDate sisteTilOgMed = sisteTilOgMed(periodeMap);
            if (sisteTilOgMed == null)
                throw new IllegalStateException("En eller fler av periodene er åpne (uten tilOgMed satt).");
            return sisteTilOgMed;
        }

        private static LocalDate sisteTilOgMed(Map<ÅpenPeriode, ?> periodeMap) {
            if (periodeMap == null || periodeMap.isEmpty()) {
                throw new IllegalStateException("Må være minst en periode for å finne siste tilOgMed");
            }
            if (periodeMap.keySet().stream().anyMatch(periode -> periode.tilOgMed == null)) {
                return null;
            }
            SortedSet<ÅpenPeriode> perioder = new TreeSet<>(tilOgMedComparator);
            perioder.addAll(periodeMap.keySet());
            return perioder.last().tilOgMed;
        }
    }

    private static String toIso8601(LocalDate dato) {
        if (dato == null)
            return ÅpenPeriode.ÅPEN;
        else
            return dato.toString();
    }

}
