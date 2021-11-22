package no.nav.k9.innsyn.søknadsammeslåer;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

public class Tilsynsammenslåer {

    public static Tilsynsordning slåsammen(PleiepengerSyktBarn s1Ytelse, PleiepengerSyktBarn s2Ytelse) {
        final Tilsynsordning tilsyn = new Tilsynsordning();
        final LocalDateTimeline<TilsynPeriodeInfo> t1 = lagTidslinje(s1Ytelse.getTilsynsordning());
        final LocalDateTimeline<TilsynPeriodeInfo> t2 = lagTidslinje(s2Ytelse.getTilsynsordning());
        tilsyn.medPerioder(SøknadsammenslåerUtils.slåSammenOgHåndterTrukkedeKrav(s2Ytelse, t1, t2));
        
        return tilsyn;
    }

    @SuppressWarnings("unchecked")
    private static LocalDateTimeline<TilsynPeriodeInfo> lagTidslinje(Tilsynsordning tilsynsordning) {
        if (tilsynsordning == null) {
            return LocalDateTimeline.EMPTY_TIMELINE;
        }
        return SøknadsammenslåerUtils.lagTidslinje(tilsynsordning.getPerioder());
    }
}
