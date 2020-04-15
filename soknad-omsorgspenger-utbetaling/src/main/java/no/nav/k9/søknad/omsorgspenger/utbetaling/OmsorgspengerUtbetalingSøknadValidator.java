package no.nav.k9.søknad.omsorgspenger.utbetaling;

import no.nav.k9.søknad.PeriodeValidator;
import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class OmsorgspengerUtbetalingSøknadValidator extends SøknadValidator<OmsorgspengerUtbetalingSøknad> {
    private final PeriodeValidator periodeValidator;

    public OmsorgspengerUtbetalingSøknadValidator() {
        this.periodeValidator = new PeriodeValidator();
    }

    @Override
    public List<Feil> valider(OmsorgspengerUtbetalingSøknad søknad) {
        List<Feil> feil = new ArrayList<>();

        validerSøknadId(søknad.søknadId, feil);
        validerVersjon(søknad.versjon, feil);
        validerMottattDato(søknad.mottattDato, feil);
        validerSøker(søknad.søker, feil);
        validerBarn(søknad.barn, feil);
        validerSelvstendingNæringsdrivende(søknad.selvstendingNæringsdrivende, feil);

        return feil;
    }

    private static void validerSøknadId(SøknadId søknadId, List<Feil> feil) {
        if (søknadId == null) {
            feil.add(new Feil("søknadId", PÅKREVD, "ID må settes i søknaden."));
        }
    }

    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon == null) {
            feil.add(new Feil("versjon", PÅKREVD, "Versjon må settes i søknaden."));
        } else if (!versjon.erGyldig()) {
            feil.add(new Feil("versjon", "ugyldigVersjon", "Versjonen er på ugyldig format."));
        }
    }

    private static void validerMottattDato(ZonedDateTime mottatDato, List<Feil> feil) {
        if (mottatDato == null) {
            feil.add(new Feil("mottattDato", PÅKREVD, "Mottatt dato må settes i søknaden."));
        }
    }

    private static void validerSøker(Søker søker, List<Feil> feil) {
        if (søker == null) {
            feil.add(new Feil("søker", PÅKREVD, "Søker må settes i søknaden."));
        } else if (søker.norskIdentitetsnummer == null) {
            feil.add(new Feil("søker.norskIdentitetsnummer", PÅKREVD, "Søkers Personnummer/D-nummer må settes i søknaden."));
        }
    }

    private static void validerBarn(List<Barn> barn, List<Feil> feil) {
        if (barn == null || barn.isEmpty()) return;
        var index = 0;
        for (Barn b : barn) {
            if (b.norskIdentitetsnummer == null && b.fødselsdato == null) {
                feil.add(new Feil("barn[" + index + "]", "norskIdentitetsnummerEllerFødselsdatoPåkrevd", "Må sette enten Personnummer/D-nummer på barn, eller fødselsdato."));
            } else if (b.norskIdentitetsnummer != null && b.fødselsdato != null) {
                feil.add(new Feil("barn[" + index + "]", "ikkeEntydigIdPåBarnet", "Må sette enten Personnummer/D-nummer på barn, eller fødselsdato."));
            }
            index++;
        }
    }

    private void validerSelvstendingNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigeVirksomheter, List<Feil> feil) {
        if (selvstendigeVirksomheter == null || selvstendigeVirksomheter.isEmpty()) return;
        var index = 0;
        for (SelvstendigNæringsdrivende sn : selvstendigeVirksomheter) {
            feil.addAll(
                    this.periodeValidator.validerTillattOverlappOgÅpnePerioder(sn.perioder, "selvstendigNæringsdrivende[" + index++ + "].perioder")
            );

            sn.perioder.forEach((periode, snInfo) -> {
                String periodeString = periode.fraOgMed + "-" + periode.tilOgMed;
                String felt = "selvstendigNæringsdrivende.perioder{" + periodeString + "}";
                if (snInfo.erVarigEndring != null) {
                    if (snInfo.endringDato == null) {
                        feil.add(new Feil(felt + ".endringsDato", PÅKREVD, "endringDato må være satt dersom erVarigEndring er true."));
                    } else if (snInfo.endringBegrunnelse == null || snInfo.endringBegrunnelse.isBlank()) {
                        feil.add(new Feil(felt + ".endringBegrunnelse", PÅKREVD, "endringBegrunnelse må være satt dersom erVarigEndring er true."));
                    }
                }

               /* if (snInfo.bruttoInntekt == null) {
                    feil.add(new Feil(felt + ".bruttoIntekt", PÅKREVD, "bruttoInntekt er påkrevd, og må være satt."))
                } TODO: Burde være påkrevd? Er ikke påkrevd i brukerdialog...*/
            });
        }
    }
}
