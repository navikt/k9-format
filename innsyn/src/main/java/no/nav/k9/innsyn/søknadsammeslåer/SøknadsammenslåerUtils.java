package no.nav.k9.innsyn.søknadsammeslåer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import no.nav.fpsak.tidsserie.LocalDateSegment;
import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.fpsak.tidsserie.StandardCombinators;
import no.nav.k9.søknad.TidsserieUtils;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;

public final class SøknadsammenslåerUtils {

    private SøknadsammenslåerUtils() {
    }

    static <T> Map<Periode, T> slåSammenOgHåndterTrukkedeKrav(PleiepengerSyktBarn s2Ytelse, final LocalDateTimeline<T> t1, final LocalDateTimeline<T> t2) {
        final LocalDateTimeline<T> kombinertInformasjon = t1.union(t2, StandardCombinators::coalesceRightHandSide);
        final LocalDateTimeline<T> resultat = kombinertInformasjon.disjoint(trukkedeKravTidslinje(s2Ytelse)).compress();
        return resultat.stream()
                .map(p -> Map.entry(new Periode(p.getFom(), p.getTom()), p.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    static <T> LocalDateTimeline<T> lagTidslinje(Map<Periode, T> perioder) {
        if (perioder == null) {
            return LocalDateTimeline.empty();
        }
        final List<LocalDateSegment<T>> segments = perioder
                .entrySet()
                .stream()
                .map(e -> new LocalDateSegment<T>(e.getKey().getFraOgMed(), e.getKey().getTilOgMed(), e.getValue()))
                .collect(Collectors.toList());
        return new LocalDateTimeline<>(segments);
    }

    private static LocalDateTimeline<Boolean> trukkedeKravTidslinje(PleiepengerSyktBarn s1Ytelse) {
        return TidsserieUtils.toLocalDateTimeline(s1Ytelse.getTrekkKravPerioder());
    }

}
