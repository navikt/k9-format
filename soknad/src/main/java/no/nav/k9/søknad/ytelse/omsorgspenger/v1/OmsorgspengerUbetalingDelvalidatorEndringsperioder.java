package no.nav.k9.søknad.ytelse.omsorgspenger.v1;

import static no.nav.k9.søknad.TidsserieUtils.tilPeriodeList;
import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import no.nav.fpsak.tidsserie.LocalDateSegment;
import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.fpsak.tidsserie.StandardCombinators;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.fravær.AktivitetFravær;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;

class OmsorgspengerUbetalingDelvalidatorEndringsperioder {

    public List<Feil> validerMotGyldigEndringsperode(Ytelse ytelse, List<Periode> gyldigeEndringsperioder) {
        Objects.requireNonNull(gyldigeEndringsperioder, "gyldigeEndringsperioder");

        var omsorgspengerUtbetaling = (OmsorgspengerUtbetaling) ytelse;
        var feil = new ArrayList<Feil>();

        try {
            feil.addAll(validerMotGyldigePerioder(omsorgspengerUtbetaling, gyldigeEndringsperioder));
        } catch (ValideringsAvbrytendeFeilException valideringsAvbrytendeFeilException) {
            feil.addAll(valideringsAvbrytendeFeilException.getFeilList());
        }

        return feil;
    }

    /**
     * for å kunne validere endringer mer nøyaktig fra punsj
     */
    public List<Feil> validerMotGyldigEndringsperode(Ytelse ytelse, Map<AktivitetFravær, List<Periode>> gyldigeEndringsperioderSøknad, Map<Organisasjonsnummer, List<Periode>> gyldigeKorrigeringsperioderIm) {
        var omsorgspengerUtbetaling = (OmsorgspengerUtbetaling) ytelse;
        var feil = new ArrayList<Feil>();

        try {
            feil.addAll(validerSøknadMotGyldigePerioder(omsorgspengerUtbetaling, gyldigeEndringsperioderSøknad));
            feil.addAll(validerKorrigeringImMotGyldigePerioder(omsorgspengerUtbetaling, gyldigeKorrigeringsperioderIm));
        } catch (ValideringsAvbrytendeFeilException valideringsAvbrytendeFeilException) {
            feil.addAll(valideringsAvbrytendeFeilException.getFeilList());
        }
        return feil;
    }

    private List<Feil> validerMotGyldigePerioder(OmsorgspengerUtbetaling omp, List<Periode> gyldigeEndringsperioder) {
        List<Feil> feilene = new ArrayList<>();
        if (omp.getFraværsperioder() != null) {
            feilene.addAll(validerSøknadMotGyldigePerioder(omp.getFraværsperioder(), gyldigeEndringsperioder));
        }
        if (omp.getFraværsperioderKorrigeringIm() != null) {
            feilene.addAll(validerKorrigeringImMotGyldigePerioder(omp.getFraværsperioderKorrigeringIm(), gyldigeEndringsperioder));
        }
        return feilene;
    }

    private List<Feil> validerKorrigeringImMotGyldigePerioder(OmsorgspengerUtbetaling omp, Map<Organisasjonsnummer, List<Periode>> gyldigeKorrigeringsperioderIm) {
        if (omp.getFraværsperioderKorrigeringIm() == null) {
            return List.of();
        }
        List<Feil> feilene = new ArrayList<>();
        Map<Organisasjonsnummer, List<FraværPeriode>> korrigeringerPrArbeidsgiver = omp.getFraværsperioderKorrigeringIm().stream().collect(Collectors.groupingBy(FraværPeriode::getArbeidsgiverOrgNr));
        korrigeringerPrArbeidsgiver.forEach((aktuellArbeidsgiver, korrigeringer) -> feilene.addAll(validerKorrigeringImMotGyldigePerioder(korrigeringer, gyldigeKorrigeringsperioderIm.get(aktuellArbeidsgiver))));
        return feilene;
    }

    List<Feil> validerSøknadMotGyldigePerioder(OmsorgspengerUtbetaling omp, Map<AktivitetFravær, List<Periode>> gyldigeEndringsperioder) {
        if (omp.getFraværsperioder() == null) {
            return List.of();
        }
        List<Feil> feilene = new ArrayList<>();
        for (AktivitetFravær aktivitestype : AktivitetFravær.values()) {
            List<FraværPeriode> aktuelleFraværPerioder = omp.getFraværsperioder().stream().filter(fp -> fp.getAktivitetFravær().contains(aktivitestype)).toList();
            List<Periode> aktuelleEndringsperioder = gyldigeEndringsperioder.getOrDefault(aktivitestype, List.of());
            feilene.addAll(validerSøknadMotGyldigePerioder(aktuelleFraværPerioder, aktuelleEndringsperioder));
        }
        return feilene;
    }

