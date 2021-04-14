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

    public static PerioderMedFeilWrapper finnIkkeKomplettePerioderOgPerioderUtenfor(LocalDateTimeline<Boolean> test, TidsseriePeriodeWrapper tidsseriePeriodeWrapper) {
        return new PerioderMedFeilWrapper(
                getPerioderSomIkkeOverlapperMedHovedperiode(test, tidsseriePeriodeWrapper),
                getPerioderUtenforGyldigperiode(test, tidsseriePeriodeWrapper));
    }

    private static List<Periode> getPerioderUtenforGyldigperiode(LocalDateTimeline<Boolean> test, TidsseriePeriodeWrapper tidsseriePeriodeWrapper) {
        return TidsserieUtils.toPeriodeList(test.disjoint(tidsseriePeriodeWrapper.gyldigInterval));
    }

    private static List<Periode> getPerioderSomIkkeOverlapperMedHovedperiode(LocalDateTimeline<Boolean> test, TidsseriePeriodeWrapper tidsseriePeriodeWrapper) {
        if (tidsseriePeriodeWrapper.getSøknadsperiode() == null || tidsseriePeriodeWrapper.getSøknadsperiode().isEmpty()) {
            return null;
        }
        return TidsserieUtils.toPeriodeList(tidsseriePeriodeWrapper.søknadsperiode.disjoint(test));
    }

    public static PerioderMedFeilWrapper finnPerioderUtenfor(LocalDateTimeline<Boolean> testTidsserie, TidsseriePeriodeWrapper hovedTidsserie) {
        if (hovedTidsserie == null) {
            return new PerioderMedFeilWrapper(new ArrayList<>(), new ArrayList<>());
        }
        return new PerioderMedFeilWrapper(
                null,
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
            return new LocalDateTimeline<Boolean>(perioder.stream().map(p -> new LocalDateSegment<Boolean>(p.getFraOgMed(), p.getTilOgMed(), true)).collect(Collectors.toList())).compress();
        }

        public static LocalDateTimeline<Boolean> toLocalDateTimeline(Map<Periode, ?> periodeMap) {
            return toLocalDateTimeline(new ArrayList<>(periodeMap.keySet()));
        }
    }

    public static class TidsseriePeriodeWrapper {
        private LocalDateTimeline<Boolean> søknadsperiode;
        private LocalDateTimeline<Boolean> gyldigInterval;

        public TidsseriePeriodeWrapper(List<Periode> søknadsperiode, List<Periode> endringsperiode) {
            if (søknadsperiode != null && !søknadsperiode.isEmpty()) {
                this.søknadsperiode = TidsserieUtils.toLocalDateTimeline(søknadsperiode);
                if (endringsperiode != null && !endringsperiode.isEmpty()) {
                    this.gyldigInterval = this.søknadsperiode.crossJoin(TidsserieUtils.toLocalDateTimeline(endringsperiode));
                } else {
                    this.gyldigInterval = this.søknadsperiode;
                }
            } else if (endringsperiode != null && !endringsperiode.isEmpty()) {
                gyldigInterval = TidsserieUtils.toLocalDateTimeline(endringsperiode);
            }
        }

        public LocalDateTimeline<Boolean> getSøknadsperiode() {
            return søknadsperiode;
        }

        public LocalDateTimeline<Boolean> getGyldigInterval() {
            return gyldigInterval;
        }
    }

    public static class PerioderMedFeilWrapper {
        private final List<Periode> perioderSomIkkeOverlapperMedHovedperiode;
        private final List<Periode> perioderUtenforGyldigperiode;

        public PerioderMedFeilWrapper(List<Periode> perioderSomIkkeOverlapperMedHovedperiode, List<Periode> perioderUtenforGyldigperiode) {
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
            if(perioderSomIkkeOverlapperMedHovedperiode != null && !this.perioderSomIkkeOverlapperMedHovedperiode.isEmpty()) {
                feil.addAll(this.perioderSomIkkeOverlapperMedHovedperiode.stream()
                        .filter(TidsserieValidator::periodeInneholderDagerSomIkkeErHelg)
                        .map(p -> toFeil(p, felt, "ikkeKomplettPeriode", "Periodene er ikke komplett, periode som mangler er: "))
                        .collect(Collectors.toList()));
            }

            if(perioderUtenforGyldigperiode != null && !this.perioderUtenforGyldigperiode.isEmpty()) {
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
