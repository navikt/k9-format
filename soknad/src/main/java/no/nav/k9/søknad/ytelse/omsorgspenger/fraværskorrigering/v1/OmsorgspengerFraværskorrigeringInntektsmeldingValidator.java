package no.nav.k9.søknad.ytelse.omsorgspenger.fraværskorrigering.v1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import no.nav.k9.søknad.PeriodeValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.fravær.AktivitetFravær;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

public class OmsorgspengerFraværskorrigeringInntektsmeldingValidator extends YtelseValidator {
    private final PeriodeValidator periodeValidator;

    public OmsorgspengerFraværskorrigeringInntektsmeldingValidator() {
        this.periodeValidator = new PeriodeValidator();
    }

    @Override
    public List<Feil> valider(Ytelse ytelse) {
        var fraværskorrigering = (OmsorgspengerFraværskorrigeringInntektsmelding) ytelse;

        List<Feil> feil = new ArrayList<>();
        feil.addAll(validerPeriodeOverlapp(fraværskorrigering));
        feil.addAll(validerFraværsperioder(fraværskorrigering));
        return feil;
    }

    private List<Feil> validerPeriodeOverlapp(OmsorgspengerFraværskorrigeringInntektsmelding dokument) {
        var perioder = dokument.getFraværsperioder().stream().collect(Collectors.toMap(e -> e.getPeriode(), e -> e.getArbeidsgiverOrgNr()));
        return periodeValidator.validerIkkeTillattOverlapp(perioder, "fraværsperioder.perioder");
    }

    private List<Feil> validerFraværsperioder(OmsorgspengerFraværskorrigeringInntektsmelding ytelse) {
        var fraværsperioder = ytelse.getFraværsperioder();
        var index = 0;
        Organisasjonsnummer uniktOrgnr = null;
        UUID unikArbeidsforholdId = null;
        List<Feil> feil = new ArrayList<>();

        for (FraværPeriode fraværPeriode : fraværsperioder) {
            if (index == 0) {
                // Initier verdier for arbeidsforhold som skal være konsistente for alle fraværsperioder
                uniktOrgnr = fraværPeriode.getArbeidsgiverOrgNr();
                unikArbeidsforholdId = fraværPeriode.getArbeidsforholdId();
            }
            if (fraværPeriode.getArbeidsgiverOrgNr() == null) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "ikkeSpesifisertOrgNr", "Må oppgi orgnr for aktivitet"));
            } else if (!Objects.equals(fraværPeriode.getArbeidsgiverOrgNr(), uniktOrgnr)) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "ikkeUniktOrgNr", "Alle aktiviteter må ha samme orgnr"));
            }
            if (!Objects.equals(fraværPeriode.getArbeidsforholdId(), unikArbeidsforholdId)) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "ikkeUnikArbeidsforholdId", "Alle aktiviteter må ha samme orgnr"));
            }

            var aktiviteterFravær = fraværPeriode.getAktivitetFravær();
            if (aktiviteterFravær.size() != 1) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "ikkeEntydigAktivitet", "Kan kun oppgi én aktivitet"));
            } else if (aktiviteterFravær.iterator().next() != AktivitetFravær.ARBEIDSTAKER) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "ikkeGyldigAktivitet", "Aktivitet må være av type Arbeidstaker"));
            }

            if (Duration.ZERO.equals(fraværPeriode.getDuration()) && !fraværPeriode.getPeriode().getFraOgMed().equals(fraværPeriode.getPeriode().getTilOgMed())) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "nullingPeriodeOversteget", "Nulling av periode kan ikke ha lenger varighet enn én dag"));
            }

            if (fraværPeriode.getDuration() != null && fraværPeriode.getDuration().compareTo(Duration.parse("PT7H30M")) == 1) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "varighetOversteget", "Delvis fravær kan ikke overstige 7 timer og 30 min"));
            }

            index++;
        }
        return feil;
    }
}
