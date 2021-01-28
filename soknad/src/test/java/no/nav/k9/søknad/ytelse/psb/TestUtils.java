package no.nav.k9.søknad.ytelse.psb;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.LovbestemtFerie;
import no.nav.k9.søknad.felles.aktivitet.*;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.psb.v1.*;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidInfo;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.ArbeidstidPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynPeriodeInfo;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

import java.io.IOException;
import java.math.BigDecimal;
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

    static PleiepengerSyktBarn komplettBuilder() {

        var søknadsperiode = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-10-20"));
        var delperiodeEn = new Periode(LocalDate.parse("2018-12-30"), LocalDate.parse("2019-02-20"));
        var delperiodeTo = new Periode(LocalDate.parse("2019-02-21"), LocalDate.parse("2019-05-21"));

        var uttak = new Uttak(Map.of(
            søknadsperiode, new UttakPeriodeInfo(Duration.ofHours(7).plusMinutes(30))));

        var arbeidstaker = new Arbeidstaker(null, Organisasjonsnummer.of("999999999"),
                new ArbeidstidInfo(Duration.ofHours(7).plusMinutes(30), Map.of(
                        søknadsperiode,
                        new ArbeidstidPeriodeInfo(Duration.ofHours(7).plusMinutes(30)))));

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

        var lovbestemtFerie = new LovbestemtFerie(List.of(delperiodeTo));

        var aktivitet = ArbeidAktivitet.builder()
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

        var barn = Barn.builder()
            .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
            .fødselsdato(null)
            .build();

        var bosteder = new Bosteder(Map.of(
            søknadsperiode,
            Bosteder.BostedPeriodeInfo.builder().land(Landkode.DANMARK).build()));

        var utenlandsopphold = new Utenlandsopphold(Map.of(
            søknadsperiode,
            Utenlandsopphold.UtenlandsoppholdPeriodeInfo.builder()
                .land(Landkode.DANMARK)
                .årsak(Utenlandsopphold.UtenlandsoppholdÅrsak.BARNET_INNLAGT_I_HELSEINSTITUSJON_FOR_NORSK_OFFENTLIG_REGNING)
                .build()));

        var søknadInfo = new SøknadInfo( "MORA", true,
                "Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.", true, true,
                false, false, false, true );

        return new PleiepengerSyktBarn(søknadsperiode, søknadInfo, barn, aktivitet, beredskap, nattevåk, tilsynsordning, arbeidstid, uttak, lovbestemtFerie, bosteder,
            utenlandsopphold);
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
