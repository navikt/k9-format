package no.nav.k9.søknad.omsorgspenger.utbetaling.snf;

import no.nav.k9.søknad.PeriodeValidator;
import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.*;
import no.nav.k9.søknad.felles.opptjening.snf.Frilanser;
import no.nav.k9.søknad.felles.opptjening.snf.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("removal")
public class OmsorgspengerUtbetalingSøknadValidator extends SøknadValidator<OmsorgspengerUtbetalingSøknad> {
    private final PeriodeValidator periodeValidator;
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();


    public OmsorgspengerUtbetalingSøknadValidator() {
        this.periodeValidator = new PeriodeValidator();
    }

    @Override
    public List<Feil> valider(OmsorgspengerUtbetalingSøknad søknad) {
        List<Feil> feil = validerSøknad(søknad);
        validerVersjon(søknad.versjon, feil);
        validerSøker(søknad.søker, feil);
        validerFosterbarn(søknad.fosterbarn, feil);
        validerFrilanserOgSelvstendingNæringsdrivende(søknad.selvstendigNæringsdrivende, søknad.frilanser, feil);
        validerSelvstendingNæringsdrivende(søknad.selvstendigNæringsdrivende, feil);
        return feil;
    }

    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon == null || !versjon.erGyldig()) {
            feil.add(new Feil("versjon", "ugyldigVersjon", "Versjonen er på ugyldig format."));
        }
    }

    private static void validerSøker(Søker søker, List<Feil> feil) {
        if (søker != null && søker.norskIdentitetsnummer == null) {
            feil.add(new Feil("søker.norskIdentitetsnummer", PÅKREVD, "Søkers Personnummer/D-nummer må settes i søknaden."));
        }
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
            feil.addAll(
                    this.periodeValidator.validerTillattOverlappOgÅpnePerioder(sn.perioder, snFelt + ".perioder")
            );

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

    private List<Feil> validerSøknad(OmsorgspengerUtbetalingSøknad søknad) {
        var constraints = VALIDATOR_FACTORY.getValidator().validate(søknad);
        if (constraints != null && !constraints.isEmpty()) {
            return constraints.stream()
                    .map((v) -> new Feil(v.getPropertyPath().toString(), PÅKREVD, v.getMessage()))
                    .collect(Collectors.toList());
        } else return new ArrayList<>();
    }
}
