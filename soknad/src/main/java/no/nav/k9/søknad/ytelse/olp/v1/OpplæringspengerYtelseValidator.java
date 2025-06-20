package no.nav.k9.søknad.ytelse.olp.v1;

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
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.Kursholder;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.Reise;

class OpplæringspengerYtelseValidator extends YtelseValidator {

    private final String YTELSE_FELT = "ytelse.";

    @Override
    public List<Feil> valider(Ytelse ytelse) {
        return validerMedGyldigEndringsperodeHvisDenFinnes(ytelse, List.of());
    }

    @Override
    public List<Feil> valider(Ytelse ytelse, List<Periode> gyldigeEndringsperioder) {
        Objects.requireNonNull(gyldigeEndringsperioder, "gyldigeEndringsperioder");
        return validerMedGyldigEndringsperodeHvisDenFinnes(ytelse, gyldigeEndringsperioder);
    }

    @Override
    public void forsikreValidert(Ytelse ytelse) {
        List<Feil> feil = validerMedGyldigEndringsperodeHvisDenFinnes(ytelse, List.of());
        if (!feil.isEmpty()) {
            throw new ValideringsFeil(feil);
        }
    }

    public void forsikreValidert(Ytelse ytelse, List<Periode> gyldigeEndringsperioder) {
        Objects.requireNonNull(gyldigeEndringsperioder, "gyldigeEndringsperioder");

        List<Feil> feil = validerMedGyldigEndringsperodeHvisDenFinnes(ytelse, gyldigeEndringsperioder);
        if (!feil.isEmpty()) {
            throw new ValideringsFeil(feil);
        }
    }

    List<Feil> validerMedGyldigEndringsperodeHvisDenFinnes(Ytelse ytelse,
                                                           List<Periode> gyldigeEndringsperioder) {

        Objects.requireNonNull(gyldigeEndringsperioder, "gyldigeEndringsperioder");

        var olp = (Opplæringspenger) ytelse;
        var feilene = new ArrayList<Feil>();

        try {
            feilene.addAll(validerOgLeggTilFeilene(olp, gyldigeEndringsperioder));
        } catch (ValideringsAvbrytendeFeilException valideringsAvbrytendeFeilException) {
            feilene.addAll(valideringsAvbrytendeFeilException.getFeilList());
        }

        return feilene;
    }

