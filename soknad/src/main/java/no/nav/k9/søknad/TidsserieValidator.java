package no.nav.k9.søknad;

import no.nav.fpsak.tidsserie.LocalDateSegment;
import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TidsserieValidator {

    public static TidsseriePeriodeWrapper finnIkkeKomplettePerioderOgPerioderUtenfor(LocalDateTimeline<Boolean> testTidsserie, LocalDateTimeline<Boolean> hovedTidsserie) {
        return new TidsseriePeriodeWrapper(
                TidsserieUtils.toPeriodeList(hovedTidsserie.disjoint(testTidsserie)),
                TidsserieUtils.toPeriodeList(testTidsserie.disjoint(hovedTidsserie)));
    }

    public static TidsseriePeriodeWrapper finnPerioderUtenfor(LocalDateTimeline<Boolean> testTidsserie, LocalDateTimeline<Boolean> hovedTidsserie) {
        return new TidsseriePeriodeWrapper(
                new ArrayList<>(),
                TidsserieUtils.toPeriodeList(testTidsserie.disjoint(hovedTidsserie)));
    }

    public static boolean periodeInneholderDagerSomIkkeErHelg(Periode periode) {
        LocalDate testDag = periode.getFraOgMed();
        while (testDag.isBefore(periode.getTilOgMed()) || testDag.isEqual(periode.getTilOgMed())) {
            if (!((testDag.getDayOfWeek() == DayOfWeek.SUNDAY) || (testDag.getDayOfWeek() == DayOfWeek.SATURDAY))) {
                return true;
            }
            testDag = testDag.plusDays(1);
        }
        return false;
    }

    public static class TidsserieUtils {
        public static List<Periode> toPeriodeList(LocalDateTimeline<?> t) {
            return t.stream().map(l -> new Periode(l.getFom(), l.getTom())).collect(Collectors.toList());
        }

        public static LocalDateTimeline<Boolean> toLocalDateTimeline(List<Periode> perioder) throws IllegalArgumentException{
            return new LocalDateTimeline<Boolean>(perioder.stream().map(p -> new LocalDateSegment<Boolean>(p.getFraOgMed(), p.getTilOgMed(), true)).collect(Collectors.toList())).compress();
        }

        public static LocalDateTimeline<Boolean> toLocalDateTimeline(Map<Periode, ?> periodeMap) {
            return toLocalDateTimeline(new ArrayList<>(periodeMap.keySet()));
        }
    }

    public static class TidsseriePeriodeWrapper {
        private final List<Periode> perioderSomIkkeOverlapperMedHovedperiode;
        private final List<Periode> perioderUtenforHovedperiode;

        public TidsseriePeriodeWrapper(List<Periode> perioderSomIkkeOverlapperMedHovedperiode, List<Periode> perioderUtenforHovedperiode) {
            this.perioderSomIkkeOverlapperMedHovedperiode = perioderSomIkkeOverlapperMedHovedperiode;
            this.perioderUtenforHovedperiode = perioderUtenforHovedperiode;
        }

        public List<Periode> getPerioderSomIkkeOverlapperMedHovedperiode() {
            return perioderSomIkkeOverlapperMedHovedperiode;
        }

        public List<Periode> getPerioderUtenforHovedperiode() {
            return perioderUtenforHovedperiode;
        }

        public void valider(String felt , List<Feil> feil) {
            if(!this.perioderSomIkkeOverlapperMedHovedperiode.isEmpty()) {
                feil.addAll(this.perioderSomIkkeOverlapperMedHovedperiode.stream()
                        .filter(TidsserieValidator::periodeInneholderDagerSomIkkeErHelg)
                        .map(p -> toFeil(p, felt, "ikkeKomplettPeriode", "Periodene er ikke komplett, periode som mangler er: "))
                        .collect(Collectors.toList()));
            }
            if(!this.perioderUtenforHovedperiode.isEmpty()) {
                feil.addAll(this.perioderUtenforHovedperiode.stream()
                        .map(p -> toFeil(p, felt, "ugyldigPeriode", "Perioden er utenfor søknadsperioden : "))
                        .collect(Collectors.toList()));
            }
        }

        private Feil toFeil(Periode periode, String felt, String feilkode, String feilmelding) {
            return new Feil(felt, feilkode, feilmelding + periode.toString());
        }

    }
}
