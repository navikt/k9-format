package no.nav.k9.søknad.ytelse.psb;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.Beredskap;
import no.nav.k9.søknad.ytelse.psb.v1.DataBruktTilUtledning;
import no.nav.k9.søknad.ytelse.psb.v1.InfoFraPunsj;
import no.nav.k9.søknad.ytelse.psb.v1.LovbestemtFerie;
import no.nav.k9.søknad.ytelse.psb.v1.Nattevåk;
import no.nav.k9.søknad.ytelse.psb.v1.Omsorg;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.Uttak;
import no.nav.k9.søknad.ytelse.psb.v1.UttakPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

public class YtelseEksempel {

    /*
    Komplett
     */

    public static PleiepengerSyktBarn komplettYtelseMedDelperioder() {

        var søknadsperiode = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-10-20"));
        var psb = komplettYtelse(søknadsperiode);

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
    }

    public static PleiepengerSyktBarn komplettYtelse(Periode periode) {
        return komplettYtelse(List.of(periode));
    }

    public static PleiepengerSyktBarn komplettYtelse(List<Periode> periodeList) {

        var barn = new Barn(NorskIdentitetsnummer.of("22211111111"), null);
        var omsorg = new Omsorg().medRelasjonTilBarnet(Omsorg.BarnRelasjon.MOR);
        var søknadInfo = new DataBruktTilUtledning( true, true,
                false, false, true );
        var infoFraPunsj = new InfoFraPunsj()
                .medSøknadenInneholderInfomasjonSomIkkeKanPunsjes(false);
        var uttak = lagUttak(periodeList);
        var nattevåk = lagNattevåk(periodeList);
        var beredskap = lagBeredskap(periodeList);
        var tilsynsordning = lagTilsynsordning(periodeList);
        var lovbestemtFerie = lagLovbestemtFerie(periodeList);
        var bosteder = lagBosteder(periodeList);
        var utenlandsopphold = lagUtenlandsopphold(periodeList);
        var arbeidstaker = lagArbeidstaker(periodeList);
        var arbeidstid = new Arbeidstid().medArbeidstaker(List.of(
                arbeidstaker));

        return new PleiepengerSyktBarn()
                .medSøknadsperiode(periodeList)
                .medSøknadInfo(søknadInfo)
                .medInfoFraPunsj(infoFraPunsj)
                .medBarn(barn)
                .medBeredskap(beredskap)
                .medNattevåk(nattevåk)
                .medTilsynsordning(tilsynsordning)
                .medArbeidstid(arbeidstid)
                .medUttak(uttak)
                .medUtenlandsopphold(utenlandsopphold)
                .medOmsorg(omsorg)
                .medLovbestemtFerie(lovbestemtFerie)
                .medBosteder(bosteder);
    }

    /*
    Standard
     */

    public static PleiepengerSyktBarn standardYtelse(Periode periode) {
        return standardYtelse(List.of(periode));
    }

    public static PleiepengerSyktBarn standardYtelse(List<Periode> periodeList) {
        return minimumYtelse(periodeList)
                .medArbeidstid(new Arbeidstid().medArbeidstaker(List.of(
                        lagArbeidstaker(periodeList))));
    }

    /*
    Minimum
     */
    public static PleiepengerSyktBarn minimumYtelseMedDelperioder() {
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
    }

    public static PleiepengerSyktBarn minimumYtelse(Periode søknadsperiode) {
        return minimumYtelse(List.of(søknadsperiode));
    }

    public static PleiepengerSyktBarn minimumYtelse(List<Periode> periodeList) {
        return new PleiepengerSyktBarn()
                .medSøknadsperiode(periodeList)
                .medBarn(new Barn(null, LocalDate.now()))
                .medUttak(lagUttak(periodeList));
    }


    /*
    Endring
     */
    private static PleiepengerSyktBarn lagEndringssøknad() {
        return new PleiepengerSyktBarn()
                .medBarn(new Barn(NorskIdentitetsnummer.of("11111111111"), null));
    }

    public static PleiepengerSyktBarn komplettEndringssøknad(Periode periode) {
        return komplettEndringssøknad(List.of(periode));
    }

    public static PleiepengerSyktBarn komplettEndringssøknad(List<Periode> periodeList) {
        return YtelseEksempel.lagEndringssøknad()
                .medBeredskap(lagBeredskap(periodeList))
                .medNattevåk(lagNattevåk(periodeList))
                .medTilsynsordning(lagTilsynsordning(periodeList))
                .medArbeidstid(new Arbeidstid().leggeTilArbeidstaker(lagArbeidstaker(periodeList)))
                .medUttak(lagUttak(periodeList));
    }

