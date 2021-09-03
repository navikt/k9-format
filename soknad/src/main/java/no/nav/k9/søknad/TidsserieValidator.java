package no.nav.k9.søknad;

import static no.nav.k9.søknad.TidsserieUtils.toPeriodeList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;

public class TidsserieValidator {

    public static List<Feil> finnPerioderUtenforGyldigInterval(LocalDateTimeline<Boolean> testTidsserie, LocalDateTimeline<Boolean> gyldigInterval, String felt) {
        var feil = new ArrayList<Feil>();
        var perioderUtenfor = getPerioderUtenforGyldigperiode(testTidsserie, gyldigInterval);

        feil.addAll(leggTilFeil(perioderUtenfor, felt + ".perioder", "ugyldigPeriode", "Perioden er utenfor gyldig interval(" + gyldigInterval.toString() +") : "));
        return feil;
    }

    public static List<Feil> finnIkkeKomplettePerioder(LocalDateTimeline<Boolean> test, LocalDateTimeline<Boolean> intervalHvorTidserieMåVæreKomplett, String felt) {
        var feil = new ArrayList<Feil>();
        var ikkeKomplettePerioder = getPerioderSomIkkeErKomplett(test, intervalHvorTidserieMåVæreKomplett);

        feil.addAll(leggTilFeilIkkeTaMedHelg(ikkeKomplettePerioder, felt + ".perioder", "ikkeKomplettPeriode", "Periodene er ikke komplett, periode som mangler er: "));
        return feil;
    }

    private static List<Feil> leggTilFeil(List<Periode> perioderMedFeil, String felt, String feilkode, String feilmelding) {
        if (perioderMedFeil.isEmpty()) {
            return List.of();
        }

        return perioderMedFeil.stream()
                .map(p -> toFeil(p, felt, feilkode, feilmelding))
                .collect(Collectors.toList());
    }

    private static List<Feil> leggTilFeilIkkeTaMedHelg(List<Periode> perioderMedFeil, String felt, String feilkode,  String feilmelding) {
        if (perioderMedFeil.isEmpty()) {
            return List.of();
        }

        return perioderMedFeil.stream()
                .filter(TidsserieValidator::periodeInneholderDagerSomIkkeErHelg)
                .map(p -> toFeil(p, felt, feilkode, feilmelding))
                .collect(Collectors.toList());
    }

    private static List<Periode> getPerioderUtenforGyldigperiode(LocalDateTimeline<Boolean> test, LocalDateTimeline<Boolean> gyldigInterval) {
        return toPeriodeList(
                test.disjoint(gyldigInterval));
    }

    private static List<Periode> getPerioderSomIkkeErKomplett(LocalDateTimeline<Boolean> test, LocalDateTimeline<Boolean> intervalHvorTidserieMåVæreKomplett) {
        return toPeriodeList(intervalHvorTidserieMåVæreKomplett.disjoint(test));
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

    private static Feil toFeil(Periode periode, String felt, String feilkode, String feilmelding) {
        return new Feil(felt, feilkode, feilmelding + periode.toString());
    }
}
