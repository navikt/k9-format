package no.nav.k9.søknad.ytelse.omsorgspenger.fraværskorrigering.v1;

import java.util.ArrayList;
import java.util.List;
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
        List<Feil> feil = new ArrayList<>();

        for (FraværPeriode fraværPeriode : fraværsperioder) {
            Organisasjonsnummer orgNr = fraværPeriode.getArbeidsgiverOrgNr();
            if (orgNr == null) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "ikkeSpesifisertOrgNr", "Må oppgi orgnr for aktivitet"));
            } else {
                if (uniktOrgnr == null) {
                    uniktOrgnr = orgNr;
                } else if(!uniktOrgnr.equals(orgNr)) {
                    feil.add(new Feil("fraværsperioder[" + index + "]", "ikkeUniktOrgNr", "Alle aktiviteter må ha samme orgnr"));
                }
            }

            var aktiviteterFravær = fraværPeriode.getAktivitetFravær();
            if (aktiviteterFravær.size() != 1) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "ikkeEntydigAktivitet", "Kan kun oppgi én aktivitet"));
            } else if (aktiviteterFravær.iterator().next() != AktivitetFravær.ARBEIDSTAKER) {
                feil.add(new Feil("fraværsperioder[" + index + "]", "ikkeGyldigAktivitet", "Aktivitet må være av type Arbeidstaker"));
            }

            index++;
        }
        return feil;
    }
}
