package no.nav.k9.søknad.ytelse.olp.v1;

import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.fpsak.tidsserie.StandardCombinators;
import no.nav.k9.søknad.TidsserieUtils;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;

class PerioderMedEndringUtil {

    public static List<Periode> getEndringsperiode(Opplæringspenger søknad) {
        var allePerioderMedEndringTidsserie =
                tilTidsserie(getAllePerioderSomMåVæreInnenforSøknadsperiode(søknad));
        var søknadsperiode = toLocalDateTimeline(søknad.getSøknadsperiodeList());
        var endringsperiodeTidsserie = allePerioderMedEndringTidsserie.disjoint(søknadsperiode);
        return TidsserieUtils.tilPeriodeList(endringsperiodeTidsserie);
    }

    public static List<PerioderMedEndring> getAllePerioderSomMåVæreInnenforSøknadsperiode(Opplæringspenger olp) {
        var listen = new ArrayList<PerioderMedEndring>();
        listen.add(new PerioderMedEndring().medPerioder("lovbestemtFerie", olp.getLovbestemtFerie().getPerioder()));
        listen.add(new PerioderMedEndring().medPerioder("uttak", olp.getUttak().getPerioder()));
        listen.add(new PerioderMedEndring().medPerioder("utenlandsopphold", olp.getUtenlandsopphold().getPerioder()));
        listen.addAll(getArbeidstidPerioder(olp.getArbeidstid()));
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

    public static class PerioderMedEndring {
        private String felt;
        private Map<Periode, ?> periodeMap;

        public PerioderMedEndring() {

        }

        public String getFelt() {
            return felt;
        }

        public Map<Periode, ?> getPeriodeMap() {
            return periodeMap;
        }

        public List<Periode> getPeriodeList() {
            return new ArrayList<>(periodeMap.keySet());
        }

        PerioderMedEndring medPerioder(String felt, Map<Periode, ?> periodeMap) {
            this.felt = felt;
            this.periodeMap = periodeMap;
            return this;
        }

    }
}
