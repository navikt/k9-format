package no.nav.k9.søknad.ytelse.pls.v1;

import static no.nav.k9.søknad.TidsserieUtils.tilPeriodeList;
import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import no.nav.fpsak.tidsserie.LocalDateSegment;
import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.opptjening.AnnenAktivitet;
import no.nav.k9.søknad.felles.opptjening.Frilanser;
import no.nav.k9.søknad.felles.opptjening.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;

public class PleiepengerLivetsSluttfaseYtelseValidator extends YtelseValidator {
    @Override
    public List<Feil> valider(Ytelse søknad) {
        return valider((PleipengerLivetsSluttfase) søknad);
    }

    public List<Feil> valider(PleipengerLivetsSluttfase søknad) {
        List<Feil> feilene = new ArrayList<>();
        try {
            if (søknad.getSøknadsperiode() == null) {
                feilene.add(lagFeil("søknadsperiode", "påkrevd", "det finnes ingen søknadsperiode"));
            }
            feilene.addAll(validerPerioderErLukketOgGyldig(søknad.getBosteder().getPerioder(), "bosteder.perioder"));
            feilene.addAll(validerPerioderErLukketOgGyldig(søknad.getBosteder().getPerioderSomSkalSlettes(), "bosteder.perioderSomSkalSlettes"));
            feilene.addAll(validerPerioderErLukketOgGyldig(søknad.getUtenlandsopphold().getPerioder(), "utenlandsopphold.perioder"));
            feilene.addAll(validerPerioderErLukketOgGyldig(søknad.getUtenlandsopphold().getPerioderSomSkalSlettes(), "utenlandsopphold.perioderSomSkalSlettes"));
            validerArbeidstid(søknad);
            validerOpptjening(søknad, feilene);
            validerTrekkKravPerioder(søknad, feilene);
            //TODO valider søknadsperiode(overlapp), uttak
        } catch (ValideringsAvbrytendeFeilException valideringsAvbrytendeFeilException) {
            feilene.addAll(valideringsAvbrytendeFeilException.getFeilList());
        }
        return feilene;
    }

    private void validerArbeidstid(PleipengerLivetsSluttfase søknad) throws ValideringsAvbrytendeFeilException {
        List<Arbeidstaker> arbeidstakerList = søknad.getArbeidstid().getArbeidstakerList();
        for (int i = 0; i < arbeidstakerList.size(); i++) {
            lagTidslinjeOgValider(arbeidstakerList.get(i).getArbeidstidInfo().getPerioder(), "arbeidstid.arbeidstakerList[" + i + "].perioder");
        }
        søknad.getArbeidstid().getSelvstendigNæringsdrivendeArbeidstidInfo().ifPresent(sn -> lagTidslinjeOgValider(sn.getPerioder(), "arbeidstid.selvstendigNæringsdrivendeArbeidstidInfo"));
        søknad.getArbeidstid().getFrilanserArbeidstidInfo().ifPresent(fl -> lagTidslinjeOgValider(fl.getPerioder(), "arbeidstid.frilanserArbeidstidInfo"));
    }

    private void validerOpptjening(PleipengerLivetsSluttfase søknad, List<Feil> feilene) {
        List<SelvstendigNæringsdrivende> snAktiviteter = søknad.getOpptjeningAktivitet().getSelvstendigNæringsdrivende();
        for (int i = 0; i < snAktiviteter.size(); i++) {
            lagTidslinjeMedStøtteForÅpenPeriodeOgValider(snAktiviteter.get(i).getPerioder(), "opptjeningAktivitet.selvstendigNæringsdrivende[" + i + "].perioder");
        }

        List<AnnenAktivitet> andreAktiviteter = søknad.getOpptjeningAktivitet().getAndreAktiviteter();
        for (int i = 0; i < andreAktiviteter.size(); i++) {
            validerPerioderIkkeErInvertert(andreAktiviteter.get(i).getPeriode(), "opptjeningAktivitet.andreAktiviteter[" + i + "].perioder", feilene);
        }
        Frilanser fl = søknad.getOpptjeningAktivitet().getFrilanser();
        if (fl != null) {
            validerPerioderIkkeErInvertert(new Periode(fl.getStartdato(), fl.getSluttdato()), "opptjeningAktivitet.frilanser.startdato/sluttdato", feilene);
        }

    }

