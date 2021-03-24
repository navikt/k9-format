package no.nav.k9.søknad.ytelse.psb;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.LovbestemtFerie;
import no.nav.k9.søknad.felles.LovbestemtFeriePeriodeInfo;
import no.nav.k9.søknad.felles.opptjeningAktivitet.*;
import no.nav.k9.søknad.felles.opptjeningAktivitet.arbeidstaker.PsbArbeidstaker;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.psb.v1.*;
import no.nav.k9.søknad.felles.opptjeningAktivitet.arbeidstaker.Arbeidstaker;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    static Søknad komplettSøknad() {
        return Søknad.SerDes.deserialize(jsonForKomplettSøknad());
    }

    static Ytelse komplettYtelsePsb(String ytelse) {
        return JsonUtils.fromString(ytelse, Ytelse.class);
    }

    static PleiepengerSyktBarn komplettYtelsePsb() {

        var søknadsperiode = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-10-20"));
        var delperiodeEn = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-02-20"));
        var delperiodeTo = new Periode(LocalDate.parse("2019-02-21"), LocalDate.parse("2019-10-20"));

        var uttak = new Uttak(Map.of(
            søknadsperiode, new UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));

        var arbeidstaker = new PsbArbeidstaker(null, Organisasjonsnummer.of("999999999"),
                new ArbeidstidInfo(
                        Map.of( søknadsperiode,
                                new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30)))));

        var arbeidstid = new Arbeidstid(List.of(
                arbeidstaker), null, null);

        var beredskap = new Beredskap(Map.of(
            delperiodeEn, new Beredskap.BeredskapPeriodeInfo("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ."),
            delperiodeTo, new Beredskap.BeredskapPeriodeInfo("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.")));

        var nattevåk = new Nattevåk(Map.of(
            delperiodeEn, new Nattevåk.NattevåkPeriodeInfo("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ."),
            delperiodeTo, new Nattevåk.NattevåkPeriodeInfo("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.")));

        var tilsynsordning = new Tilsynsordning(Map.of(
                new Periode(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-01-01")),
                new TilsynPeriodeInfo(Duration.ofHours(7).plusMinutes(30)),
                new Periode(LocalDate.parse("2019-01-02"), LocalDate.parse("2019-01-02")),
                new TilsynPeriodeInfo(Duration.ofHours(7).plusMinutes(30)),
                new Periode(LocalDate.parse("2019-01-03"), LocalDate.parse("2019-01-09")),
                new TilsynPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));

        var lovbestemtFerie = new LovbestemtFerie(Map.of(delperiodeTo, new LovbestemtFeriePeriodeInfo()));

        var aktivitet = OpptjeningAktivitet.builder()
            .selvstendigNæringsdrivende(SelvstendigNæringsdrivende.builder()
                .periode(
                    new Periode(LocalDate.parse("2018-11-11"), LocalDate.parse("2018-11-30")),
                    SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder()
                        .virksomhetstyper(List.of(VirksomhetType.FISKE)).build())
                .virksomhetNavn("Test")
                .build())
            .frilanser(Frilanser.builder()
                .startdato(LocalDate.parse("2019-10-10"))
                .jobberFortsattSomFrilans(true)
                .build())
            .build();

        var barn = new Barn(NorskIdentitetsnummer.of("11111111111"), null);

        var bosteder = new Bosteder(Map.of(
            søknadsperiode,
            new Bosteder.BostedPeriodeInfo(Landkode.DANMARK)));

        var utenlandsopphold = new Utenlandsopphold(Map.of(
            søknadsperiode,
            Utenlandsopphold.UtenlandsoppholdPeriodeInfo.builder()
                .land(Landkode.DANMARK)
                .årsak(Utenlandsopphold.UtenlandsoppholdÅrsak.BARNET_INNLAGT_I_HELSEINSTITUSJON_FOR_NORSK_OFFENTLIG_REGNING)
                .build()));

        var omsorg = new Omsorg("MORA", true,
                "Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.");

        var søknadInfo = new DataBruktTilUtledning( true, true,
                false, false, true );

        return new PleiepengerSyktBarn(søknadsperiode, søknadInfo, barn, aktivitet, beredskap, nattevåk, tilsynsordning, arbeidstid, uttak, omsorg, lovbestemtFerie, bosteder,
            utenlandsopphold);
    }

    static PleiepengerSyktBarn komplettYtelsePsb(Periode søknadsperiode) {

        var uttak = new Uttak(Map.of(
                søknadsperiode, new UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));

        var arbeidstaker = new PsbArbeidstaker(null, Organisasjonsnummer.of("999999999"),
                new ArbeidstidInfo(
                        Map.of( søknadsperiode,
                                new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30), Duration.ofHours(7).plusMinutes(30)))));

        var arbeidstid = new Arbeidstid(List.of(
                arbeidstaker), null, null);

        var beredskap = new Beredskap(Map.of(
                søknadsperiode, new Beredskap.BeredskapPeriodeInfo("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.")));

        var nattevåk = new Nattevåk(Map.of(
                søknadsperiode, new Nattevåk.NattevåkPeriodeInfo("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.")));

        var tilsynsordning = new Tilsynsordning(Map.of(
                søknadsperiode,
                new TilsynPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));

        var lovbestemtFerie = new LovbestemtFerie(Map.of(søknadsperiode, new LovbestemtFeriePeriodeInfo()));

        var barn = new Barn(NorskIdentitetsnummer.of("11111111111"), null);

        var bosteder = new Bosteder(Map.of(
                søknadsperiode,
                new Bosteder.BostedPeriodeInfo(Landkode.NORGE)));

        var omsorg = new Omsorg("MORA", true,
                "Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.");

        var søknadInfo = new DataBruktTilUtledning( true, true,
                false, false, true );

        return new PleiepengerSyktBarn(søknadsperiode, søknadInfo, barn, null, beredskap, nattevåk, tilsynsordning, arbeidstid, uttak, omsorg, lovbestemtFerie, bosteder,
                null);
    }

    static PleiepengerSyktBarn minimumSøknadPleiepengerSyktBarn() {
        var søknadsperiode = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-10-20"));
        var uttakperiode = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-02-20"));
        var uttakperiode2 = new Periode(LocalDate.parse("2019-02-21"), LocalDate.parse("2019-10-20"));

        var uttak = new Uttak(Map.of(
            uttakperiode, new UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30)),
            uttakperiode2, new UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));

        var barn = new Barn(null, LocalDate.now());

        return new PleiepengerSyktBarn().medSøknadsperiode(søknadsperiode).medBarn(barn).medUttak(uttak);
    }

}
