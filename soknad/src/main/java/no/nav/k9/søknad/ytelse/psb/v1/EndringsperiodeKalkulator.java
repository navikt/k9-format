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

public class EndringsperiodeKalkulator {


    public static List<Periode> getEndringsperiode(PleiepengerSyktBarn psb) {
        var alleYtelsePerioderTidsserie =
                ytelsePerioderTilTidsserie(getYtelsePerioder(psb));
        var søknadsperiode = toLocalDateTimeline(psb.getSøknadsperiodeList());
        var endringsperiodeTidsserie = alleYtelsePerioderTidsserie.disjoint(søknadsperiode);
        return TidsserieUtils.toPeriodeList(endringsperiodeTidsserie);
    }

    public static List<YtelsePerioder> getYtelsePerioder(PleiepengerSyktBarn psb) {
        var listen = new ArrayList<YtelsePerioder>();
        listen.add(new YtelsePerioder().medPerioder("utenlandsopphold", psb.getUtenlandsopphold().getPerioder()));
        listen.add(new YtelsePerioder().medPerioder("bosteder", psb.getBosteder().getPerioder()));
        listen.add(new YtelsePerioder().medPerioder("beredskap", psb.getBeredskap().getPerioder()));
        listen.add(new YtelsePerioder().medPerioder("nattevåk", psb.getNattevåk().getPerioder()));
        listen.add(new YtelsePerioder().medPerioder("tilsynsordning", psb.getTilsynsordning().getPerioder()));
        listen.add(new YtelsePerioder().medPerioder("lovbestemtFerie", psb.getLovbestemtFerie().getPerioder()));
        listen.add(new YtelsePerioder().medPerioder("uttak", psb.getUttak().getPerioder()));
        listen.addAll(getArbeidstidPerioder(psb.getArbeidstid()));
        return listen;
    }

    public static List<YtelsePerioder> getArbeidstidPerioder(Arbeidstid arbeidstid) {
        var listen = new ArrayList<YtelsePerioder>();
        if (arbeidstid == null) {
            return listen;
        }
        if (arbeidstid.getArbeidstakerList() != null && !arbeidstid.getArbeidstakerList().isEmpty()) {
            int i = 0;
            for (var at: arbeidstid.getArbeidstakerList()) {
                listen.add(new YtelsePerioder().medPerioder("arbeidstid.arbeidstaker[" + i + "]" , at.getArbeidstidInfo().getPerioder()));
                i++;
            }
        }
        if (arbeidstid.getFrilanserArbeidstidInfo().isPresent()) {
            listen.add(new YtelsePerioder().medPerioder("arbeidstid.frilanser",
                    arbeidstid.getFrilanserArbeidstidInfo().get().getPerioder()));
        }
        if (arbeidstid.getSelvstendigNæringsdrivendeArbeidstidInfo().isPresent()) {
            listen.add(new YtelsePerioder().medPerioder("arbeidstid.selvstendigNæringsdrivende",
                    arbeidstid.getSelvstendigNæringsdrivendeArbeidstidInfo().get().getPerioder()));
        }
        return listen;
    }

    public static LocalDateTimeline<Boolean> ytelsePerioderTilTidsserie(List<YtelsePerioder> listen) {
        var temp = new LocalDateTimeline<Boolean>(Collections.emptyList());
        for (YtelsePerioder yp: listen) {
            temp = temp.union(
                    toLocalDateTimeline(yp.getPeriodeList()),
                    StandardCombinators::coalesceLeftHandSide);
        }
        return temp;
    }

    public static class YtelsePerioder {
        private String felt;
        private List<Periode> periodeList;

        public YtelsePerioder() {

        }

        public String getFelt() {
            return felt;
        }

        public List<Periode> getPeriodeList() {
            return periodeList;
        }

        YtelsePerioder medPerioder(String felt, List<Periode> periodeList) {
            this.felt = felt;
            this.periodeList = periodeList;
            return this;
        }

        YtelsePerioder medPerioder(String felt, Map<Periode, ?> periodeMap) {
            this.felt = felt;
            this.periodeList = new ArrayList<>(periodeMap.keySet());
            return this;
        }
    }
}