    private List<Feil> validerSøknadMotGyldigePerioder(List<FraværPeriode> fraværPerioder, List<Periode> gyldigeEndringsperioder) {
        final LocalDateTimeline<Boolean> fraværsperioderTidslinje = lagTidslinjeGodtaOverlapp(tilPerioder(fraværPerioder), "fraværsperioder.perioder");
        final LocalDateTimeline<Boolean> gyldigEndringsperiodeTidslinje = lagTidslinjeOgValider(gyldigeEndringsperioder, "gyldigeEndringsperioder.perioder");
        return validerAtYtelsePerioderErInnenforIntervalForEndring(fraværsperioderTidslinje, gyldigEndringsperiodeTidslinje, "fraværsperioder.perioder");
    }

    private List<Feil> validerKorrigeringImMotGyldigePerioder(List<FraværPeriode> fraværPerioder, List<Periode> gyldigeEndringsperioder) {
        final LocalDateTimeline<Boolean> fraværsperioderKorrigeringImTidslinje = lagTidslinjeGodtaOverlapp(tilPerioder(fraværPerioder), "fraværsperioderKorrigeringIm.perioder");
        final LocalDateTimeline<Boolean> gyldigEndringsperiodeTidslinje = lagTidslinjeOgValider(gyldigeEndringsperioder, "gyldigeEndringsperioder.perioder");
        return validerAtYtelsePerioderErInnenforIntervalForEndring(fraværsperioderKorrigeringImTidslinje, gyldigEndringsperiodeTidslinje, "fraværsperioderKorrigeringIm.perioder");
    }

    private static List<Periode> tilPerioder(List<FraværPeriode> fraværPerioder) {
        return fraværPerioder.stream().map(FraværPeriode::getPeriode).toList();
    }

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(List<Periode> periodeList, String felt) throws ValideringsAvbrytendeFeilException {
        var nyFeil = validerPerioderErLukketOgGyldig(periodeList, felt);
        if (!nyFeil.isEmpty()) {
            throw new ValideringsAvbrytendeFeilException(nyFeil);
        }
        try {
            return toLocalDateTimeline(periodeList);
        } catch (RuntimeException e) {
            throw new ValideringsAvbrytendeFeilException(List.of(lagFeil(felt, e.getClass().getSimpleName(), e.getMessage())));
        }
    }

    private LocalDateTimeline<Boolean> lagTidslinjeGodtaOverlapp(List<Periode> perioder, String felt) throws ValideringsAvbrytendeFeilException {
        var nyFeil = validerPerioderErLukketOgGyldig(perioder, felt);
        if (!nyFeil.isEmpty()) {
            throw new ValideringsAvbrytendeFeilException(nyFeil);
        }
        List<LocalDateSegment<Boolean>> segmenter = perioder.stream().map(p -> new LocalDateSegment<>(p.getFraOgMed(), p.getTilOgMed(), true)).toList();
        return new LocalDateTimeline<>(segmenter, StandardCombinators::alwaysTrueForMatch);
    }

    private List<Feil> validerAtYtelsePerioderErInnenforIntervalForEndring(
            LocalDateTimeline<Boolean> søknadsperiodeTidslinje,
            LocalDateTimeline<Boolean> gyldigePerioderTidslinje,
            String felt) {

        LocalDateTimeline<Boolean> fellesPerioder = søknadsperiodeTidslinje.disjoint(gyldigePerioderTidslinje);

        return tilPeriodeList(fellesPerioder)
                .stream()
                .map((Periode ugyldigPeriode) -> toFeil(ugyldigPeriode, felt, "ugyldigPeriode",
                        "Perioden er utenfor gyldig interval. Gyldig interval: (" + gyldigePerioderTidslinje.getLocalDateIntervals() + "), Ugyldig periode: "))
                .toList();
    }

    private List<Feil> validerPerioderErLukketOgGyldig(List<Periode> periodeList, String felt) {
        var feil = new ArrayList<Feil>();
        for (int i = 0; i < periodeList.size(); i++) {
            var periode = periodeList.get(i);
            if (periode != null) {
                validerPerioderErLukket(periode, felt + "[" + i + "]", feil);
            }
        }
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

    private Feil toFeil(Periode periode, String felt, String feilkode, String feilmelding) {
        return lagFeil(felt, feilkode, feilmelding + periode.toString());
    }

    private Feil lagFeil(String felt, String feilkode, String feilmelding) {
        return new Feil("ytelse." + felt, feilkode, feilmelding);
    }


}
