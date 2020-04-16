package no.nav.k9.søknad.omsorgspenger.utbetaling;

import no.nav.k9.søknad.PeriodeValidator;
import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.*;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        validerBarn(søknad.barn, feil);
        validerFrilanserOgSelvstendingNæringsdrivende(søknad.selvstendingNæringsdrivende, søknad.frilanser, feil);
        validerSelvstendingNæringsdrivende(søknad.selvstendingNæringsdrivende, feil);

        return feil;
    }

    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon == null && !versjon.erGyldig()) {
            feil.add(new Feil("versjon", "ugyldigVersjon", "Versjonen er på ugyldig format."));
        }
    }

    private static void validerSøker(Søker søker, List<Feil> feil) {
        if (søker != null && søker.norskIdentitetsnummer == null) {
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

    private void validerFrilanserOgSelvstendingNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigeVirksomheter, Frilanser frilanser, List<Feil> feil) {
        if (frilanser == null && (selvstendigeVirksomheter == null || selvstendigeVirksomheter.isEmpty())) {
            feil.add(new Feil("frilanser & selvstendingNæringsdrivene", PÅKREVD, "Enten frilanser eller selvstendingNæringsdrivende må være satt i søknaden."));
        }
    }

    private void validerSelvstendingNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendigeVirksomheter, List<Feil> feil) {
        if (selvstendigeVirksomheter == null || selvstendigeVirksomheter.isEmpty()) return;
        var index = 0;
        for (SelvstendigNæringsdrivende sn : selvstendigeVirksomheter) {
            feil.addAll(
                    this.periodeValidator.validerTillattOverlappOgÅpnePerioder(sn.perioder, "selvstendigNæringsdrivende[" + index + "].perioder")
            );

            sn.perioder.forEach((periode, snInfo) -> {
                String periodeString = periode.fraOgMed + "-" + periode.tilOgMed;
                String felt = "selvstendigNæringsdrivende.perioder{" + periodeString + "}";

                if (snInfo.erVarigEndring != null) {
                    if (snInfo.endringDato == null) {
                        feil.add(new Feil(felt + ".endringsDato", PÅKREVD, "endringDato må være satt dersom erVarigEndring er true."));
                    }
                    if (snInfo.endringBegrunnelse == null || snInfo.endringBegrunnelse.isBlank()) {
                        feil.add(new Feil(felt + ".endringBegrunnelse", PÅKREVD, "endringBegrunnelse må være satt dersom erVarigEndring er true."));
                    }
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
