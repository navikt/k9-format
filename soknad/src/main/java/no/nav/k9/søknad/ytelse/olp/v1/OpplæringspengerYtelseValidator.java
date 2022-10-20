package no.nav.k9.søknad.ytelse.olp.v1;

import static no.nav.k9.søknad.TidsserieUtils.tilPeriodeList;
import static no.nav.k9.søknad.TidsserieUtils.toLocalDateTimeline;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.fpsak.tidsserie.StandardCombinators;
import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.KursPeriodeMedReisetid;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.Kursholder;

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

        feilene.addAll(validerPerioderErLukketOgGyldig(olp.getBosteder().getPerioder(), "bosteder.perioder"));
        feilene.addAll(validerPerioderErLukketOgGyldig(olp.getUtenlandsopphold().getPerioder(), "utenlandsopphold.perioder"));

        final LocalDateTimeline<Boolean> søknadsperiodeTidslinje = lagTidslinjeOgValider(olp.getSøknadsperiodeList(), "søknadsperiode.perioder", feilene);
        final LocalDateTimeline<Boolean> intervalForEndringTidslinje;


        final LocalDateTimeline<Boolean> gyldigEndringsperiodeTidslinje = lagTidslinjeOgValider(gyldigeEndringsperioder, "gyldigeEndringsperioder.perioder", feilene);
        intervalForEndringTidslinje = søknadsperiodeTidslinje.union(gyldigEndringsperiodeTidslinje, StandardCombinators::coalesceLeftHandSide);

        final LocalDateTimeline<Boolean> trekkKravPerioderTidslinje = lagTidslinjeOgValider(olp.getTrekkKravPerioder(), "trekkKravPerioder.perioder", feilene);
        feilene.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, søknadsperiodeTidslinje, "trekkKravPerioder"));

        for (var ytelsePeriode : PerioderMedEndringUtil.getAllePerioderSomMåVæreInnenforSøknadsperiode(olp)) {
            var ytelsePeriodeTidsserie = lagTidslinjeOgValider(ytelsePeriode.getPeriodeMap(), ytelsePeriode.getFelt() + ".perioder", feilene);
            feilene.addAll(validerAtYtelsePerioderErInnenforIntervalForEndring(intervalForEndringTidslinje, ytelsePeriodeTidsserie, ytelsePeriode.getFelt() + ".perioder"));
            feilene.addAll(validerAtIngenPerioderOverlapperMedTrekkKravPerioder(trekkKravPerioderTidslinje, ytelsePeriodeTidsserie, ytelsePeriode.getFelt() + ".perioder"));
        }

        validerAtYtelsePeriodenErKomplettMedSøknad(søknadsperiodeTidslinje, olp.getUttak().getPerioder(), "uttak", feilene);

        validerKursholderOgInstitusjonUuid(olp.getKurs().getKursholder(), "kurs.kursholder", feilene);

        validerReisetidMotKursperioden(olp.getKurs().getKursperioder(), "kurs.kursperioder", feilene);

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

    private void validerAtYtelsePeriodenErKomplettMedSøknad(LocalDateTimeline<Boolean> søknadsperiode,
                                                            Map<Periode, ?> ytelsePeriode,
                                                            String felt,
                                                            List<Feil> feil) {
        feil.addAll(tilPeriodeList(søknadsperiode.disjoint(lagTidslinjeOgValider(new ArrayList<>(ytelsePeriode.keySet()), felt, feil))).stream()
                .filter(this::periodeInneholderDagerSomIkkeErHelg)
                .map(p -> toFeil(p, felt, "ikkeKomplettPeriode", "Periodene er ikke komplett, periode som mangler er: "))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    private void validerKursholderOgInstitusjonUuid(Kursholder kursholder, String felt, List<Feil> feil) {
        String holder = kursholder.getHolder();
        UUID institusjonUuid = kursholder.getInstitusjonUuid();

        if (holder == null && institusjonUuid == null) {
            feil.add(lagFeil(felt, "ugyldigHolderEllerInstitusjonUuid", "Enten holder eller institusjonUuid må være satt."));
        } else if (holder != null && institusjonUuid != null) {
            feil.add(lagFeil(felt, "ugyldigHolderEllerInstitusjonUuid", "Kan ikke ha både holder og institusjonUuid satt samtidig."));
        }
    }

    private void validerReisetidMotKursperioden(List<KursPeriodeMedReisetid> kursperioder, String felt, List<Feil> feil) {
        for (KursPeriodeMedReisetid kursPeriode : kursperioder) {
            if (kursPeriode != null) {
                LocalDate avreise = kursPeriode.getAvreise();
                LocalDate hjemkomst = kursPeriode.getHjemkomst();
                Periode periode = kursPeriode.getPeriode();

                if (avreise != null && hjemkomst != null && periode != null) {
                    if (hjemkomst.isBefore(avreise)) {
                        feil.add(toFeil(periode, felt, "ugyldigKursPeriode", "hjemkomst er før avreise: "));
                    }
                    if (avreise.isAfter(periode.getFraOgMed())) {
                        feil.add(toFeil(periode, felt, "ugyldigKursPeriode", "avreise er etter kursstart: "));
                    }
                    if (hjemkomst.isBefore(periode.getTilOgMed())) {
                        feil.add(toFeil(periode, felt, "ugyldigKursPeriode", "hjemkomst er før kursslutt: "));
                    }
                }
            }
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

    private LocalDateTimeline<Boolean> lagTidslinjeOgValider(Map<Periode, ?> periodeMap, String felt, List<Feil> feil) throws ValideringsAvbrytendeFeilException {
        var nyFeil = validerPerioderErLukketOgGyldig(periodeMap, felt);
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

    private List<Feil> validerPerioderErLukketOgGyldig(Map<Periode, ?> perioder, String felt) {
        var feil = new ArrayList<Feil>();
        perioder.keySet().forEach(p -> {
            validerPerioderErLukket(p, felt + "['" + p + "']", feil);
            validerPerioderIkkeErInvertert(p, felt + "['" + p + "']", feil);
        });
        return feil;
    }

    private List<Feil> validerPerioderErLukketOgGyldig(List<Periode> periodeList, String felt) {
        var feil = new ArrayList<Feil>();
        for (int i = 0; i < periodeList.size(); i++) {
            var periode = periodeList.get(i);
            if (periode != null) {
                validerPerioderErLukket(periode, felt + "[" + i + "]", feil);
                validerPerioderIkkeErInvertert(periode, felt + "[" + i + "]", feil);
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

    private void validerPerioderIkkeErInvertert(Periode periode, String felt, List<Feil> feil) {
        if (periode.getFraOgMed() != null && periode.getTilOgMed() != null && periode.getTilOgMed().isBefore(periode.getFraOgMed())) {
            feil.add(lagFeil(felt, "ugyldigPeriode", "Fra og med (FOM) må være før eller lik til og med (TOM)."));
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
