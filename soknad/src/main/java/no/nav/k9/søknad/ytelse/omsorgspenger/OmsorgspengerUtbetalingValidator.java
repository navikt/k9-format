package no.nav.k9.søknad.ytelse.omsorgspenger;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import no.nav.k9.søknad.PeriodeValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.opptjening.arbeidstaker.Arbeidstaker;
import no.nav.k9.søknad.felles.opptjening.snf.Frilanser;
import no.nav.k9.søknad.felles.opptjening.snf.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

public class OmsorgspengerUtbetalingValidator extends YtelseValidator {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private final PeriodeValidator periodeValidator;

    public OmsorgspengerUtbetalingValidator() {
        this.periodeValidator = new PeriodeValidator();
    }

    private static void validerFosterbarn(List<Barn> barn, List<Feil> feil) {
        if (barn == null || barn.isEmpty()) return;
        var index = 0;
        for (Barn b : barn) {
            if (b.norskIdentitetsnummer == null && b.fødselsdato == null) {
                feil.add(new Feil("fosterbarn[" + index + "]", "norskIdentitetsnummerEllerFødselsdatoPåkrevd", "Må sette enten Personnummer/D-nummer på fosterbarn, eller fødselsdato."));
            } else if (b.norskIdentitetsnummer != null && b.fødselsdato != null) {
                feil.add(new Feil("fosterbarn[" + index + "]", "ikkeEntydigIdPåBarnet", "Må sette enten Personnummer/D-nummer på fosterbarn, eller fødselsdato."));
            }
            index++;
        }
    }

    @Override
    public List<Feil> valider(Ytelse ytelse) {
        var omsorgspengerUtbetaling = (OmsorgspengerUtbetaling) ytelse;

        List<Feil> feil = new ArrayList<>();
        validerFosterbarn(omsorgspengerUtbetaling.getFosterbarn(), feil);
        validerAktivitet(omsorgspengerUtbetaling, feil);
        return feil;
    }

    private void validerAktivitet(OmsorgspengerUtbetaling søknad, List<Feil> feil) {
        var aktivitet = søknad.getOpptjening();

        if (aktivitet != null) {

            if (aktivitet.getFrilanser() != null
                    || (aktivitet.getSelvstendigNæringsdrivende() != null && !aktivitet.getSelvstendigNæringsdrivende().isEmpty())) {
                validerFrilanserOgSelvstendingNæringsdrivende(aktivitet.getSelvstendigNæringsdrivende(), aktivitet.getFrilanser(), feil);
                validerSelvstendingNæringsdrivende(aktivitet.getSelvstendigNæringsdrivende(), feil);
            }
        }
    }

    private void validerFrilanserOgSelvstendingNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigeVirksomheter, Frilanser frilanser, List<Feil> feil) {
        if (frilanser == null && (selvstendigeVirksomheter == null || selvstendigeVirksomheter.isEmpty())) {
            feil.add(new Feil("frilanser & selvstendingNæringsdrivene", PÅKREVD, "Enten frilanser eller selvstendingNæringsdrivende må være satt i søknaden."));
        }
    }

    private void validerSelvstendingNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigeVirksomheter, List<Feil> feil) {
        if (selvstendigeVirksomheter == null || selvstendigeVirksomheter.isEmpty()) return;
        var index = 0;
        for (SelvstendigNæringsdrivende sn : selvstendigeVirksomheter) {
            String snFelt = "selvstendigNæringsdrivende[" + index + "]";
            feil.addAll(this.periodeValidator.validerTillattOverlappOgÅpnePerioder(sn.perioder, snFelt + ".perioder"));

            sn.perioder.forEach((periode, snInfo) -> {
                String periodeString = periode.fraOgMed + "-" + periode.tilOgMed;
                String snInfoFelt = "selvstendigNæringsdrivende.perioder{" + periodeString + "}";

                if (snInfo.erVarigEndring != null && snInfo.erVarigEndring) {
                    if (snInfo.endringDato == null) {
                        feil.add(new Feil(snInfoFelt + ".endringsDato", PÅKREVD, "endringDato må være satt dersom erVarigEndring er true."));
                    }
                    if (snInfo.endringBegrunnelse == null || snInfo.endringBegrunnelse.isBlank()) {
                        feil.add(new Feil(snInfoFelt + ".endringBegrunnelse", PÅKREVD, "endringBegrunnelse må være satt dersom erVarigEndring er true."));
                    }
                }

                if (snInfo.registrertIUtlandet != null) {
                    if (snInfo.registrertIUtlandet && snInfo.landkode == null) {
                        feil.add(new Feil(snInfoFelt + ".landkode", PÅKREVD, "landkode må være satt, og kan ikke være null, dersom virksomhet er registrert i utlandet."));
                    }
                } else if (sn.organisasjonsnummer == null) {
                    feil.add(new Feil(snInfoFelt + ".registrertIUtlandet og " + snFelt + ".organisasjonsnummer", PÅKREVD, "Dersom virksomheten er registrert i norge, må organisasjonsnummeret være satt."));
                }
            });
            index++;
        }
    }
}
