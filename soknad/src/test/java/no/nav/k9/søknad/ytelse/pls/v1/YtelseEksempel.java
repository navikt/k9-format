package no.nav.k9.søknad.ytelse.pls.v1;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.LovbestemtFerie;
import no.nav.k9.søknad.ytelse.psb.v1.Uttak;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;

public class YtelseEksempel {

    public static PleipengerLivetsSluttfase ytelseForArbeidstaker(Periode... søknadsperioder) {
        return new PleipengerLivetsSluttfase()
                .medPleietrengende(lagPleietrengende())
                .medSøknadsperiode(Arrays.asList(søknadsperioder))
                .medUttak(lagUttak(søknadsperioder))
                .medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(YtelseEksempel.lagArbeidstaker(søknadsperioder))));
    }

    public static PleipengerLivetsSluttfase ytelseMedUtenlandstilsnitt(Periode søknadsperiode, Periode utenlandsperiode, Periode bostedperiode) {
        return ytelseForArbeidstaker(søknadsperiode)
                .medUtenlandsopphold(lagUtenlandsopphold(utenlandsperiode))
                .medBosteder(lagBosteder(bostedperiode));
    }

    public static PleipengerLivetsSluttfase lagYtelse() {
        return new PleipengerLivetsSluttfase()
                .medPleietrengende(lagPleietrengende());
    }

    public static Pleietrengende lagPleietrengende() {
        return new Pleietrengende().medNorskIdentitetsnummer(TestUtils.okNorskIdentitetsnummerBarn());
    }

    public static Bosteder lagBosteder(Periode... perioder) {
        Bosteder.BostedPeriodeInfo bostedPeriodeInfo = new Bosteder.BostedPeriodeInfo()
                .medLand(Landkode.NORGE);
        return new Bosteder().medPerioder(
                lagPerioder(perioder, bostedPeriodeInfo));
    }

    public static LovbestemtFerie lagLovbestemtFerie(Periode... perioder) {
        LovbestemtFerie.LovbestemtFeriePeriodeInfo feriePeriodeInfo = new LovbestemtFerie.LovbestemtFeriePeriodeInfo().medSkalHaFerie(true);
        return new LovbestemtFerie().medPerioder(lagPerioder(perioder, feriePeriodeInfo));
    }

    public static Utenlandsopphold lagUtenlandsopphold(Periode... perioder) {
        //TODO PLS denne årsaken gir ingen mening for ytelsen?
        return lagUtenlandsopphold(Utenlandsopphold.UtenlandsoppholdÅrsak.BARNET_INNLAGT_I_HELSEINSTITUSJON_FOR_NORSK_OFFENTLIG_REGNING, perioder);
    }

    public static Utenlandsopphold lagUtenlandsopphold(Utenlandsopphold.UtenlandsoppholdÅrsak årsak, Periode... perioder) {
        Utenlandsopphold.UtenlandsoppholdPeriodeInfo utenlandsoppholdPeriodeInfo = new Utenlandsopphold
                .UtenlandsoppholdPeriodeInfo()
                .medLand(Landkode.FINLAND)
                .medÅrsak(årsak);
        return new Utenlandsopphold().medPerioder(
                lagPerioder(perioder, utenlandsoppholdPeriodeInfo));
    }

    public static Arbeidstaker lagArbeidstaker(Periode... perioder) {
        ArbeidstidPeriodeInfo arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo()
                .medJobberNormaltTimerPerDag(Duration.ofHours(7).plusMinutes(30))
                .medFaktiskArbeidTimerPerDag(Duration.ofHours(7).plusMinutes(30));
        return lagArbeidstaker(arbeidstidPeriodeInfo, perioder);
    }

    public static Arbeidstaker lagArbeidstaker(ArbeidstidPeriodeInfo arbeidstidPeriodeInfo, Periode... perioder) {
        return new Arbeidstaker()
                .medOrganisasjonsnummer(Organisasjonsnummer.of("999999999"))
                .medArbeidstidInfo(new ArbeidstidInfo()
                        .medPerioder(lagPerioder(perioder, arbeidstidPeriodeInfo)));
    }

    public static <T> HashMap<Periode, T> lagPerioder(Periode[] periodeList, T periodeInfo) {
        var resultatMap = new HashMap<Periode, T>();
        for (Periode periode : periodeList) {
            resultatMap.put(periode, periodeInfo);
        }
        return resultatMap;
    }

    public static PleipengerLivetsSluttfase komplettYtelseMedEndring(Periode... perioder) {
        return lagYtelse()
                .medArbeidstid(new Arbeidstid().leggeTilArbeidstaker(lagArbeidstaker(perioder)))
                .medUttak(lagUttak(perioder));
    }

    public static Uttak lagUttak(Periode... perioder) {
        Uttak.UttakPeriodeInfo uttakPeriodeInfo = new Uttak.UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30));
        return new Uttak().medPerioder(lagPerioder(perioder, uttakPeriodeInfo));
    }

    public static PleipengerLivetsSluttfase minimumYtelseMedSøknadsperiode(Periode... perioder) {
        return lagYtelse()
                .medSøknadsperiode(List.of(perioder))
                .medUttak(lagUttak(perioder));
    }
}
