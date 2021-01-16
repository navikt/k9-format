package no.nav.k9.søknad.ytelse.psb.v1.tilsyn;

import no.nav.k9.søknad.felles.type.Periode;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

/*
    En "legacy"-uke som er blitt brukt i papirsøknad og videreført i digital søknad (per 20.02.2020)
    Tar imot èn uke og sier at det er slik alle ukene med tilsyn kommer til å se ut innenfor en periode
    (normalt søknadsperioden).

    Denne klassen tar inn denne presentasjonen av tilsynsordning og gjør det om til x antall "opphold".

    Måten dette gjøres er å se på "kalenderuker" og avkorter alle dager til maks 7 timer og 30 minutter
    før den slår sammen tiden i tilsynsordning på en ukers perioder.

    Håndterer også om start & slutt-uke ikke er hele uker eller om ukene går over i nytt kalenderår (uke 0/53).
 */
public class TilsynsordningUke {
    public static final Duration MAX_LENGDE_PER_DAG = Duration.ofHours(7).plusMinutes(30);
    private static final WeekFields WEEK_FIELDS = WeekFields.of(Locale.forLanguageTag("no-NO"));

    final Map<Periode, TilsynsordningOpphold> opphold;

    private TilsynsordningUke(
                              Periode periode,
                              Duration mandag,
                              Duration tirsdag,
                              Duration onsdag,
                              Duration torsdag,
                              Duration fredag) {
        Map<String, UkeInfo> uker = new HashMap<>();
        LocalDate dato = periode.fraOgMed;
        do {
            String ukeId = ukeId(dato);
            UkeInfo ukeInfo = uker.getOrDefault(ukeId, UkeInfo.of(dato));

            switch (dato.getDayOfWeek()) {
                case MONDAY:
                    ukeInfo.plussDag(dato, mandag);
                    break;
                case TUESDAY:
                    ukeInfo.plussDag(dato, tirsdag);
                    break;
                case WEDNESDAY:
                    ukeInfo.plussDag(dato, onsdag);
                    break;
                case THURSDAY:
                    ukeInfo.plussDag(dato, torsdag);
                    break;
                case FRIDAY:
                    ukeInfo.plussDag(dato, fredag);
                    break;
                case SATURDAY:
                case SUNDAY:
                default:
                    break;
            }
            if (!ukeInfo.summertLengde.isZero()) {
                uker.put(ukeId, ukeInfo);
            }
            dato = dato.plusDays(1);

        } while (dato.isBefore(periode.tilOgMed) || dato.equals(periode.tilOgMed));

        Map<Periode, TilsynsordningOpphold> opphold = new HashMap<>();

        uker.forEach((ukeId, ukeInfo) -> opphold.put(
            Periode
                .builder()
                .fraOgMed(ukeInfo.førsteAktuelleDag)
                .tilOgMed(ukeInfo.sisteAktuelleDag)
                .build(),
            TilsynsordningOpphold
                .builder()
                .lengde(ukeInfo.summertLengde)
                .build()));

        this.opphold = Collections.unmodifiableMap(opphold);
    }

    private static String ukeId(LocalDate dato) {
        LocalDate førsteDag = dato.with(DayOfWeek.MONDAY);
        LocalDate sisteDag = førsteDag.plusDays(6);
        return String.format("%s-%s-Uke%s",
            førsteDag.getYear(),
            sisteDag.getYear(),
            dato.get(WEEK_FIELDS.weekOfWeekBasedYear()));
    }

    private static class UkeInfo {
        private final LocalDate førsteAktuelleDag;
        private LocalDate sisteAktuelleDag;
        private Duration summertLengde;

        private UkeInfo(LocalDate førsteAktuelleDag) {
            Objects.requireNonNull(førsteAktuelleDag);
            this.førsteAktuelleDag = førsteAktuelleDag;
            this.sisteAktuelleDag = førsteAktuelleDag;
            this.summertLengde = Duration.ZERO;
        }

        private static UkeInfo of(LocalDate førsteAktuelleDag) {
            return new UkeInfo(førsteAktuelleDag);
        }

        private void plussDag(LocalDate dato, Duration lengde) {
            Objects.requireNonNull(dato);
            sisteAktuelleDag = dato;
            if (lengde != null) {
                if (lengde.compareTo(MAX_LENGDE_PER_DAG) > 0) {
                    this.summertLengde = summertLengde.plus(MAX_LENGDE_PER_DAG);
                } else {
                    this.summertLengde = summertLengde.plus(lengde);
                }
            }
        }
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

        private Builder() {
        }

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
            if (duration == null || duration.isZero())
                return null;
            if (duration.isNegative())
                return duration.abs();
            else
                return duration;
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
                håndter(fredag));
        }
    }
}
