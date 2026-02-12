package no.nav.k9.søknad.ytelse.pls.v1;

import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;

import java.util.ArrayList;
import java.util.List;

import no.nav.k9.søknad.TidsserieUtils;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.LovbestemtFerie;
import no.nav.k9.søknad.ytelse.psb.v1.PerioderMedEndring;
import no.nav.k9.søknad.ytelse.psb.v1.PerioderMedEndringUtil;

public class PleiepengerLivetsSluttfasePerioderMedEndringUtil {

    public static List<Periode> getEndringsperiode(PleipengerLivetsSluttfase ppn) {
        var allePerioderMedEndringTidsserie =
                PerioderMedEndringUtil.tilTidsserie(getAllePerioderSomMåVæreInnenforSøknadsperiode(ppn));
        var søknadsperiode = toLocalDateTimeline(ppn.getSøknadsperiodeList());
        var endringsperiodeTidsserie = allePerioderMedEndringTidsserie.disjoint(søknadsperiode);
        return TidsserieUtils.tilPeriodeList(endringsperiodeTidsserie);
    }

    public static List<PerioderMedEndring> getAllePerioderSomMåVæreInnenforSøknadsperiode(PleipengerLivetsSluttfase ppn) {
        var listen = new ArrayList<PerioderMedEndring>();
        listen.add(new PerioderMedEndring().medPerioder("uttak", ppn.getUttak().getPerioder()));
        listen.add(new PerioderMedEndring().medPerioder("utenlandsopphold", ppn.getUtenlandsopphold().getPerioder()));
        LovbestemtFerie lovbestemtFerie = ppn.getLovbestemtFerie();
        if (lovbestemtFerie != null && lovbestemtFerie.getPerioder() != null) {
            listen.add(new PerioderMedEndring().medPerioder("lovbestemtFerie", lovbestemtFerie.getPerioder()));
        }

        listen.addAll(PerioderMedEndringUtil.getArbeidstidPerioder(ppn.getArbeidstid()));
        return listen;
    }

}
