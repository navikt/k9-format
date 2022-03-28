package no.nav.k9.søknad.ytelse.pls.v1;

import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.fpsak.tidsserie.StandardCombinators;
import no.nav.k9.søknad.TidsserieUtils;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.PerioderMedEndring;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;

public class PerioderMedEndringUtil {

    public static List<Periode> getEndringsperiode(PleipengerLivetsSluttfase ppn) {
        var allePerioderMedEndringTidsserie =
                tilTidsserie(getAllePerioderSomMåVæreInnenforSøknadsperiode(ppn));
        var søknadsperiode = toLocalDateTimeline(ppn.getSøknadsperiodeList());
        var endringsperiodeTidsserie = allePerioderMedEndringTidsserie.disjoint(søknadsperiode);
        return TidsserieUtils.tilPeriodeList(endringsperiodeTidsserie);
    }

    public static List<PerioderMedEndring> getAllePerioderSomMåVæreInnenforSøknadsperiode(PleipengerLivetsSluttfase psb) {
        var listen = new ArrayList<PerioderMedEndring>();
        listen.add(new PerioderMedEndring().medPerioder("uttak", psb.getUttak().getPerioder()));
        listen.addAll(getArbeidstidPerioder(psb.getArbeidstid()));
        return listen;
    }

    public static List<PerioderMedEndring> getArbeidstidPerioder(Arbeidstid arbeidstid) {
        var listen = new ArrayList<PerioderMedEndring>();
        if (arbeidstid == null) {
            return listen;
        }
        if (arbeidstid.getArbeidstakerList() != null && !arbeidstid.getArbeidstakerList().isEmpty()) {
            int i = 0;
            for (var at : arbeidstid.getArbeidstakerList()) {
                listen.add(new PerioderMedEndring().medPerioder("arbeidstid.arbeidstakerList[" + i + "]", at.getArbeidstidInfo().getPerioder()));
                i++;
            }
        }
        if (arbeidstid.getFrilanserArbeidstidInfo().isPresent()) {
            listen.add(new PerioderMedEndring().medPerioder("arbeidstid.frilanser",
                    arbeidstid.getFrilanserArbeidstidInfo().get().getPerioder()));
        }
        if (arbeidstid.getSelvstendigNæringsdrivendeArbeidstidInfo().isPresent()) {
            listen.add(new PerioderMedEndring().medPerioder("arbeidstid.selvstendigNæringsdrivende",
                    arbeidstid.getSelvstendigNæringsdrivendeArbeidstidInfo().get().getPerioder()));
        }
        return listen;
    }

    private static LocalDateTimeline<Boolean> tilTidsserie(List<PerioderMedEndring> listen) {
        var temp = new LocalDateTimeline<Boolean>(Collections.emptyList());
        for (PerioderMedEndring yp : listen) {
            for (Periode p : yp.getPeriodeMap().keySet()) {
                temp = temp.union(
                        new LocalDateTimeline<>(p.getFraOgMed(), p.getTilOgMed(), Boolean.TRUE),
                        StandardCombinators::coalesceLeftHandSide);
            }
        }
        return temp;
    }

}
