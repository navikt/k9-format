package no.nav.k9.søknad.ytelse.omsorgspenger.v1;

import java.util.ArrayList;
import java.util.List;

import no.nav.k9.søknad.PeriodeValidator;
import no.nav.k9.søknad.Validator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.aktivitet.Frilanser;
import no.nav.k9.søknad.felles.aktivitet.SelvstendigNæringsdrivende;
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
        getAktivitet(omsorgspengerUtbetaling, feil);
        feil.addAll(Validator.validerTilFeil(ytelse));
        return feil;
    }

    private void getAktivitet(OmsorgspengerUtbetaling søknad, List<Feil> feil) {
        var aktivitet = søknad.getAktivitet();

        if (aktivitet != null) {

            if (aktivitet.getFrilanser() != null
                || (aktivitet.getSelvstendigNæringsdrivende() != null && !aktivitet.getSelvstendigNæringsdrivende().isEmpty())) {
                validerFrilanserOgSelvstendingNæringsdrivende(aktivitet.getSelvstendigNæringsdrivende(), aktivitet.getFrilanser(), feil);
                validerSelvstendingNæringsdrivende(aktivitet.getSelvstendigNæringsdrivende(), feil);
            }
        }
    }

    private void validerFrilanserOgSelvstendingNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigeVirksomheter, Frilanser frilanser,
                                                               List<Feil> feil) {
        if (frilanser == null && (selvstendigeVirksomheter == null || selvstendigeVirksomheter.isEmpty())) {
            feil.add(new Feil("frilanser & selvstendingNæringsdrivene", PÅKREVD, "Enten frilanser eller selvstendingNæringsdrivende må være satt i søknaden."));
        }
    }

    private void validerSelvstendingNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigeVirksomheter, List<Feil> feil) {
        if (selvstendigeVirksomheter == null || selvstendigeVirksomheter.isEmpty())
            return;
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
                        feil.add(new Feil(snInfoFelt + ".landkode", PÅKREVD,
                            "landkode må være satt, og kan ikke være null, dersom virksomhet er registrert i utlandet."));
                    }
                } else if (sn.organisasjonsnummer == null) {
                    feil.add(new Feil(snInfoFelt + ".registrertIUtlandet og " + snFelt + ".organisasjonsnummer", PÅKREVD,
                        "Dersom virksomheten er registrert i norge, må organisasjonsnummeret være satt."));
                }
            });
            index++;
        }
    }
}
