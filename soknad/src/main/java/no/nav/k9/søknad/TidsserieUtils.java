package no.nav.k9.søknad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import no.nav.fpsak.tidsserie.LocalDateSegment;
import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;

public class TidsserieUtils {
    public static List<Periode> toPeriodeList(LocalDateTimeline<?> t) {
        return t.stream().map(l -> new Periode(l.getFom(), l.getTom())).collect(Collectors.toList());
    }

    public static LocalDateTimeline<Boolean> toLocalDateTimeline(Map<Periode, ?> periodeMap, String felt, List<Feil> feil) {
        return toLocalDateTimeline(new ArrayList<>(periodeMap.keySet()), felt, feil);
    }

    public static LocalDateTimeline<Boolean> toLocalDateTimeline(List<Periode> perioder, String felt, List<Feil> feil) {
        try {
            return toLocalDateTimeline(perioder);
        } catch (IllegalArgumentException e) {
            if (!feil.contains(new Feil(felt, "IllegalArgumentException", e.getMessage()))) {
                feil.add(new Feil(felt, "IllegalArgumentException", e.getMessage()));
            }
        }
        return new LocalDateTimeline<>(Collections.emptyList());
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
