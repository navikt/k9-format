package no.nav.k9.søknad.ytelse.omsorgspenger.v1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import no.nav.k9.søknad.PeriodeValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.fravær.AktivitetFravær;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.opptjening.Frilanser;
import no.nav.k9.søknad.felles.opptjening.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

public class OmsorgspengerUtbetalingValidator extends YtelseValidator {
    private final PeriodeValidator periodeValidator;

    public OmsorgspengerUtbetalingValidator() {
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
        List<Feil> feil = new ArrayList<>();
        var søknadsperiode = ytelse.getSøknadsperiode();
        var yearMin = søknadsperiode.getFraOgMed().getYear();
        var yearMax = søknadsperiode.getTilOgMed().getYear();

        if (yearMin != yearMax) {
            feil.add(new Feil("fraværsperioder", "perioderOverFlereÅr", "Perioder kan ikke overstige ett år"));
        }
        return feil;
    }

    private List<Feil> validerAktivitet(OmsorgspengerUtbetaling ytelse) {
        List<Feil> feil = new ArrayList<>();
        var aktivitet = ytelse.getAktivitet();

        if (aktivitet != null) {
            if ((aktivitet.getFrilanser() != null)
                    || ((aktivitet.getSelvstendigNæringsdrivende() != null) && !aktivitet.getSelvstendigNæringsdrivende().isEmpty())) {
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

            if (Duration.ZERO.equals(fraværPeriode.getDuration()) && !fraværPeriode.getPeriode().getFraOgMed().equals(fraværPeriode.getPeriode().getTilOgMed())) {
                feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "]", "nullingPeriodeOversteget", "Nulling av periode kan ikke ha lenger varighet enn én dag"));
            }

            if (fraværPeriode.getDuration() != null && fraværPeriode.getDuration().compareTo(Duration.parse("PT7H30M")) == 1) {
                feil.add(new Feil("fraværsperioderKorrigeringIm[" + index + "]", "varighetOversteget", "Delvis fravær kan ikke overstige 7 timer og 30 min"));
            }

            index++;
        }

        // Valider perioder på tvers
        var perioder = fraværsperioderKorrigeringIm.stream().collect(Collectors.toMap(e -> e.getPeriode(), e -> e.getArbeidsgiverOrgNr()));
        feil.addAll(periodeValidator.validerIkkeTillattOverlapp(perioder, "fraværsperioderKorrigeringIm.perioder"));

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
            if (fraværPeriode.getSøknadÅrsak() == null) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "påkrevd", "Søknadårsak må være satt"));
            }
            index++;
        }
        return feil;
    }

    private List<Feil> validerFraværsperiodeKilder(OmsorgspengerUtbetaling ytelse) {
        List<Feil> feil = new ArrayList<>();

        var fraværsperioder = Optional.ofNullable(ytelse.getFraværsperioder()).orElse(List.of());
        var fraværsperioderKorrigeringIm = Optional.ofNullable(ytelse.getFraværsperioderKorrigeringIm()).orElse(List.of());
        ;

        if (fraværsperioder.isEmpty() && fraværsperioderKorrigeringIm.isEmpty()) {
            feil.add(new Feil("fraværsperioder", "ingenPerioder", "Må oppgi minst én fraværsperiode"));
        } else if (!fraværsperioder.isEmpty() && !fraværsperioderKorrigeringIm.isEmpty()) {
            feil.add(new Feil("fraværsperioder", "ugyldigKombinasjon", "Kan ikke korrigere både søknad og inntektsmelding i samme dokument"));
        }
        return feil;
    }
}
