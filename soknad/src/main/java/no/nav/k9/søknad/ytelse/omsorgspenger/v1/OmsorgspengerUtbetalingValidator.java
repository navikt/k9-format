package no.nav.k9.søknad.ytelse.omsorgspenger.v1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import no.nav.fpsak.tidsserie.LocalDateSegment;
import no.nav.fpsak.tidsserie.LocalDateTimeline;
import no.nav.k9.søknad.PeriodeValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.fravær.AktivitetFravær;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.opptjening.Frilanser;
import no.nav.k9.søknad.felles.opptjening.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

/**
 * @deprecated bruk istedet {@link OmsorgspengerUtbetalingSøknadValidator}
 */
@Deprecated(forRemoval = true, since = "6.1.1") //må ikke fjernes helt, men skal fjernes fra API-et
public class OmsorgspengerUtbetalingValidator extends YtelseValidator {
    private final PeriodeValidator periodeValidator;
    private Versjon versjon;

    public OmsorgspengerUtbetalingValidator(Versjon versjon) {
        this.versjon = versjon;
        this.periodeValidator = new PeriodeValidator();
    }

    @Override
    public List<Feil> valider(Ytelse ytelse) {

        var omsorgspengerUtbetaling = (OmsorgspengerUtbetaling) ytelse;

        List<Feil> feil = new ArrayList<>();
        feil.addAll(validerPeriodeInnenforEttÅr(omsorgspengerUtbetaling));
        feil.addAll(validerAktivitet(omsorgspengerUtbetaling));
        feil.addAll(validerUtenlandsopphold(omsorgspengerUtbetaling));
        feil.addAll(validerFosterbarn(omsorgspengerUtbetaling));
        feil.addAll(validerBosteder(omsorgspengerUtbetaling));
        feil.addAll(validerFraværsperioderFraSøker(omsorgspengerUtbetaling));
        feil.addAll(validerFraværskorrigeringIm(omsorgspengerUtbetaling));
        feil.addAll(validerFraværsperiodeKilder(omsorgspengerUtbetaling));

        return feil;
    }

    private List<Feil> validerPeriodeInnenforEttÅr(OmsorgspengerUtbetaling ytelse) {
        if (ytelse.getFraværsperioder().stream().anyMatch(OmsorgspengerUtbetalingValidator::erPeriodeUgyldig)) {
            //avbryter her for å unngå NPE i koden under når periode ikke er satt, eller ikke er satt ordentlig
            //nødvendig siden validatoren brukes mens søknaden bygges i k9-punsj, og søknad vil således være ukomplett underveis
            //periode valideres andre steder uansett
            return List.of();
        }
        var søknadsperiode = ytelse.getSøknadsperiode();
        var yearMin = søknadsperiode.getFraOgMed().getYear();
        var yearMax = søknadsperiode.getTilOgMed().getYear();

        if (yearMin != yearMax) {
            return List.of(new Feil("fraværsperioder", "perioderOverFlereÅr", "Perioder kan ikke overstige ett år"));
        }
        return List.of();
    }

    private List<Feil> validerAktivitet(OmsorgspengerUtbetaling ytelse) {
        List<Feil> feil = new ArrayList<>();
        var aktivitet = ytelse.getAktivitet();

        if (aktivitet != null) {
            boolean erSøknadForFrilanser = aktivitet.getFrilanser() != null;
            boolean erSøknadForSN = aktivitet.getSelvstendigNæringsdrivende() != null && !aktivitet.getSelvstendigNæringsdrivende().isEmpty();
            if (erSøknadForFrilanser || erSøknadForSN) {
                feil.addAll(validerFrilanserOgSelvstendingNæringsdrivende(aktivitet.getSelvstendigNæringsdrivende(), aktivitet.getFrilanser()));
                feil.addAll(validerSelvstendingNæringsdrivende(aktivitet.getSelvstendigNæringsdrivende()));
                feil.addAll(validerFrilanser(aktivitet.getFrilanser()));
            }
        }
        return feil;
    }

