package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.felles.Periode;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TilsynsordningUke {
    final Map<LocalDate, TilsynsordningOpphold> opphold;

    private TilsynsordningUke(
            Periode periode,
            Duration mandag,
            Duration tirsdag,
            Duration onsdag,
            Duration torsdag,
            Duration fredag) {
        Map<LocalDate, TilsynsordningOpphold> opphold = new HashMap<>();
        LocalDate dato = periode.fraOgMed;
        do {
            Duration duration;
            switch (dato.getDayOfWeek()) {
                case MONDAY:
                    duration = mandag;
                    break;
                case TUESDAY:
                    duration = tirsdag;
                    break;
                case WEDNESDAY:
                    duration = onsdag;
                    break;
                case THURSDAY:
                    duration = torsdag;
                    break;
                case FRIDAY:
                    duration = fredag;
                    break;
                default:
                    duration = null;
            }
            if (duration != null) {
                TilsynsordningOpphold tilsynsordningOpphold = TilsynsordningOpphold
                        .builder()
                        .lengde(duration)
                        .build();
                opphold.put(dato, tilsynsordningOpphold);
            }
            dato = dato.plusDays(1);
        } while (dato.isBefore(periode.tilOgMed) || dato.equals(periode.tilOgMed));

        this.opphold = Collections.unmodifiableMap(opphold);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Periode periode;
        private Duration mandag;
        private Duration tirsdag;
        private Duration onsdag;
        private Duration torsdag;
        private Duration fredag;

        private Builder() {}

        public Builder periode(Periode periode) {
            this.periode = periode;
            return this;
        }

        public Builder mandag(Duration mandag) {
            this.mandag = mandag;
            return this;
        }

        public Builder tirsdag(Duration tirsdag) {
            this.tirsdag = tirsdag;
            return this;
        }

        public Builder onsdag(Duration onsdag) {
            this.onsdag = onsdag;
            return this;
        }

        public Builder torsdag(Duration torsdag) {
            this.torsdag = torsdag;
            return this;
        }

        public Builder fredag(Duration fredag) {
            this.fredag = fredag;
            return this;
        }

        private static Duration håndter(Duration duration) {
            if (duration == null || duration.isZero()) return null;
            if (duration.isNegative()) return duration.abs();
            else return duration;
        }

        public TilsynsordningUke build() {
            if (periode == null) {
                throw new IllegalArgumentException("Periode må settes.");
            } else if (periode.fraOgMed == null || periode.tilOgMed == null) {
                throw new IllegalArgumentException("Perioden må inneholde fraOgMed og tilOgMed.");
            } else if (periode.tilOgMed.isBefore(periode.fraOgMed)) {
                throw new IllegalArgumentException("Perioden må ha fraOgMed før tilOgMed.");
            }
            return new TilsynsordningUke(
                    periode,
                    håndter(mandag),
                    håndter(tirsdag),
                    håndter(onsdag),
                    håndter(torsdag),
                    håndter(fredag)
            );
        }
    }
}
