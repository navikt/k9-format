package no.nav.k9.søknad.ytelse.psb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.opptjening.Frilanser;
import no.nav.k9.søknad.felles.opptjening.OpptjeningAktivitet;
import no.nav.k9.søknad.felles.opptjening.Organisasjonsnummer;
import no.nav.k9.søknad.felles.opptjening.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.opptjening.VirksomhetType;
import no.nav.k9.søknad.ytelse.psb.v1.InfoFraPunsj;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstaker;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.psb.v1.Beredskap;
import no.nav.k9.søknad.ytelse.psb.v1.DataBruktTilUtledning;
import no.nav.k9.søknad.ytelse.psb.v1.LovbestemtFerie;
import no.nav.k9.søknad.ytelse.psb.v1.LovbestemtFerie.LovbestemtFeriePeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.Nattevåk;
import no.nav.k9.søknad.ytelse.psb.v1.Omsorg;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;
import no.nav.k9.søknad.ytelse.psb.v1.Uttak;
import no.nav.k9.søknad.ytelse.psb.v1.UttakPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

final class TestUtils {

    private TestUtils() {
    }

    private static String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/ytelse/psb/" + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String jsonForKomplettSøknad() {
        return jsonFromFile("komplett-søknad.json");
    }

    static Søknad komplettSøknadJson() {
        return Søknad.SerDes.deserialize(jsonForKomplettSøknad());
    }

    static Ytelse komplettYtelsePsbJson(String ytelse) {
        return JsonUtils.fromString(ytelse, Ytelse.class);
    }

    static PleiepengerSyktBarn komplettYtelsePsbMedDelperioder() {

        var søknadsperiode = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-10-20"));
        var psb = komplettYtelsePsb(søknadsperiode);

        var delperiodeEn = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-02-20"));
        var delperiodeTo = new Periode(LocalDate.parse("2019-02-21"), LocalDate.parse("2019-10-20"));

        psb.medBeredskap(new Beredskap().medPerioder(Map.of(
                delperiodeEn, new Beredskap.BeredskapPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()),
                delperiodeTo, new Beredskap.BeredskapPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()))));

        psb.medNattevåk(new Nattevåk().medPerioder(Map.of(
                delperiodeEn, new Nattevåk.NattevåkPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()),
                delperiodeTo, new Nattevåk.NattevåkPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()))));

        psb.medTilsynsordning( new Tilsynsordning(Map.of(
                new Periode(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-01-01")),
                new TilsynPeriodeInfo(Duration.ofHours(7).plusMinutes(30)),
                new Periode(LocalDate.parse("2019-01-02"), LocalDate.parse("2019-01-02")),
                new TilsynPeriodeInfo(Duration.ofHours(7).plusMinutes(30)),
                new Periode(LocalDate.parse("2019-01-03"), LocalDate.parse("2019-01-09")),
                new TilsynPeriodeInfo(Duration.ofHours(7).plusMinutes(30)))));

        psb.medLovbestemtFerie(new LovbestemtFerie().medPerioder(
                Map.of(delperiodeTo, new LovbestemtFeriePeriodeInfo())));

        return psb;
    }

    static PleiepengerSyktBarn komplettYtelsePsb(Periode søknadsperiode) {

        var uttak = new Uttak(Map.of(
                søknadsperiode, new UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));

        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("999999999"),
                new ArbeidstidInfo(
                        Map.of( søknadsperiode,
                                new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30)))));

        var arbeidstid = new Arbeidstid(List.of(
                arbeidstaker), null, null);

        var beredskap = new Beredskap().medPerioder(Map.of(
                søknadsperiode, new Beredskap.BeredskapPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst())));

        var nattevåk = new Nattevåk().medPerioder(Map.of(
                søknadsperiode, new Nattevåk.NattevåkPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst())));

        var tilsynsordning = new Tilsynsordning(Map.of(
                søknadsperiode, new TilsynPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));

        var lovbestemtFerie = new LovbestemtFerie().medPerioder(Map.of(
                søknadsperiode, new LovbestemtFeriePeriodeInfo()));

        var barn = new Barn(NorskIdentitetsnummer.of("11111111111"), null);

        var bosteder = new Bosteder(Map.of(
                søknadsperiode, new Bosteder.BostedPeriodeInfo(Landkode.NORGE)));

        var omsorg = new Omsorg().medRelasjonTilBarnet(Omsorg.BarnRelasjon.MOR);

        var søknadInfo = new DataBruktTilUtledning( true, true,
                false, false, true );

        var infoFraPunsj = new InfoFraPunsj().medSøknadenInneholderInfomasjonSomIkkeKanPunsjes(false);

        return new PleiepengerSyktBarn()
                .medSøknadsperiode(søknadsperiode)
                .medSøknadInfo(søknadInfo)
                .medInfoFraPunsj(infoFraPunsj)
                .medBarn(barn)
                .medBeredskap(beredskap)
                .medNattevåk(nattevåk)
                .medTilsynsordning(tilsynsordning)
                .medArbeidstid(arbeidstid)
                .medUttak(uttak)
                .medOmsorg(omsorg)
                .medLovbestemtFerie(lovbestemtFerie)
                .medBosteder(bosteder);
    }

    static PleiepengerSyktBarn minimumSøknadPleiepengerSyktBarn() {
        var søknadsperiode = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-10-20"));
        var uttakperiode = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-02-20"));
        var uttakperiode2 = new Periode(LocalDate.parse("2019-02-21"), LocalDate.parse("2019-10-20"));

        var uttak = new Uttak(Map.of(
            uttakperiode, new UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30)),
            uttakperiode2, new UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));

        var barn = new Barn(null, LocalDate.now());

        var omsorg = new Omsorg()
                .medRelasjonTilBarnet(Omsorg.BarnRelasjon.MOR)
                .medBeskrivelseAvOmsorgsrollen(TestUtils.testTekst());

        return new PleiepengerSyktBarn()
                .medSøknadsperiode(søknadsperiode)
                .medBarn(barn)
                .medUttak(uttak)
                .medOmsorg(omsorg);
    }

    static PleiepengerSyktBarn minimumEndringssøknad(Periode endringsperiode) {
        return new PleiepengerSyktBarn()
                .medEndringsperiode(endringsperiode)
                .medBarn(new Barn(NorskIdentitetsnummer.of("11111111111"), null));
    }

    static PleiepengerSyktBarn fullEndringssøknad(Periode periode, Periode endringsperiode) {
        return TestUtils.minimumEndringssøknad(endringsperiode)
                .medBeredskap(new Beredskap().medPerioder(Map.of(
                        periode, new Beredskap.BeredskapPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()))))
                .medNattevåk(new Nattevåk().medPerioder(Map.of(
                        periode, new Nattevåk.NattevåkPeriodeInfo().medTilleggsinformasjon(TestUtils.testTekst()))))
                .medTilsynsordning(new Tilsynsordning(Map.of(periode, new TilsynPeriodeInfo(Duration.ofHours(5)))))
                .medArbeidstid(new Arbeidstid().medArbeidstakerList(List.of(
                        new Arbeidstaker(null, Organisasjonsnummer.of("999999999"), new ArbeidstidInfo(
                                Map.of(periode, new ArbeidstidPeriodeInfo(Duration.ofHours(8), Duration.ofHours(4))))))))
                .medUttak(new Uttak(Map.of(periode, new UttakPeriodeInfo(Duration.ofHours(3)))));
    }

    static String testTekst() {
        return "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    }

}