    List<Feil> validerOgLeggTilFeilene(Opplæringspenger olp,
                                       List<Periode> gyldigeEndringsperioder) {
        final List<Feil> feilene = new ArrayList<Feil>();

        validerLovligEndring(olp, gyldigeEndringsperioder, feilene);

        final LocalDateTimeline<Boolean> søknadsperiodeTidslinje = lagTidslinjeOgValider(olp.getSøknadsperiodeList(), "søknadsperiode.perioder", feilene);
        final LocalDateTimeline<Boolean> intervalForEndringTidslinje;


        final LocalDateTimeline<Boolean> gyldigEndringsperiodeTidslinje = lagTidslinjeOgValider(gyldigeEndringsperioder, "gyldigeEndringsperioder.perioder", feilene);
        intervalForEndringTidslinje = søknadsperiodeTidslinje.union(gyldigEndringsperiodeTidslinje, StandardCombinators::coalesceLeftHandSide);

        final LocalDateTimeline<Boolean> trekkKravPerioderTidslinje = lagTidslinjeOgValider(olp.getTrekkKravPerioder(), "trekkKravPerioder.perioder", feilene);
        feilene.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, søknadsperiodeTidslinje, "trekkKravPerioder"));

        for (var ytelsePeriode : PerioderMedEndringUtil.getAllePerioderSomMåVæreInnenforSøknadsperiode(olp)) {
            if (ytelsePeriode.getPeriodeMap().keySet().stream().anyMatch(Periode::isTilOgMedFørFraOgMed)) {
                continue; //fortsette validering med ugyldige perioder gir bare duplikate feil
            }
            var ytelsePeriodeTidsserie = lagTidslinjeOgValider(ytelsePeriode.getPeriodeMap(), ytelsePeriode.getFelt() + ".perioder", feilene);
            feilene.addAll(validerAtYtelsePerioderErInnenforIntervalForEndring(intervalForEndringTidslinje, ytelsePeriodeTidsserie, ytelsePeriode.getFelt() + ".perioder"));
            feilene.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, ytelsePeriodeTidsserie, ytelsePeriode.getFelt() + ".perioder"));
        }

        validerReise(olp.getKurs().getReise(), "kurs.reise", feilene);
        validerReisetidMotKursperioden(olp.getKurs().getKursperioder(), olp.getKurs().getReise(), "kurs.reise", feilene);
        validerKursholder(olp.getKurs().getKursholder(), feilene);

        return feilene;
    }

    private void validerLovligEndring(Opplæringspenger olp, List<Periode> gyldigeEndringsperioder, List<Feil> feil) {
        if (olp.getSøknadsperiodeList().isEmpty() && gyldigeEndringsperioder.isEmpty()) {
            feil.add(lagFeil("søknadsperiode", "missingArgument", "Mangler søknadsperiode eller gyldigeEndringsperioder."));
            throw new ValideringsAvbrytendeFeilException(feil);
        }
    }

    private List<Feil> validerAtYtelsePerioderErInnenforIntervalForEndring(LocalDateTimeline<Boolean> gyldigInterval,
                                                                           LocalDateTimeline<Boolean> testTidsserie,
                                                                           String felt) {
        return tilPeriodeList(
                testTidsserie.disjoint(gyldigInterval)).stream()
                .filter(this::periodeInneholderDagerSomIkkeErHelg)
                .map(p -> toFeil(p, felt, "ugyldigPeriode",
                        "Perioden er utenfor gyldig interval. Gyldig interva: (" + gyldigInterval.getLocalDateIntervals() + "), Ugyldig periode: "))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void validerReise(Reise reise, String felt, List<Feil> feilene) {
        if (reise.isReiserUtenforKursdager()) {
            if (reise.getReisedager() == null || reise.getReisedager().isEmpty()) {
                feilene.add(lagFeil(felt, "påkrevd", "Reisedager må inneholde minst en dag når reiserUtenforKursdager er satt."));
            }
            if (reise.getReisedagerBeskrivelse() == null) {
                feilene.add(lagFeil(felt, "påkrevd", "Beskrivelse må være satt når reiserUtenforKursdager er satt."));
            }
        }
    }

    private void validerReisetidMotKursperioden(List<Periode> kursperioder, Reise reise, String felt, List<Feil> feil) {
        var reisedager = reise.getReisedager();
        if (reisedager == null || reisedager.isEmpty()) {
            return;
        }

        // En reisedag må være innenfor en kursperiode
        for (LocalDate reisedag : reisedager) {
            if (reisedag != null) {
                if (kursperioder.stream().noneMatch(kursPeriode -> kursPeriode.inneholder(new Periode(reisedag, reisedag)))) {
                    feil.add(lagFeil(felt, "ugyldigReise", "Reisedagen er ikke innenfor en kursperiode: " + reisedag));
                }
            }
        }
    }

    private void validerKursholder(Kursholder kursholder, List<Feil> feilene) {
        if (kursholder.getInstitusjonUuid() == null && (kursholder.getNavn() == null || kursholder.getNavn().isEmpty())) {
            feilene.add(lagFeil("kurs.kursholder", "påkrevd", "Enten id eller navn på institusjon må være satt"));
        }
    }

    private List<Feil> validerAtIngenPerioderOverlapperMedTrekkKravPerioder(LocalDateTimeline<Boolean> trekkKravPerioder,
                                                                            LocalDateTimeline<Boolean> test,
                                                                            String felt) {
        return tilPeriodeList(trekkKravPerioder.intersection(test)).stream()
                .map(p -> toFeil(p, felt, "ugyldigPeriodeInterval", "Overlapper med trekk krav periode: "))
                .collect(Collectors.toCollection(ArrayList::new));
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

    private Feil toFeil(Periode periode, String felt, String feilkode, String feilmelding) {
        return lagFeil(felt, feilkode, feilmelding + periode.toString());
    }

    private Feil lagFeil(String felt, String feilkode, String feilmelding) {
        return new Feil(YTELSE_FELT + felt, feilkode, feilmelding);
    }

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(List<Periode> periodeList, String felt, List<Feil> feil) throws ValideringsAvbrytendeFeilException {
        var nyFeil = validerPerioderErLukket(periodeList, felt);
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

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(Map<Periode, ?> periodeMap, String felt, List<Feil> feil) throws ValideringsAvbrytendeFeilException {
        var nyFeil = validerPerioderErLukket(periodeMap, felt);
        if (!nyFeil.isEmpty()) {
            feil.addAll(nyFeil);
            throw new ValideringsAvbrytendeFeilException(feil);
        }
        try {
            return toLocalDateTimeline(new ArrayList<>(periodeMap.keySet()));
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

    private List<Feil> validerPerioderErLukket(List<Periode> periodeList, String felt) {
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
