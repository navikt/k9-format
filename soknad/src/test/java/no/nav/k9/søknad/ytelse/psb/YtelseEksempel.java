package no.nav.k9.søknad.ytelse.psb;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import no.nav.k9.søknad.TestUtils;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.Beredskap;
import no.nav.k9.søknad.ytelse.psb.v1.DataBruktTilUtledning;
import no.nav.k9.søknad.ytelse.psb.v1.InfoFraPunsj;
import no.nav.k9.søknad.ytelse.psb.v1.LovbestemtFerie;
import no.nav.k9.søknad.ytelse.psb.v1.Nattevåk;
import no.nav.k9.søknad.ytelse.psb.v1.Omsorg;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.Tilsynsordning;
import no.nav.k9.søknad.ytelse.psb.v1.Tilsynsordning.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.Uttak;
import no.nav.k9.søknad.ytelse.psb.v1.Uttak.UttakPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;

public class YtelseEksempel {

    /*
    Komplett
     */

/*    @Deprecated
    public static PleiepengerSyktBarn ytelseKomplettMedFlerePerioder() {

        var søknadsperiode = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-10-20"));
        var psb = ytelseKomplett(søknadsperiode);

        var delperiodeEn = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-02-20"));
        var delperiodeTo = new Periode(LocalDate.parse("2019-02-21"), LocalDate.parse("2019-10-20"));

        psb.medBeredskap(new Beredskap().medPerioder(Map.of(
                delperiodeEn, new Beredskap.BeredskapPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()),
                delperiodeTo, new Beredskap.BeredskapPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()))));

        psb.medNattevåk(new Nattevåk().medPerioder(Map.of(
                delperiodeEn, new Nattevåk.NattevåkPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()),
                delperiodeTo, new Nattevåk.NattevåkPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()))));

        psb.medTilsynsordning( new Tilsynsordning().medPerioder(Map.of(
                new Periode(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-01-01")),
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7).plusMinutes(30)),
                new Periode(LocalDate.parse("2019-01-02"), LocalDate.parse("2019-01-02")),
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7).plusMinutes(30)),
                new Periode(LocalDate.parse("2019-01-03"), LocalDate.parse("2019-01-09")),
                new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7).plusMinutes(30)))));

        psb.medLovbestemtFerie(new LovbestemtFerie().medPerioder(
                Map.of(delperiodeTo, new LovbestemtFerie.LovbestemtFeriePeriodeInfo())));

        return psb;
    }*/

    public static PleiepengerSyktBarn komplettYtelseMedSøknadsperiode(Periode søknadsperiode, Periode lovbestemtFeriePeriode, Periode utenlandsperiode, Periode bostedperiode) {

        var søknadInfo = new DataBruktTilUtledning( true, true,
                false, false, true );
        var infoFraPunsj = new InfoFraPunsj()
                .medSøknadenInneholderInfomasjonSomIkkeKanPunsjes(false);

        return ytelseMedSøknadsperideOgArbeidstid((Periode) søknadsperiode)
                .medSøknadInfo(søknadInfo)
                .medTilsynsordning(lagTilsynsordning((Periode) søknadsperiode))
                .medBeredskap(lagBeredskap((Periode) søknadsperiode))
                .medNattevåk(lagNattevåk((Periode) søknadsperiode))
                .medOmsorg(lagOmsorg())
                .medLovbestemtFerie(lagLovbestemtFerie((Periode) lovbestemtFeriePeriode))
                .medUtenlandsopphold(lagUtenlandsopphold((Periode) utenlandsperiode))
                .medBosteder(lagBosteder((Periode) bostedperiode));
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

/*    @Deprecated
    public static PleiepengerSyktBarn ytelseMinimumMedFlerePerioder() {
        var søknadsperiode = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-10-20"));
        var uttakperiode = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-02-20"));
        var uttakperiode2 = new Periode(LocalDate.parse("2019-02-21"), LocalDate.parse("2019-10-20"));

        var uttak = new Uttak().medPerioder(Map.of(
                uttakperiode, new UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30)),
                uttakperiode2, new UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));

        var barn = new Barn(null, LocalDate.now());

        return new PleiepengerSyktBarn()
                .medSøknadsperiode(søknadsperiode)
                .medBarn(barn)
                .medUttak(uttak);
    }*/

    public static PleiepengerSyktBarn komplettYtelseMedEndring(Periode... perioder) {
        return YtelseEksempel.lagYtelse()
                .medBeredskap(lagBeredskap(perioder))
                .medNattevåk(lagNattevåk(perioder))
                .medTilsynsordning(lagTilsynsordning(perioder))
                .medArbeidstid(new Arbeidstid().leggeTilArbeidstaker(lagArbeidstaker(perioder)))
                .medUttak(lagUttak(perioder));
    }


//    public static void leggPåKomplettEndringsøknad(Periode endringsperiode, PleiepengerSyktBarn søknad) {
//        var endringssøknad = komplettEndringssøknad(endringsperiode);
//        søknad.medEndringsperiode(endringssøknad.getEndringsperiode());
//        søknad.getBeredskap().leggeTilPeriode(endringssøknad.getBeredskap().getPerioder());
//        søknad.getNattevåk().leggeTilPeriode(endringssøknad.getNattevåk().getPerioder());
//        søknad.getTilsynsordning().leggeTilPeriode(endringssøknad.getTilsynsordning().getPerioder());
//        søknad.getArbeidstid().leggeTilArbeidstaker(endringssøknad.getArbeidstid().getArbeidstakerList());
//        søknad.getUttak().leggeTilPeriode(endringssøknad.getUttak().getPerioder());
//    }

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
        ArbeidstidPeriodeInfo arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30));
        return lagArbeidstaker(arbeidstidPeriodeInfo, perioder);
    }

    public static Arbeidstaker lagArbeidstaker(ArbeidstidPeriodeInfo arbeidstidPeriodeInfo, Periode... perioder) {
        return new Arbeidstaker(null, Organisasjonsnummer.of("999999999"),
                new ArbeidstidInfo(
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
