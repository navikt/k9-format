package no.nav.k9.søknad.ytelse.psb;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.*;
import no.nav.k9.søknad.ytelse.psb.v1.Uttak.UttakPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

public class YtelseEksempel {

    public static PleiepengerSyktBarn ytelseMedSøknadOgDelperioder(Periode søknadsperiode, Periode... delperioder) {
        return minimumYtelseMedSøknadsperiode(søknadsperiode)
                .medBeredskap(lagBeredskap(delperioder))
                .medNattevåk(lagNattevåk(delperioder))
                .medTilsynsordning(lagTilsynsordning(delperioder))
                .medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(lagArbeidstaker(delperioder))));
    }

    public static PleiepengerSyktBarn komplettYtelseMedSøknadsperiode(Periode søknadsperiode, Periode lovbestemtFeriePeriode, Periode utenlandsperiode, Periode bostedperiode) {
        var søknadInfo = new DataBruktTilUtledning(true, true,
                false, false, "abc-123",  true, Arrays.asList(
                new UkjentArbeidsforhold()
                        .medOrganisasjonsnummer(Organisasjonsnummer.of("888888888"))
                        .medErAnsatt(true)
                        .medNormalarbeidstid(new NormalArbeidstid().medTimerPerUke(Duration.ofHours(7).plusMinutes(30)))
                        .medArbeiderIPerioden(ArbeiderIPeriodenSvar.SOM_VANLIG)
        ));
        var infoFraPunsj = new InfoFraPunsj()
                .medSøknadenInneholderInfomasjonSomIkkeKanPunsjes(false);

        return ytelseMedSøknadsperideOgArbeidstid(søknadsperiode)
                .medSøknadInfo(søknadInfo)
                .medInfoFraPunsj(infoFraPunsj)
                .medTilsynsordning(lagTilsynsordning(søknadsperiode))
                .medBeredskap(lagBeredskap(søknadsperiode))
                .medNattevåk(lagNattevåk(søknadsperiode))
                .medOmsorg(lagOmsorg())
                .medLovbestemtFerie(lagLovbestemtFerie(lovbestemtFeriePeriode))
                .medUtenlandsopphold(lagUtenlandsopphold(utenlandsperiode))
                .medBosteder(lagBosteder(bostedperiode));
    }


    public static PleiepengerSyktBarn minimumYtelseMedSøknadsperiode(Periode... perioder) {
        return lagYtelse()
                .medSøknadsperiode(List.of(perioder))
                .medUttak(lagUttak(perioder));
    }

    public static PleiepengerSyktBarn ytelseMedSøknadsperideOgArbeidstid(Periode... perioder) {
        return minimumYtelseMedSøknadsperiode(perioder)
                .medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(
                        lagArbeidstaker(perioder))));
    }

    public static PleiepengerSyktBarn komplettYtelseMedEndring(Periode... perioder) {
        return YtelseEksempel.lagYtelse()
                .medBeredskap(lagBeredskap(perioder))
                .medNattevåk(lagNattevåk(perioder))
                .medTilsynsordning(lagTilsynsordning(perioder))
                .medArbeidstid(new Arbeidstid().leggeTilArbeidstaker(lagArbeidstaker(perioder)))
                .medUttak(lagUttak(perioder));
    }

    public static PleiepengerSyktBarn lagYtelse() {
        return new PleiepengerSyktBarn()
                .medBarn(lagBarn());
    }

    public static Barn lagBarn() {
        return new Barn().medNorskIdentitetsnummer(TestUtils.okNorskIdentitetsnummerBarn());
    }

    public static Utenlandsopphold lagUtenlandsopphold(Periode... perioder) {
        Utenlandsopphold.UtenlandsoppholdPeriodeInfo utenlandsoppholdPeriodeInfo = new Utenlandsopphold
                .UtenlandsoppholdPeriodeInfo()
                .medLand(Landkode.FINLAND)
                .medÅrsak(Utenlandsopphold.UtenlandsoppholdÅrsak.BARNET_INNLAGT_I_HELSEINSTITUSJON_FOR_NORSK_OFFENTLIG_REGNING);
        return new Utenlandsopphold().medPerioder(
                lagPerioder(perioder, utenlandsoppholdPeriodeInfo));
    }

    public static Bosteder lagBosteder(Periode... perioder) {
        Bosteder.BostedPeriodeInfo bostedPeriodeInfo = new Bosteder.BostedPeriodeInfo()
                .medLand(Landkode.NORGE);
        return new Bosteder().medPerioder(
                lagPerioder(perioder, bostedPeriodeInfo));
    }

    public static LovbestemtFerie lagLovbestemtFerie(Periode... perioder) {
        LovbestemtFerie.LovbestemtFeriePeriodeInfo lovbestemtFeriePeriodeInfo = new LovbestemtFerie.LovbestemtFeriePeriodeInfo();
        return new LovbestemtFerie().medPerioder(
                lagPerioder(perioder, lovbestemtFeriePeriodeInfo));
    }

    public static Tilsynsordning lagTilsynsordning(Periode... perioder) {
        TilsynPeriodeInfo tilsynPeriodeInfo = new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7).plusMinutes(30));
        return new Tilsynsordning().medPerioder(
                lagPerioder(perioder, tilsynPeriodeInfo));
    }

    public static Nattevåk lagNattevåk(Periode... perioder) {
        Nattevåk.NattevåkPeriodeInfo nattevåkPeriodeInfo = new Nattevåk.NattevåkPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst());
        return new Nattevåk().medPerioder(
                lagPerioder(perioder, nattevåkPeriodeInfo));
    }

    public static Beredskap lagBeredskap(Periode... perioder) {
        Beredskap.BeredskapPeriodeInfo beredskapPeriodeInfo = new Beredskap.BeredskapPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst());
        return new Beredskap().medPerioder(
                lagPerioder(perioder, beredskapPeriodeInfo));
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
                .medArbeidstidInfo(new ArbeidstidInfo().medPerioder(
                        lagPerioder(perioder, arbeidstidPeriodeInfo)));
    }

    public static Uttak lagUttak(Periode... perioder) {
        UttakPeriodeInfo uttakPeriodeInfo = new UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30));
        return new Uttak().medPerioder(lagPerioder(perioder, uttakPeriodeInfo));
    }

    public static Omsorg lagOmsorg() {
        return new Omsorg().medRelasjonTilBarnet(Omsorg.BarnRelasjon.MOR);
    }

    public static <T> HashMap<Periode, T> lagPerioder(Periode[] periodeList, T periodeInfo) {
        var resultatMap = new HashMap<Periode, T>();
        for (Periode periode : periodeList) {
            resultatMap.put(periode, periodeInfo);
        }
        return resultatMap;
    }
}
