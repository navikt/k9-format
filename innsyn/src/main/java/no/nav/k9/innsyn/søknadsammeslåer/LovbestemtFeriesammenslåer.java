package no.nav.k9.innsyn.søknadsammeslåer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import no.nav.k9.søknad.TidsserieUtils;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.LovbestemtFerie;
import no.nav.k9.søknad.ytelse.psb.v1.LovbestemtFerie.LovbestemtFeriePeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;

public class LovbestemtFeriesammenslåer {

    public static LovbestemtFerie slåsammen(PleiepengerSyktBarn s1Ytelse, PleiepengerSyktBarn s2Ytelse) {
        var s1Tidslinje = SøknadsammenslåerUtils.lagTidslinje(s1Ytelse.getLovbestemtFerie().getPerioder());
        
        /*
         * Dette speiler workaround i k9-sak der vi nuller ut lovbestemt ferie når
         * denne inngår i en søknadsperiode (fremfor kun endringsperiode).
         * 
         * Workarounden i k9-sak er der fordi brukerdialog ikke selv nuller ut
         * ferie ved innsending.
         */
        final var s2Søknadsperioder = TidsserieUtils.toLocalDateTimeline(s2Ytelse.getSøknadsperiodeList());
        s1Tidslinje = s1Tidslinje.disjoint(s2Søknadsperioder);
        
        final var s2Tidslinje = SøknadsammenslåerUtils.lagTidslinje(s2Ytelse.getLovbestemtFerie().getPerioder());
        
        final Map<Periode, LovbestemtFeriePeriodeInfo> data = SøknadsammenslåerUtils.slåSammenOgHåndterTrukkedeKrav(s2Ytelse, s1Tidslinje, s2Tidslinje);
        return new LovbestemtFerie().medPerioder(medSkalHaFerieEksplisittSatt(data));
    }

    /**
     * Støtte for å tolke manglende "skalHaFerie"-felt som true. Data fra søknadsdialogen blir
     * dessverre fortsatt sendt inn slik (og vi må uansett støtte historisk format etter at dette
     * har blitt endret).
     */
    private static Map<Periode, LovbestemtFeriePeriodeInfo> medSkalHaFerieEksplisittSatt(Map<Periode, LovbestemtFeriePeriodeInfo> data) {
        final Map<Periode, LovbestemtFeriePeriodeInfo> resultatData = new HashMap<>();
        for (Entry<Periode, LovbestemtFeriePeriodeInfo> entry : data.entrySet()) {
            resultatData.put(entry.getKey(), new LovbestemtFeriePeriodeInfo().medSkalHaFerie(entry.getValue() == null || entry.getValue().isSkalHaFerie()));
        }
        return resultatData;
    }

}