    private List<Feil> validerFrilanserOgSelvstendingNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigeVirksomheter, Frilanser frilanser) {
        List<Feil> feil = new ArrayList<>();
        if (frilanser == null && (selvstendigeVirksomheter == null || selvstendigeVirksomheter.isEmpty())) {
            feil.add(new Feil("frilanser & selvstendingNæringsdrivene", PÅKREVD, "Enten frilanser eller selvstendingNæringsdrivende må være satt i søknaden."));
        }
        return feil;
    }

    private List<Feil> validerFrilanser(Frilanser frilanser) {
        List<Feil> feil = new ArrayList<>();
        if (frilanser == null) return feil;

        if (frilanser.getStartdato() != null && frilanser.getSluttdato() != null && frilanser.getStartdato().isAfter(frilanser.getSluttdato())) {
            feil.add(new Feil("frilanser.startdato", UGYLDIG_ARGUMENT, "'startdato' kan ikke være etter 'sluttdato'"));
        }
        return feil;
    }

    private List<Feil> validerSelvstendingNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigeVirksomheter) {
        List<Feil> feil = new ArrayList<>();
        if (selvstendigeVirksomheter == null || selvstendigeVirksomheter.isEmpty())
            return feil;
        var index = 0;
        for (SelvstendigNæringsdrivende sn : selvstendigeVirksomheter) {
            String snFelt = "selvstendigNæringsdrivende[" + index + "]";
            feil.addAll(this.periodeValidator.validerTillattOverlappOgÅpnePerioder(sn.getPerioder(), snFelt + ".perioder"));

            sn.getPerioder().forEach((periode, snInfo) -> {
                String periodeString = periode.getFraOgMed() + "-" + periode.getTilOgMed();
                String snInfoFelt = "selvstendigNæringsdrivende.perioder{" + periodeString + "}";

                if (snInfo.getErVarigEndring() != null && snInfo.getErVarigEndring()) {
                    if (snInfo.getEndringDato() == null) {
                        feil.add(new Feil(snInfoFelt + ".endringsDato", PÅKREVD, "endringDato må være satt dersom erVarigEndring er true."));
                    }
                    if (snInfo.getEndringBegrunnelse() == null || snInfo.getEndringBegrunnelse().isBlank()) {
                        feil.add(new Feil(snInfoFelt + ".endringBegrunnelse", PÅKREVD, "endringBegrunnelse må være satt dersom erVarigEndring er true."));
                    }
                }

                if (snInfo.getRegistrertIUtlandet() != null) {
                    if (snInfo.getRegistrertIUtlandet() && snInfo.getLandkode() == null) {
                        feil.add(new Feil(snInfoFelt + ".landkode", PÅKREVD,
                                "landkode må være satt, og kan ikke være null, dersom virksomhet er registrert i utlandet."));
                    }
                } else if (sn.getOrganisasjonsnummer() == null) {
                    feil.add(new Feil(snInfoFelt + ".registrertIUtlandet og " + snFelt + ".organisasjonsnummer", PÅKREVD,
                            "Dersom virksomheten er registrert i norge, må organisasjonsnummeret være satt."));
                }
            });
            index++;
        }
        return feil;
    }

    private List<Feil> validerUtenlandsopphold(OmsorgspengerUtbetaling ytelse) {
        var utenlandsopphold = ytelse.getUtenlandsopphold();
        if (utenlandsopphold == null) {
            return List.of();
        }
        return new PeriodeValidator().validerIkkeTillattOverlapp(utenlandsopphold.getPerioder(), "utenlandsopphold.perioder");
    }

    private List<Feil> validerFosterbarn(OmsorgspengerUtbetaling ytelse) {
        var fosterbarn = ytelse.getFosterbarn();
        if (fosterbarn == null || fosterbarn.isEmpty()) {
            return List.of();
        }
        var index = 0;
        List<Feil> feil = new ArrayList<>();
        for (Barn b : fosterbarn) {
            if (b.getPersonIdent() == null && b.getFødselsdato() == null) {
                feil.add(new Feil("fosterbarn[" + index + "]", "norskIdentitetsnummerEllerFødselsdatoPåkrevd",
                        "Må sette enten Personnummer/D-nummer på fosterbarn, eller fødselsdato."));
            } else if (b.getPersonIdent() != null && b.getFødselsdato() != null) {
                feil.add(
                        new Feil("fosterbarn[" + index + "]", "ikkeEntydigIdPåBarnet", "Må sette enten Personnummer/D-nummer på fosterbarn, eller fødselsdato."));
            }
            index++;
        }
        return feil;
    }

    private List<Feil> validerBosteder(OmsorgspengerUtbetaling ytelse) {
        var bosteder = ytelse.getBosteder();
        if (bosteder == null) {
            return List.of();
        }
        return new PeriodeValidator().validerIkkeTillattOverlapp(bosteder.getPerioder(), "bosteder.perioder");
    }

    private List<Feil> validerFraværskorrigeringIm(OmsorgspengerUtbetaling ytelse) {
        var fraværsperioderKorrigeringIm = ytelse.getFraværsperioderKorrigeringIm();
        if (fraværsperioderKorrigeringIm == null) {
            return List.of();
        }

        var index = 0;
        Organisasjonsnummer uniktOrgnr = null;
        String unikArbeidsforholdId = null;
        List<Feil> feil = new ArrayList<>();
        for (FraværPeriode fraværPeriode : fraværsperioderKorrigeringIm) {
            if (index == 0) {
                // Initier verdier for arbeidsforhold som skal være konsistente for alle fraværsperioder
                uniktOrgnr = fraværPeriode.getArbeidsgiverOrgNr();
                unikArbeidsforholdId = fraværPeriode.getArbeidsforholdId();
            }
            if (fraværPeriode.getArbeidsgiverOrgNr() == null) {
                feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "]", "ikkeSpesifisertOrgNr", "Må oppgi orgnr for aktivitet"));
            } else if (!Objects.equals(fraværPeriode.getArbeidsgiverOrgNr(), uniktOrgnr)) {
                feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "]", "ikkeUniktOrgNr", "Alle aktiviteter må ha samme orgnr"));
            }
            if (!Objects.equals(fraværPeriode.getArbeidsforholdId(), unikArbeidsforholdId)) {
                feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "]", "ikkeUnikArbeidsforholdId", "Alle aktiviteter må ha samme orgnr"));
            }

            var aktiviteterFravær = fraværPeriode.getAktivitetFravær();
            if (aktiviteterFravær.size() != 1) {
                feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "]", "ikkeEntydigAktivitet", "Kan kun oppgi én aktivitet"));
            } else if (aktiviteterFravær.iterator().next() != AktivitetFravær.ARBEIDSTAKER) {
                feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "]", "ikkeGyldigAktivitet", "Aktivitet må være av type Arbeidstaker"));
            }
            feil.addAll(validerKorrigerIMIkkeFulltFravær(index, fraværPeriode));
            index++;
        }
        feil.addAll(validerIkkeLikeEllerOverlappendePerioder(fraværsperioderKorrigeringIm));
        return feil;
    }

    private List<Feil> validerIkkeLikeEllerOverlappendePerioder(List<FraværPeriode> fraværPerioder) {
        List<Feil> feil = new ArrayList<>();
        Set<Periode> perioder = new HashSet<>();
        for (FraværPeriode fraværPeriode : fraværPerioder) {
            if (perioder.contains(fraværPeriode.getPeriode())) {
                feil.add(new Feil("fraværsperioderKorrigeringIm.perioder[" + fraværPeriode.getPeriode().getIso8601() + "]", "likePerioder", "To identiske perioder oppgitt"));
            } else {
                perioder.add(fraværPeriode.getPeriode());
            }
        }
        if (feil.stream().noneMatch(it -> it.getFeilkode().equals("likePerioder"))) {
            feil.addAll(validerOverlappendePerioderKorrigerIm(fraværPerioder));
        }
        return feil;
    }

    private List<Feil> validerOverlappendePerioderPrAktivitet(List<FraværPeriode> fraværPerioder) {
        List<Feil> feil = new ArrayList<>();
        Map<Aktivitet, List<LocalDateSegment<Integer>>> perioderPrAktivitet = new HashMap<>();
        Map<Aktivitet, Set<Periode>> unikePerioderPrAktivitet = new HashMap<>();
        int index = 0;
        for (FraværPeriode fraværPeriode : fraværPerioder) {
            if (erPeriodeUgyldig(fraværPeriode)){
                //avbryter her for å unngå NPE i koden under når periode ikke er satt, eller ikke er satt ordentlig
                //nødvendig siden validatoren brukes mens søknaden bygges i k9-punsj, og søknad vil således være ukomplett underveis
                //periode valideres andre steder uansett
                continue;
            }
            for (AktivitetFravær aktivitetType : fraværPeriode.getAktivitetFravær()) {
                Aktivitet aktivitet = new Aktivitet(aktivitetType, fraværPeriode.getArbeidsgiverOrgNr());
                Set<Periode> unikePerioder = unikePerioderPrAktivitet.computeIfAbsent(aktivitet, k -> new HashSet<>());
                if (unikePerioder.contains(fraværPeriode.getPeriode())) {
                    feil.add(new Feil("fraværsperioder[" + fraværPeriode.getPeriode().getIso8601() + "]", "likePerioder", "To identiske perioder oppgitt for aktivitettype " + (aktivitetType != null ? aktivitetType.getÅrsak() : null)));
                } else {
                    unikePerioder.add(fraværPeriode.getPeriode());
                    perioderPrAktivitet
                            .computeIfAbsent(aktivitet, k -> new ArrayList<>())
                            .add(new LocalDateSegment<>(fraværPeriode.getPeriode().getFraOgMed(), fraværPeriode.getPeriode().getTilOgMed(), index));
                }
            }
            index++;
        }
        for (var entry : perioderPrAktivitet.entrySet()) {
            LocalDateTimeline<List<Integer>> overlappendeFraværsperioder = LocalDateTimeline.buildGroupOverlappingSegments(entry.getValue())
                    .filterValue(v -> v.size() > 1);

            overlappendeFraværsperioder.stream()
                    .forEach(overlapp -> feil.add(new Feil(
                            "fraværsperioder" + overlapp.getValue(),
                            "overlappendePerioder",
                            "Overlappende periode " + new Periode(overlapp.getFom(), overlapp.getTom()).getIso8601() + " for aktivitetstype: " + (entry.getKey().aktivitetType != null ? entry.getKey().aktivitetType.getÅrsak() : null)
                    )));
        }
        return feil;
    }

    private static boolean erPeriodeUgyldig(FraværPeriode fraværPeriode){
        Periode periode = fraværPeriode.getPeriode();
        return periode == null || periode.getFraOgMed() == null || periode.getTilOgMed() == null || periode.getTilOgMed().isBefore(periode.getFraOgMed());
    }


    private List<Feil> validerOverlappendePerioderKorrigerIm(List<FraværPeriode> fraværPerioder) {
        List<Feil> feil = new ArrayList<>();
        List<LocalDateSegment<Integer>> perioder = new ArrayList<>();
        int index = 0;
        for (FraværPeriode fraværPeriode : fraværPerioder) {
            perioder.add(new LocalDateSegment<>(fraværPeriode.getPeriode().getFraOgMed(), fraværPeriode.getPeriode().getTilOgMed(), index));
            index++;
        }

        LocalDateTimeline<List<Integer>> overlappendePerioder = LocalDateTimeline.buildGroupOverlappingSegments(perioder)
                .filterValue(v -> v.size() > 1);

        overlappendePerioder.stream()
                .forEach(overlapp -> feil.add(new Feil(
                        "fraværsperioderKorrigeringIm.perioder" + overlapp.getValue(),
                        "overlappendePerioder",
                        "Overlappende periode " + new Periode(overlapp.getFom(), overlapp.getTom()).getIso8601()
                )));
        return feil;
    }

    private record Aktivitet(AktivitetFravær aktivitetType, Organisasjonsnummer arbeidsgiverOrgNr) {
    }

    private List<Feil> validerKorrigerIMIkkeFulltFravær(int index, FraværPeriode fraværPeriode) {
        if (Versjon.of("1.0.0").equals(versjon)) {
            return validerKorrigerIMIkkeFulltFraværV1_0_0(index, fraværPeriode);
        } else {
            return validerKorrigerIMIkkeFulltFraværV1_1_0(index, fraværPeriode);
        }
    }

    private List<Feil> validerKorrigerIMIkkeFulltFraværV1_0_0(int index, FraværPeriode fraværPeriode) {
        List<Feil> feil = new ArrayList<>();
        if (fraværPeriode.getDelvisFravær() != null) {
            feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "]", "delvisFravær", "feltet delvisFravær er ikke støttet i versjon " + versjon.getVerdi()));
        }
        if (Duration.ZERO.equals(fraværPeriode.getDuration()) && !fraværPeriode.getPeriode().getFraOgMed().equals(fraværPeriode.getPeriode().getTilOgMed())) {
            feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "]", "nullingPeriodeOversteget", "Nulling av periode kan ikke ha lenger varighet enn én dag"));
        }
        if (fraværPeriode.getDuration() != null && fraværPeriode.getDuration().compareTo(Duration.parse("PT7H30M")) > 0) {
            feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "]", "varighetOversteget", "Delvis fravær kan ikke overstige 7 timer og 30 min"));
        }
        return feil;
    }

    private List<Feil> validerKorrigerIMIkkeFulltFraværV1_1_0(int index, FraværPeriode fraværPeriode) {
        List<Feil> feil = new ArrayList<>();
        if (fraværPeriode.getDelvisFravær() != null) {
            //det er ikke påkrevet å bruke delvisFravær-feltet for korrigere IM siden det ikke brukes i IM.
            //tillater bruk og validerer konsistens
            if (fraværPeriode.getDelvisFravær().getFravær() == null) {
                feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "].delvisFravær.fravær", "manglerFraværForDelvisFravær", "Må oppgi fravær ved delvis fravær"));
            }
            if (fraværPeriode.getDelvisFravær().getNormalarbeidstid() == null) {
                feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "].delvisFravær.normalarbeidstid", "manglerNormalarbeidstidForDelvisFravær", "Må oppgi normalarbeidstid ved delvis fravær"));
            }
            if (fraværPeriode.getDelvisFravær().getFravær() != null && fraværPeriode.getDelvisFravær().getNormalarbeidstid() != null) {
                if (fraværPeriode.getDelvisFravær().getNormalarbeidstid().isZero()) {
                    feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "].delvisFravær.normalarbeidstid", "normalarbeidstid0", "Normalarbeidstid kan ikke settes til 0"));
                } else if (!fraværPeriode.getDelvisFravær().normalisertTilStandarddag().equals(fraværPeriode.getDuration())) {
                    feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "].delvisFravær", "avvikDurationOgDelvisFravær", "Duration stemmer ikke med delvisFravær.fravær og delvisFravær.normalarbeidstid"));
                }
            }
        }
        if (Duration.ZERO.equals(fraværPeriode.getDuration()) && !fraværPeriode.getPeriode().getFraOgMed().equals(fraværPeriode.getPeriode().getTilOgMed())) {
            feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "]", "nullingPeriodeOversteget", "Nulling av periode kan ikke ha lenger varighet enn én dag"));
        }
        if (fraværPeriode.getDuration() != null && fraværPeriode.getDuration().compareTo(Duration.parse("PT7H30M")) > 0) {
            feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "]", "varighetOversteget", "Delvis fravær kan ikke overstige 7 timer og 30 min"));
        }
        return feil;
    }

    private List<Feil> validerFraværsperioderFraSøker(OmsorgspengerUtbetaling ytelse) {
        var fraværsperioder = ytelse.getFraværsperioder();
        if (fraværsperioder == null) {
            return List.of();
        }

        var index = 0;
        List<Feil> feil = new ArrayList<>();
        for (FraværPeriode fraværPeriode : fraværsperioder) {
            if (fraværPeriode.getAktivitetFravær() == null || fraværPeriode.getAktivitetFravær().isEmpty()) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "påkrevd", "Aktivitet må være satt"));
            }
            if (fraværPeriode.getÅrsak() == null) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "påkrevd", "Fraværsårsak må være satt"));
            }
            if (fraværPeriode.getSøknadÅrsak() == null && fraværPeriode.getAktivitetFravær().contains(AktivitetFravær.ARBEIDSTAKER)) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "påkrevd", "Søknadårsak må være satt for arbeidstaker"));
            }
            feil.addAll(validerSøknadIkkeFulltFravær(index, fraværPeriode));
            index++;
        }
        if (!Versjon.of("1.0.0").equals(versjon)) {
            feil.addAll(validerOverlappendePerioderPrAktivitet(fraværsperioder));
        }
        return feil;
    }

    private List<Feil> validerSøknadIkkeFulltFravær(int index, FraværPeriode fraværPeriode) {
        if (versjon == null || Versjon.of("1.0.0").equals(versjon)) {
            return validerSøknadIkkeFulltFraværV1_0_0(index, fraværPeriode);
        } else {
            return validerSøknadIkkeFulltFraværV1_1_0(index, fraværPeriode);
        }
    }

    private List<Feil> validerSøknadIkkeFulltFraværV1_0_0(int index, FraværPeriode fraværPeriode) {
        List<Feil> feil = new ArrayList<>();
        if (fraværPeriode.getDelvisFravær() != null) {
            feil.add(new Feil("fraværsperioder[" + index + "]", "delvisFravær", "feltet delvisFravær er ikke støttet i versjon " + (versjon != null ? versjon.getVerdi() : null)));
        }
        return feil;
    }

    private List<Feil> validerSøknadIkkeFulltFraværV1_1_0(int index, FraværPeriode fraværPeriode) {
        List<Feil> feil = new ArrayList<>();
        if (fraværPeriode.getDuration() != null && !fraværPeriode.getDuration().isZero() && fraværPeriode.getDelvisFravær() == null) {
            feil.add(new Feil("fraværsperioder[" + index + "].delvisFravær", "manglerDelvisFravær", "feltet delvisFravær er påkrevet i versjon " + (versjon != null ? versjon.getVerdi() : null) + " når duration er satt til noe som ikke er 0"));
        }
        if (fraværPeriode.getDelvisFravær() != null) {
            if (fraværPeriode.getDelvisFravær().getFravær() == null) {
                feil.add(new Feil("fraværsperioder[" + index + "].delvisFravær.fravær", "manglerFraværForDelvisFravær", "Må oppgi fravær ved delvis fravær"));
            }
            if (fraværPeriode.getDelvisFravær().getNormalarbeidstid() == null) {
                feil.add(new Feil("fraværsperioder[" + index + "].delvisFravær.normalarbeidstid", "manglerNormalarbeidstidForDelvisFravær", "Må oppgi normalarbeidstid ved delvis fravær"));
            }
            if (fraværPeriode.getDelvisFravær().getFravær() != null && fraværPeriode.getDelvisFravær().getNormalarbeidstid() != null) {
                if (fraværPeriode.getDelvisFravær().getNormalarbeidstid().isZero()) {
                    feil.add(new Feil("fraværsperioder[" + index + "].delvisFravær.normalarbeidstid", "normalarbeidstid0", "Normalarbeidstid kan ikke settes til 0"));
                } else if (!fraværPeriode.getDelvisFravær().normalisertTilStandarddag().equals(fraværPeriode.getDuration())) {
                    feil.add(new Feil("fraværsperioder[" + index + "]", "avvikDurationOgDelvisFravær", "Duration stemmer ikke med delvisFravær.fravær og delvisFravær.normalarbeidstid"));
                }
            }
        }
        if (fraværPeriode.getDuration() != null && fraværPeriode.getDuration().compareTo(Duration.parse("PT7H30M")) > 0) {
            feil.add(new Feil("fraværsperioder[" + index + "].duration", "varighetOversteget", "Delvis fravær kan ikke overstige 7 timer og 30 min"));
        }

        return feil;
    }

    private List<Feil> validerFraværsperiodeKilder(OmsorgspengerUtbetaling ytelse) {
        List<Feil> feil = new ArrayList<>();

        var fraværsperioder = Optional.ofNullable(ytelse.getFraværsperioder()).orElse(List.of());
        var fraværsperioderKorrigeringIm = Optional.ofNullable(ytelse.getFraværsperioderKorrigeringIm()).orElse(List.of());

        if (fraværsperioder.isEmpty() && fraværsperioderKorrigeringIm.isEmpty()) {
            feil.add(new Feil("fraværsperioder", "ingenPerioder", "Må oppgi minst én fraværsperiode"));
        } else if (!fraværsperioder.isEmpty() && !fraværsperioderKorrigeringIm.isEmpty()) {
            feil.add(new Feil("fraværsperioder", "ugyldigKombinasjon", "Kan ikke korrigere både søknad og inntektsmelding i samme dokument"));
        }
        return feil;
    }
}
