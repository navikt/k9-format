package no.nav.k9.søknad.ytelse.psb.v1;

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

    private static final String YTELSE_FELT = "ytelse.";


    public static List<Periode> getEndringsperiode(PleiepengerSyktBarn psb) {
        var allePerioderMedEndringTidsserie =
                tilTidsserie(getAllePerioderSomMåVæreInnenforSøknadsperiode(psb));
        var søknadsperiode = toLocalDateTimeline(psb.getSøknadsperiodeList());
        var endringsperiodeTidsserie = allePerioderMedEndringTidsserie.disjoint(søknadsperiode);
        return TidsserieUtils.toPeriodeList(endringsperiodeTidsserie);
    }

    public static List<PerioderMedEndring> getAllePerioderSomMåVæreInnenforSøknadsperiode(PleiepengerSyktBarn psb) {
        var listen = new ArrayList<PerioderMedEndring>();
        listen.add(new PerioderMedEndring().medPerioder(YTELSE_FELT + "utenlandsopphold", psb.getUtenlandsopphold().getPerioder()));
        listen.add(new PerioderMedEndring().medPerioder(YTELSE_FELT + "beredskap", psb.getBeredskap().getPerioder()));
        listen.add(new PerioderMedEndring().medPerioder(YTELSE_FELT + "nattevåk", psb.getNattevåk().getPerioder()));
        listen.add(new PerioderMedEndring().medPerioder(YTELSE_FELT + "tilsynsordning", psb.getTilsynsordning().getPerioder()));
        listen.add(new PerioderMedEndring().medPerioder(YTELSE_FELT + "lovbestemtFerie", psb.getLovbestemtFerie().getPerioder()));
        listen.add(new PerioderMedEndring().medPerioder(YTELSE_FELT + "uttak", psb.getUttak().getPerioder()));
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
                listen.add(new PerioderMedEndring().medPerioder(YTELSE_FELT + "arbeidstid.arbeidstaker[" + i + "]", at.getArbeidstidInfo().getPerioder()));
                i++;
            }
        }
        if (arbeidstid.getFrilanserArbeidstidInfo().isPresent()) {
            listen.add(new PerioderMedEndring().medPerioder(YTELSE_FELT + "arbeidstid.frilanser",
                    arbeidstid.getFrilanserArbeidstidInfo().get().getPerioder()));
        }
        if (arbeidstid.getSelvstendigNæringsdrivendeArbeidstidInfo().isPresent()) {
            listen.add(new PerioderMedEndring().medPerioder(YTELSE_FELT + "arbeidstid.selvstendigNæringsdrivende",
                    arbeidstid.getSelvstendigNæringsdrivendeArbeidstidInfo().get().getPerioder()));
        }
        return listen;
    }

    public static LocalDateTimeline<Boolean> tilTidsserie(List<PerioderMedEndring> listen) {
        var temp = new LocalDateTimeline<Boolean>(Collections.emptyList());
        for (PerioderMedEndring yp : listen) {
            temp = temp.union(
                    toLocalDateTimeline(yp.getPeriodeList()),
                    StandardCombinators::coalesceLeftHandSide);
        }
        return temp;
    }

    public static class PerioderMedEndring {
        private String felt;
        private List<Periode> periodeList;

        public PerioderMedEndring() {

        }

        public String getFelt() {
            return felt;
        }

        public List<Periode> getPeriodeList() {
            return periodeList;
        }

        PerioderMedEndring medPerioder(String felt, Map<Periode, ?> periodeMap) {
            this.felt = felt;
            this.periodeList = new ArrayList<>(periodeMap.keySet());
            return this;
        }

        PerioderMedEndring medPerioder(String felt, List<Periode> periodeList) {
            this.felt = felt;
            this.periodeList = periodeList;
            return this;
        }
    }
}
