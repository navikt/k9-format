package no.nav.k9.søknad;

import java.util.List;
import java.util.stream.Collectors;

import no.nav.fpsak.tidsserie.LocalDateSegment;
import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.k9.søknad.felles.type.Periode;

public class TidsserieUtils {
    private TidsserieUtils() {
    }

    public static List<Periode> tilPeriodeList(LocalDateTimeline<?> t) {
        return t.stream()
                .map(l -> new Periode(l.getFom(), l.getTom()))
                .collect(Collectors.toList());
    }

    public static LocalDateTimeline<Boolean> toLocalDateTimeline(List<Periode> perioder) {
        return new LocalDateTimeline<>(perioder
                .stream()
                .map(periode -> new LocalDateSegment<>(periode.getFraOgMed(), periode.getTilOgMed(), true))
                .collect(Collectors.toList()))
                .compress();
    }

}
