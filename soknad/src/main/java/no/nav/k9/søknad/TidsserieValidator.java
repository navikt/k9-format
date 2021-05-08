package no.nav.k9.søknad;

import no.nav.fpsak.tidsserie.LocalDateSegment;
import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.fpsak.tidsserie.StandardCombinators;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TidsserieValidator {

    public static PerioderMedFeil finnIkkeKomplettePerioderOgPerioderUtenfor(LocalDateTimeline<Boolean> test, Perioder perioder) {
        return new PerioderMedFeil(
                getPerioderSomIkkeOverlapperMedHovedperiode(test, perioder),
                getPerioderUtenforGyldigperiode(test, perioder));
    }

    private static List<Periode> getPerioderUtenforGyldigperiode(LocalDateTimeline<Boolean> test, Perioder perioder) {
        return TidsserieUtils.toPeriodeList(test.disjoint(perioder.gyldigInterval));
    }

    private static List<Periode> getPerioderSomIkkeOverlapperMedHovedperiode(LocalDateTimeline<Boolean> test, Perioder perioder) {

        return TidsserieUtils.toPeriodeList(perioder.søknadsperiode.disjoint(test));
    }

    public static PerioderMedFeil finnPerioderUtenfor(LocalDateTimeline<Boolean> testTidsserie, Perioder hovedTidsserie) {
        return new PerioderMedFeil(
                Collections.emptyList(),
                getPerioderUtenforGyldigperiode(testTidsserie, hovedTidsserie));
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
            return new LocalDateTimeline<Boolean>(perioder
                    .stream()
                    .map(p -> new LocalDateSegment<Boolean>(
                            Objects.requireNonNull(p.getFraOgMed()),
                            Objects.requireNonNull(p.getTilOgMed()),
                            true))
                    .collect(Collectors.toList())).compress();
        }

        public static LocalDateTimeline<Boolean> toLocalDateTimeline(Map<Periode, ?> periodeMap) {
            return toLocalDateTimeline(new ArrayList<>(periodeMap.keySet()));
        }
    }

    public static class Perioder {
        private final LocalDateTimeline<Boolean> søknadsperiode;
        private final LocalDateTimeline<Boolean> gyldigInterval;

        public Perioder(List<Periode> søknadsperiode, List<Periode> endringsperiode) {
            this.søknadsperiode = TidsserieUtils.toLocalDateTimeline(søknadsperiode);
            this.gyldigInterval = this.søknadsperiode.union(TidsserieUtils.toLocalDateTimeline(endringsperiode), StandardCombinators::coalesceLeftHandSide);
        }

        public LocalDateTimeline<Boolean> getSøknadsperiode() {
            return søknadsperiode;
        }

        public LocalDateTimeline<Boolean> getPerioderDerEndringerErTillatt() {
            return gyldigInterval;
        }
    }

    public static class PerioderMedFeil {
        private final List<Periode> perioderSomIkkeOverlapperMedHovedperiode;
        private final List<Periode> perioderUtenforGyldigperiode;

        public PerioderMedFeil(List<Periode> perioderSomIkkeOverlapperMedHovedperiode, List<Periode> perioderUtenforGyldigperiode) {
            this.perioderSomIkkeOverlapperMedHovedperiode = perioderSomIkkeOverlapperMedHovedperiode;
            this.perioderUtenforGyldigperiode = perioderUtenforGyldigperiode;
        }

        public List<Periode> getPerioderSomIkkeOverlapperMedHovedperiode() {
            return perioderSomIkkeOverlapperMedHovedperiode;
        }

        public List<Periode> getPerioderUtenforGyldigperiode() {
            return perioderUtenforGyldigperiode;
        }

        public void valider(String felt , List<Feil> feil) {
            if (!this.perioderSomIkkeOverlapperMedHovedperiode.isEmpty()) {
                feil.addAll(this.perioderSomIkkeOverlapperMedHovedperiode.stream()
                        .filter(TidsserieValidator::periodeInneholderDagerSomIkkeErHelg)
                        .map(p -> toFeil(p, felt, "ikkeKomplettPeriode", "Periodene er ikke komplett, periode som mangler er: "))
                        .collect(Collectors.toList()));
            }

            if (!this.perioderUtenforGyldigperiode.isEmpty()) {
                feil.addAll(this.perioderUtenforGyldigperiode.stream()
                        .map(p -> toFeil(p, felt, "ugyldigPeriode", "Perioden er utenfor søknadsperioden : "))
                        .collect(Collectors.toList()));
            }
        }

        private Feil toFeil(Periode periode, String felt, String feilkode, String feilmelding) {
            return new Feil(felt, feilkode, feilmelding + periode.toString());
        }

    }
}
