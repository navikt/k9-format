package no.nav.k9.søknad.felles;

import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        this.fraOgMed = parseLocalDate(split[0]);
        this.tilOgMed = parseLocalDate(split[1]);
        this.iso8601 = iso8601;
    }

    public static Periode parse(String iso8601) {
        return new Periode(iso8601);
    }

    public static Periode forsikreLukketPeriode(Periode periode, LocalDate fallbackTilOgMed) {
        Objects.requireNonNull(periode);
        Objects.requireNonNull(periode.fraOgMed);
        Objects.requireNonNull(fallbackTilOgMed);
        return Periode
                .builder()
                .fraOgMed(periode.fraOgMed)
                .tilOgMed(periode.tilOgMed != null ? periode.tilOgMed : fallbackTilOgMed)
                .build();
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

    private static LocalDate parseLocalDate(String iso8601) {
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

    public static final class Utils {
        private Utils() {}

        private static final Comparator<Periode> tilOgMedComparator = Comparator.comparing(periode -> periode.tilOgMed);

        public static <PERIODE_INFO> void leggTilPeriode(
                Map<Periode, PERIODE_INFO> perioder,
                Periode nyPeriode,
                PERIODE_INFO nyPeriodeInfo) {
            Objects.requireNonNull(perioder);
            Objects.requireNonNull(nyPeriode);
            Objects.requireNonNull(nyPeriodeInfo);

            if (perioder.containsKey(nyPeriode)) {
                throw new IllegalArgumentException("Inneholder allerede " + nyPeriode.iso8601);
            }

            perioder.put(nyPeriode,  nyPeriodeInfo);
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
                        .map(it-> it.iso8601)
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
            if (sisteTilOgMed == null) throw new IllegalStateException("En eller fler av periodene er åpne (uten tilOgMed satt).");
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
}
