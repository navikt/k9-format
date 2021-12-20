package no.nav.k9.søknad.ytelse.pls.v1;

import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                feilene.add(lagFeil("arbeidstid", "påkrevd", "det finnes ingen søknadsperiode"));
            }
            feilene.addAll(validerPerioderErLukketOgGyldig(søknad.getBosteder().getPerioder(), "bosteder.perioder"));
            feilene.addAll(validerPerioderErLukketOgGyldig(søknad.getBosteder().getPerioderSomSkalSlettes(), "bosteder.perioderSomSkalSlettes"));
            feilene.addAll(validerPerioderErLukketOgGyldig(søknad.getUtenlandsopphold().getPerioder(), "utenlandsopphold.perioder"));
            feilene.addAll(validerPerioderErLukketOgGyldig(søknad.getUtenlandsopphold().getPerioderSomSkalSlettes(), "utenlandsopphold.perioderSomSkalSlettes"));
            validerArbeidstid(søknad);
            validerOpptjening(søknad, feilene);
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
