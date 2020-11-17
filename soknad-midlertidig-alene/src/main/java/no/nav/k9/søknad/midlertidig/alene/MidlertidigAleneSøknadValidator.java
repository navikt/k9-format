package no.nav.k9.søknad.midlertidig.alene;

import no.nav.k9.søknad.SøknadValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.SøknadId;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class MidlertidigAleneSøknadValidator extends SøknadValidator<MidlertidigAleneSøknad> {

    @Override
    public List<Feil> valider(MidlertidigAleneSøknad søknad) {
        List<Feil> feil = new ArrayList<>();

        validerSøknadId(søknad.søknadId, feil);
        validerId(søknad.id, feil);
        validerVersjon(søknad.versjon, feil);
        validerMottattDato(søknad.mottattDato, feil);
        validerSøker(søknad.søker, feil);
        validerAnnenForelder(søknad.annenForelder, feil);
        validerArbeidssituasjon(søknad.arbeidssituasjon, feil);
        validerAntallBarn(søknad.antallBarn, feil);
        validerFødselsårBarn(søknad.fødselsårBarn, feil);
        validerMedlemskap(søknad.medlemskap, feil);

        return feil;
    }

    private static void validerSøknadId(SøknadId søknadId, List<Feil> feil) {
        if (søknadId == null) {
            feil.add(new Feil("søknadId", PÅKREVD, "SøknadID må settes i søknaden."));
        }
    }

    private static void validerId(String id, List<Feil> feil) {
        if (id == null) {
            feil.add(new Feil("id", PÅKREVD, "ID må settes i søknaden."));
        }
    }

    private static void validerVersjon(Versjon versjon, List<Feil> feil) {
        if (versjon == null) {
            feil.add(new Feil("versjon", PÅKREVD, "Versjon må settes i søknaden."));
        } else if (!versjon.erGyldig()){
            feil.add(new Feil("versjon", "ugyldigVersjon", "Versjonen er på ugyldig format."));
        }
    }

    private static void validerMottattDato(ZonedDateTime mottatDato, List<Feil> feil) {
        if (mottatDato == null) {
            feil.add(new Feil("mottattDato", PÅKREVD, "Mottatt dato må settes i søknaden."));
        }
    }

    private static void validerAnnenForelder(AnnenForelder annenForelder, List<Feil> feil) {
        if (annenForelder == null) {
            feil.add(new Feil("annenForelder", PÅKREVD, "annenForelder må settes i søknaden."));
        } else if (annenForelder.norskIdentitetsnummer == null){
            feil.add(new Feil("annenForelder.norskIdentitetsnummer", PÅKREVD, "annenForelder Personnummer/D-nummer må settes i søknaden."));
        } else if (annenForelder.navn == null){
            feil.add(new Feil("annenForelder.navn", PÅKREVD, "annenForelder navn må settes i søknaden."));
        } else if (annenForelder.situasjon == null){
            feil.add(new Feil("annenForelder.situasjon", PÅKREVD, "annenForelder situasjon må settes i søknaden."));
        }
    }

    private static void validerSøker(Søker søker, List<Feil> feil) {
        if (søker == null) {
            feil.add(new Feil("søker", PÅKREVD, "Søker må settes i søknaden."));
        } else if (søker.norskIdentitetsnummer == null) {
            feil.add(new Feil("søker.norskIdentitetsnummer", PÅKREVD, "Søkers Personnummer/D-nummer må settes i søknaden."));
        }
    }

    private static void validerArbeidssituasjon(Arbeidssituasjon arbeidssituasjon, List<Feil> feil) {
        if (arbeidssituasjon == null) {
            feil.add(new Feil("arbeidssituasjon", PÅKREVD, "Arbeidssituasjon må settes i søknaden."));
        }
    }

    private static void validerAntallBarn(int antallBarn, List<Feil> feil) {
        if (antallBarn <= 0) {
            feil.add(new Feil("antallBarn", PÅKREVD, "antallBarn kan ikke være 0 eller mindre"));
        }
    }

    private static void validerFødselsårBarn(List<Integer> fødselsårBarn, List<Feil> feil) {
        if (fødselsårBarn == null) {
            feil.add(new Feil("fødselsårBarn", PÅKREVD, "fødselsårBarn må være satt"));
        } else if (fødselsårBarn.size() == 0) {
            feil.add(new Feil("fødselsårBarn", PÅKREVD, "fødselsårBarn må inneholde minst et element"));
        }
    }

    private static void validerMedlemskap(Medlemskap medlemskap, List<Feil> feil) {
        if (medlemskap == null) {
            feil.add(new Feil("medlemskap", PÅKREVD, "medlemskap må være satt i søknaden"));
        } else if (medlemskap.harBoddIUtlandetSiste12Mnd == null) {
            feil.add(new Feil("medlemskap", PÅKREVD, "medlemskap.harBoddIUtlandetSiste12Mnd må være satt i søknaden"));
        } else if (medlemskap.skalBoIUtlandetNeste12Mnd == null) {
            feil.add(new Feil("medlemskap", PÅKREVD, "medlemskap.skalBoIUtlandetNeste12Mnd må være satt i søknaden"));
        }

        if(medlemskap != null && medlemskap.utenlandsoppholdSiste12Mnd != null){
            if(!medlemskap.utenlandsoppholdSiste12Mnd.isEmpty()){
                validerUtenlandsopphold(medlemskap.utenlandsoppholdSiste12Mnd, feil);
            }
        }

        if(medlemskap != null && medlemskap.utenlandsoppholdNeste12Mnd != null){
            if(!medlemskap.utenlandsoppholdNeste12Mnd.isEmpty()){
                validerUtenlandsopphold(medlemskap.utenlandsoppholdNeste12Mnd, feil);
            }
        }
    }

    private static void validerUtenlandsopphold(List<Utenlandsopphold> utenlandsopphold, List<Feil> feil) {
        for(Utenlandsopphold opphold : utenlandsopphold){
            if(opphold.landnavn == null){
                feil.add(new Feil("landnavn", PÅKREVD, "landnavn må være satt i utenlandsopphold"));
            } else if(opphold.fraOgMed == null){
                feil.add(new Feil("fraOgMed", PÅKREVD, "fraOgMed må være satt i utenlandsopphold"));
            } else if(opphold.tilOgMed == null){
                feil.add(new Feil("tilOgMed", PÅKREVD, "tilOgMed må være satt i utenlandsopphold"));
            }
        }
    }

}