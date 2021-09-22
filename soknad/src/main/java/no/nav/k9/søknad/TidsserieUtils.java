package no.nav.k9.søknad;

import java.util.List;
import java.util.stream.Collectors;

import no.nav.fpsak.tidsserie.LocalDateSegment;
import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.k9.søknad.felles.type.Periode;

public class TidsserieUtils {
    public static List<Periode> tilPeriodeList(LocalDateTimeline<?> t) {
        return t.stream().map(l -> new Periode(l.getFom(), l.getTom())).collect(Collectors.toList());
    }

    public static LocalDateTimeline<Boolean> toLocalDateTimeline(List<Periode> perioder) {
        return new LocalDateTimeline<Boolean>(perioder
                .stream()
                .map(no.nav.k9.søknad.TidsserieUtils::mapLocalDateSegment)
                .collect(Collectors.toList()))
                .compress();
    }

    private static LocalDateSegment<Boolean> mapLocalDateSegment(Periode periode) {
        return new LocalDateSegment<Boolean>(
                periode.getFraOgMed(),
                periode.getTilOgMed(),
                true);
    }

}