    public static PleiepengerSyktBarn standardYtelseMedEndring(Periode søknadsperiode, Periode endringsperiode) {
        var søknad = standardYtelse(søknadsperiode);
        leggPåKomplettEndringsøknad(endringsperiode, søknad);
        return søknad;
    }

    public static PleiepengerSyktBarn minimumYtelseMedEndring(Periode søknadsperiode, Periode endringsperiode) {
        var søknad = minimumYtelse(søknadsperiode);
        leggPåKomplettEndringsøknad(endringsperiode, søknad);
        return søknad;
    }

    public static void leggPåKomplettEndringsøknad(Periode endringsperiode, PleiepengerSyktBarn søknad) {
        var endringssøknad = komplettEndringssøknad(endringsperiode);
        søknad.getBeredskap().leggeTilPeriode(endringssøknad.getBeredskap().getPerioder());
        søknad.getNattevåk().leggeTilPeriode(endringssøknad.getNattevåk().getPerioder());
        søknad.getTilsynsordning().leggeTilPeriode(endringssøknad.getTilsynsordning().getPerioder());
        søknad.getArbeidstid().leggeTilArbeidstaker(endringssøknad.getArbeidstid().getArbeidstakerList());
        søknad.getUttak().leggeTilPeriode(endringssøknad.getUttak().getPerioder());
    }

    /*
    Util
     */

    public static Utenlandsopphold lagUtenlandsopphold(List<Periode> periodeList) {
        Utenlandsopphold.UtenlandsoppholdPeriodeInfo utenlandsoppholdPeriodeInfo = new Utenlandsopphold
                .UtenlandsoppholdPeriodeInfo()
                .medLand(Landkode.FINLAND)
                .medÅrsak(Utenlandsopphold.UtenlandsoppholdÅrsak.BARNET_INNLAGT_I_HELSEINSTITUSJON_FOR_NORSK_OFFENTLIG_REGNING);
        return new Utenlandsopphold().medPerioder(
                lagPerioder(periodeList, utenlandsoppholdPeriodeInfo));
    }

    public static Bosteder lagBosteder(List<Periode> periodeList) {
        Bosteder.BostedPeriodeInfo bostedPeriodeInfo = new Bosteder.BostedPeriodeInfo()
                .medLand(Landkode.NORGE);
        return new Bosteder().medPerioder(
                lagPerioder(periodeList, bostedPeriodeInfo));
    }

    public static LovbestemtFerie lagLovbestemtFerie(List<Periode> periodeList) {
        LovbestemtFerie.LovbestemtFeriePeriodeInfo lovbestemtFeriePeriodeInfo = new LovbestemtFerie.LovbestemtFeriePeriodeInfo();
        return new LovbestemtFerie().medPerioder(
                lagPerioder(periodeList, lovbestemtFeriePeriodeInfo));
    }

    public static Tilsynsordning lagTilsynsordning(List<Periode> periodeList) {
        TilsynPeriodeInfo tilsynPeriodeInfo = new TilsynPeriodeInfo().medEtablertTilsynTimerPerDag(Duration.ofHours(7).plusMinutes(30));
        return new Tilsynsordning().medPerioder(
                lagPerioder(periodeList, tilsynPeriodeInfo));
    }

    public static Nattevåk lagNattevåk(List<Periode> periodeList) {
        Nattevåk.NattevåkPeriodeInfo nattevåkPeriodeInfo = new Nattevåk.NattevåkPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst());
        return new Nattevåk().medPerioder(
                lagPerioder(periodeList, nattevåkPeriodeInfo));
    }

    public static Beredskap lagBeredskap(List<Periode> periodeList) {
        Beredskap.BeredskapPeriodeInfo beredskapPeriodeInfo = new Beredskap.BeredskapPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst());
        return new Beredskap().medPerioder(
                lagPerioder(periodeList, beredskapPeriodeInfo));
    }

    public static Arbeidstaker lagArbeidstaker(List<Periode> periodeList) {
        ArbeidstidPeriodeInfo arbeidstidPeriodeInfo = new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30));
        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("999999999"),
                new ArbeidstidInfo(
                        lagPerioder(periodeList, arbeidstidPeriodeInfo)));
        return arbeidstaker;
    }

    public static Uttak lagUttak(List<Periode> periodeList) {
        UttakPeriodeInfo uttakPeriodeInfo = new UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30));
        return new Uttak().medPerioder(lagPerioder(periodeList, uttakPeriodeInfo));
    }

    public static <T> HashMap<Periode, T> lagPerioder(List<Periode> periodeList, T periodeInfo) {
        var resultatMap = new HashMap<Periode, T>();
        for (Periode periode : periodeList) {
            resultatMap.put(periode, periodeInfo);
        }
        return resultatMap;
    }
}