    private void validerTrekkKravPerioder(PleipengerLivetsSluttfase søknad, List<Feil> feilene) {
        LocalDateTimeline<Boolean> trekkKravPerioderTidslinje = lagTidslinjeOgValider(søknad.getTrekkKravPerioder(), "trekkKravPerioder.perioder", feilene);
        var søktePerioder = søknad.getArbeidstid().getArbeidstakerList().stream()
                .flatMap(it -> it.getArbeidstidInfo().getPerioder().keySet().stream())
                .map(it -> new LocalDateSegment<>(it.getFraOgMed(), it.getTilOgMed(), Boolean.TRUE))
                .collect(Collectors.toList());
        var tidslinjeSøktPeriode = LocalDateTimeline.buildGroupOverlappingSegments(søktePerioder).mapValue(s -> Boolean.TRUE);
        feilene.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, tidslinjeSøktPeriode, "trekkKravPerioder"));
    }

    private LocalDateTimeline<Boolean> lagTidslinjeMedStøtteForÅpenPeriodeOgValider(Map<Periode, ?> periodeMap, String felt) throws ValideringsAvbrytendeFeilException {
        return lagTidslinjeOgValider(periodeMap, felt, true);
    }

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(Map<Periode, ?> periodeMap, String felt) throws ValideringsAvbrytendeFeilException {
        return lagTidslinjeOgValider(periodeMap, felt, false);
    }

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(Map<Periode, ?> periodeMap, String felt, boolean godtaÅpenPeriode) throws ValideringsAvbrytendeFeilException {
        var nyFeil = godtaÅpenPeriode
                ? validerPerioderGyldig(periodeMap, felt)
                : validerPerioderErLukketOgGyldig(periodeMap, felt);

        if (!nyFeil.isEmpty()) {
            throw new ValideringsAvbrytendeFeilException(nyFeil);
        }
        try {
            return toLocalDateTimeline(new ArrayList<>(periodeMap.keySet()));
        } catch (IllegalArgumentException e) {
            throw new ValideringsAvbrytendeFeilException(List.of(lagFeil(felt, "IllegalArgumentException", e.getMessage())));
        }
    }

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(List<Periode> periodeList, String felt, List<Feil> feil) throws ValideringsAvbrytendeFeilException {
        var nyFeil = validerPerioderErLukketOgGyldig(periodeList, felt);
        if (!nyFeil.isEmpty()) {
            feil.addAll(nyFeil);
            throw new ValideringsAvbrytendeFeilException(feil);
        }
        try {
            return toLocalDateTimeline(periodeList);
        } catch (IllegalArgumentException e) {
            feil.add(lagFeil(felt, "IllegalArgumentException", e.getMessage()));
            throw new ValideringsAvbrytendeFeilException(feil);
        }
    }

    private List<Feil> validerPerioderErLukketOgGyldig(List<Periode> periodeList, String felt) {
        var feil = new ArrayList<Feil>();
        for (int i = 0; i < periodeList.size(); i++ ) {
            var periode = periodeList.get(i);
            if (periode != null) {
                validerPerioderErLukket(periode, felt + "[" + i + "]", feil);
                validerPerioderIkkeErInvertert(periode, felt + "[" + i + "]", feil);
            }
        }
        return feil;
    }


    private List<Feil> validerPerioderErLukketOgGyldig(Map<Periode, ?> perioder, String felt) {
        var feil = new ArrayList<Feil>();
        perioder.keySet().forEach(p -> {
            validerPerioderErLukket(p, felt + "['" + p + "']", feil);
            validerPerioderIkkeErInvertert(p, felt + "['" + p + "']", feil);
        });
        return feil;
    }

    private List<Feil> validerPerioderGyldig(Map<Periode, ?> perioder, String felt) {
        var feil = new ArrayList<Feil>();
        perioder.keySet().forEach(p -> validerPerioderIkkeErInvertert(p, felt + "['" + p + "']", feil));
        return feil;
    }

    private void validerPerioderErLukket(Periode periode, String felt, List<Feil> feil) {
        if (periode.getTilOgMed() == null) {
            feil.add(lagFeil(felt, "påkrevd", "Til og med (TOM) må være satt."));
        }
        if (periode.getFraOgMed() == null) {
            feil.add(lagFeil(felt, "påkrevd", "Fra og med (FOM) må være satt."));
        }
    }

    private void validerPerioderIkkeErInvertert(Periode periode, String felt, List<Feil> feil) {
        if (periode.getFraOgMed() != null && periode.getTilOgMed() != null && periode.getTilOgMed().isBefore(periode.getFraOgMed())) {
            feil.add(lagFeil(felt, "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM)."));
        }
    }

    private List<Feil> validerAtIngenPerioderOverlapperMedTrekkKravPerioder(LocalDateTimeline<Boolean> trekkKravPerioder,
                                                                            LocalDateTimeline<Boolean> søktePerioder,
                                                                            String felt) {
        return tilPeriodeList(trekkKravPerioder.intersection(søktePerioder)).stream()
                .map(p -> toFeil(p, felt, "ugyldigPeriodeInterval", "Søkt periode overlapper med trekk krav periode: "))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Feil toFeil(Periode periode, String felt, String feilkode, String feilmelding) {
        return lagFeil(felt, feilkode, feilmelding + periode.toString());
    }

    private Feil lagFeil(String felt, String feilkode, String feilmelding) {
        return new Feil("ytelse." + felt, feilkode, feilmelding);
    }

    static class ValideringsAvbrytendeFeilException extends RuntimeException {

        private final List<Feil> feilList;

        public ValideringsAvbrytendeFeilException(List<Feil> feilList) {
            this.feilList = feilList;
        }

        public List<Feil> getFeilList() {
            return feilList;
        }
    }
}
