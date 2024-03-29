package no.nav.k9.søknad.ytelse.pls.v1;

import static no.nav.k9.søknad.TidsserieUtils.tilPeriodeList;
import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.fpsak.tidsserie.StandardCombinators;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.opptjening.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.psb.v1.PerioderMedEndring;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;

/**
 * @deprecated bruk istedet {@link PleiepengerLivetsSluttfaseSøknadValidator}
 */
@Deprecated(forRemoval = true, since = "6.1.1") //må ikke fjernes helt, men skal fjernes fra API-et
public class PleiepengerLivetsSluttfaseYtelseValidator extends YtelseValidator {
    @Override
    public List<Feil> valider(Ytelse søknad) {
        return valider((PleipengerLivetsSluttfase) søknad, List.of());
    }

    public List<Feil> valider(PleipengerLivetsSluttfase søknad, List<Periode> gyldigeEndringsperioder) {
        return validerMedGyldigEndringsperodeHvisDenFinnes(søknad, gyldigeEndringsperioder);
    }

    List<Feil> validerMedGyldigEndringsperodeHvisDenFinnes(PleipengerLivetsSluttfase søknad, List<Periode> gyldigeEndringsperioder) {
        List<Feil> feilene = new ArrayList<>();
        try {
            validerLovligEndring(søknad, gyldigeEndringsperioder);

            validerArbeidstid(søknad);
            validerOpptjening(søknad, feilene);

            if (søknad.getSøknadsperiodeList().stream().anyMatch(Periode::isTilOgMedFørFraOgMed)){
                return feilene; //får duplikate feil hvis vi her går videre med ugyldige perioder
            }
            var søknadsperiodeTidslinje = lagTidslinjeOgValider(søknad.getSøknadsperiodeList(), "søknadsperiode.perioder", feilene);
            var uttakTidslinje = lagTidslinjeOgValider(new ArrayList<>(søknad.getUttak().getPerioder().keySet()), "uttak.perioder", feilene);
            validerAtUttakErKomplettForSøknadsperiode(søknadsperiodeTidslinje, uttakTidslinje, "uttak.perioder", feilene);

            var trekkKravPerioderTidslinje = lagTidslinjeOgValider(søknad.getTrekkKravPerioder(), "trekkKravPerioder.perioder", feilene);
            validerTrekkKravPerioder(trekkKravPerioderTidslinje, søknadsperiodeTidslinje, feilene);

            var gyldigEndringsperiodeTidslinje = lagTidslinjeOgValider(gyldigeEndringsperioder, "gyldigeEndringsperioder.perioder", feilene);
            var intervalForEndringTidslinje = søknadsperiodeTidslinje.union(gyldigEndringsperiodeTidslinje, StandardCombinators::coalesceLeftHandSide);
            List<PerioderMedEndring> perioderMedEndring = PleiepengerLivetsSluttfasePerioderMedEndringUtil.getAllePerioderSomMåVæreInnenforSøknadsperiode(søknad);
            for (PerioderMedEndring ytelsePeriode : perioderMedEndring) {
                if (ytelsePeriode.getPeriodeMap().keySet().stream().anyMatch(Periode::isTilOgMedFørFraOgMed)){
                    continue; //ugyldig periode, får duplikate feil hvis vi går videre med periodene
                }
                var ytelsePeriodeTidslinje = lagTidslinjeOgValider(ytelsePeriode.getPeriodeMap(), ytelsePeriode.getFelt() + ".perioder", feilene);
                feilene.addAll(validerAtYtelsePerioderErInnenforIntervalForEndring(intervalForEndringTidslinje, ytelsePeriodeTidslinje, ytelsePeriode.getFelt() + ".perioder"));
                feilene.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, ytelsePeriodeTidslinje, ytelsePeriode.getFelt() + ".perioder"));
            }

        } catch (ValideringsAvbrytendeFeilException valideringsAvbrytendeFeilException) {
            feilene.addAll(valideringsAvbrytendeFeilException.getFeilList());
        }
        return feilene;
    }

    // Brukes av Punsj for å sende inn endringer på allerede mottatte søknadsperioder. Komplett datasett kreves ikke for disse perioden
    @Override
    public void forsikreValidert(Ytelse ytelse, List<Periode> gyldigeEndringsperioder) {
        Objects.requireNonNull(gyldigeEndringsperioder, "gyldigeEndringsperioder");

        var feilene = valider((PleipengerLivetsSluttfase) ytelse, gyldigeEndringsperioder);
        if (!feilene.isEmpty()) {
            throw new ValideringsFeil(feilene);
        }
    }

    private void validerLovligEndring(PleipengerLivetsSluttfase ytelse, List<Periode> gyldigeEndringsperioder) {
        /*
         * Merk: Vi tillater trekk av periode som ikke finnes -- siden dette ikke gir noen negative konsekvenser,
         */
        if (ytelse.getSøknadsperiodeList().isEmpty() && gyldigeEndringsperioder.isEmpty() && ytelse.getTrekkKravPerioder().isEmpty()) {
            throw new ValideringsAvbrytendeFeilException(List.of(
                    lagFeil("søknadsperiode", "missingArgument", "Mangler søknadsperiode eller gyldigeEndringsperioder.")));
        }
    }

    private void validerArbeidstid(PleipengerLivetsSluttfase søknad) throws ValideringsAvbrytendeFeilException {
        List<Arbeidstaker> arbeidstakerList = søknad.getArbeidstid().getArbeidstakerList();
        for (int i = 0; i < arbeidstakerList.size(); i++) {
            Map<Periode, ArbeidstidPeriodeInfo> perioder = arbeidstakerList.get(i).getArbeidstidInfo().getPerioder();
            lagTidslinjeOgValiderHvisPerioderGyldige(perioder, "arbeidstid.arbeidstakerList[" + i + "].perioder");
        }
        søknad.getArbeidstid().getSelvstendigNæringsdrivendeArbeidstidInfo().ifPresent(sn -> lagTidslinjeOgValiderHvisPerioderGyldige(sn.getPerioder(), "arbeidstid.selvstendigNæringsdrivendeArbeidstidInfo"));
        søknad.getArbeidstid().getFrilanserArbeidstidInfo().ifPresent(fl -> lagTidslinjeOgValiderHvisPerioderGyldige(fl.getPerioder(), "arbeidstid.frilanserArbeidstidInfo"));
    }

    private void lagTidslinjeOgValiderHvisPerioderGyldige(Map<Periode, ?> periodeMap, String felt) throws ValideringsAvbrytendeFeilException {
        if (periodeMap.keySet().stream().noneMatch(Periode::isTilOgMedFørFraOgMed)){
            //får duplikate feilmeldinger hvis vi forsøker å konstruere tidsserie med ugyldige perioder
            lagTidslinjeOgValider(periodeMap, felt);
        }
    }

    private void validerOpptjening(PleipengerLivetsSluttfase søknad, List<Feil> feilene) {
        if (!søknad.skalHaOpplysningOmOpptjeningVedNyPeriode()) {
            feilene.add(new Feil("oppgittOpptjening", "påkrevd", "Opplysninger om opptjening må være oppgitt for ny søknadsperiode."));
        }
        var opptjeningAktivitet = søknad.getOpptjeningAktivitet();
        if (opptjeningAktivitet != null) {
            List<SelvstendigNæringsdrivende> snAktiviteter = opptjeningAktivitet.getSelvstendigNæringsdrivende();
            for (int i = 0; i < snAktiviteter.size(); i++) {
                lagTidslinjeMedStøtteForÅpenPeriodeOgValider(snAktiviteter.get(i).getPerioder(), "opptjeningAktivitet.selvstendigNæringsdrivende[" + i + "].perioder");
            }
        }
    }


    private void validerAtUttakErKomplettForSøknadsperiode(LocalDateTimeline<Boolean> søknadsperioderTidslinje,
                                                           LocalDateTimeline<Boolean> uttakTidslinje,
                                                           String felt,
                                                           List<Feil> feil) {

        var søknadsperioderUtenUttak = tilPeriodeList(søknadsperioderTidslinje.disjoint(uttakTidslinje));
        feil.addAll(søknadsperioderUtenUttak.stream()
                .filter(this::periodeInneholderDagerSomIkkeErHelg)
                .map(p -> toFeil(p, felt, "ikkeKomplettPeriode", "Periodene er ikke komplett, periode som mangler er: "))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    private boolean periodeInneholderDagerSomIkkeErHelg(Periode periode) {
        LocalDate testDag = periode.getFraOgMed();
        while (testDag.isBefore(periode.getTilOgMed()) || testDag.isEqual(periode.getTilOgMed())) {
            if (!((testDag.getDayOfWeek() == DayOfWeek.SUNDAY) || (testDag.getDayOfWeek() == DayOfWeek.SATURDAY))) {
                return true;
            }
            testDag = testDag.plusDays(1);
        }
        return false;
    }

    private List<Feil> validerAtYtelsePerioderErInnenforIntervalForEndring(LocalDateTimeline<Boolean> intervalForEndringTidslinje,
                                                                           LocalDateTimeline<Boolean> ytelsePeriodeLinje,
                                                                           String felt) {
        return tilPeriodeList(
                ytelsePeriodeLinje.disjoint(intervalForEndringTidslinje)).stream()
                .filter(this::periodeInneholderDagerSomIkkeErHelg)
                .map(p -> toFeil(p, felt, "ugyldigPeriode",
                        "Perioden er utenfor gyldig interval. Gyldig interval: (" + intervalForEndringTidslinje.getLocalDateIntervals() + "), Ugyldig periode: "))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void validerTrekkKravPerioder(LocalDateTimeline<Boolean> trekkKravPerioderTidslinje, LocalDateTimeline<Boolean> søknadsperiodeTidslinje, List<Feil> feilene) {
        feilene.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, søknadsperiodeTidslinje, "trekkKravPerioder"));
    }

    private LocalDateTimeline<Boolean> lagTidslinjeMedStøtteForÅpenPeriodeOgValider(Map<Periode, ?> periodeMap, String felt) throws ValideringsAvbrytendeFeilException {
        return lagTidslinjeOgValider(periodeMap, felt, true);
    }

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(Map<Periode, ?> periodeMap, String felt, List<Feil> feil) throws ValideringsAvbrytendeFeilException {
        try {
            return toLocalDateTimeline(new ArrayList<>(periodeMap.keySet()));
        } catch (IllegalArgumentException e) {
            feil.add(lagFeil(felt, "IllegalArgumentException", e.getMessage()));
            throw new ValideringsAvbrytendeFeilException(feil);
        }
    }

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(Map<Periode, ?> periodeMap, String felt) throws ValideringsAvbrytendeFeilException {
        return lagTidslinjeOgValider(periodeMap, felt, false);
    }

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(Map<Periode, ?> periodeMap, String felt, boolean godtaÅpenPeriode) throws ValideringsAvbrytendeFeilException {

        List<Feil> feil = godtaÅpenPeriode ? List.of() : validerPerioderErLukket(periodeMap, felt);

        if (!feil.isEmpty()) {
            throw new ValideringsAvbrytendeFeilException(feil);
        }
        try {
            // Kaster feil dersom overlappende perioder
            return toLocalDateTimeline(new ArrayList<>(periodeMap.keySet()));
        } catch (IllegalArgumentException e) {
            throw new ValideringsAvbrytendeFeilException(List.of(lagFeil(felt, "IllegalArgumentException", e.getMessage())));
        }
    }

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(List<Periode> periodeList, String felt, List<Feil> feil) throws ValideringsAvbrytendeFeilException {
        try {
            // Kaster feil dersom overlappende perioder
            return toLocalDateTimeline(periodeList);
        } catch (IllegalArgumentException e) {
            feil.add(lagFeil(felt, "IllegalArgumentException", e.getMessage()));
            throw new ValideringsAvbrytendeFeilException(feil);
        }
    }

    private List<Feil> validerPerioderErLukket(Map<Periode, ?> perioder, String felt) {
        var feil = new ArrayList<Feil>();
        perioder.keySet().forEach(p -> {
            validerPerioderErLukket(p, felt + "['" + p + "']", feil);
        });
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
