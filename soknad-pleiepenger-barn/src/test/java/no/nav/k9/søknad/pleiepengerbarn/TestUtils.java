package no.nav.k9.søknad.pleiepengerbarn;

import no.nav.k9.søknad.felles.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;

final class TestUtils {

    private TestUtils() {}

    private static String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/" + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String jsonForKomplettSøknad() {
        return jsonFromFile("komplett-søknad.json");
    }

    static PleiepengerBarnSøknad komplettSøknad() {
        return PleiepengerBarnSøknad.SerDes.deserialize(jsonForKomplettSøknad());
    }

    static PleiepengerBarnSøknad.Builder komplettBuilder() {
        return PleiepengerBarnSøknad.builder()
                .søknadId(SøknadId.of("1"))
                .søknadsperiode(
                        Periode.builder().fraOgMed(LocalDate.parse("2018-12-30")).tilOgMed(LocalDate.parse("2019-10-20")).build(),
                        SøknadsperiodeInfo.builder().build())
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .språk(Språk.of("nb"))
                .søker(Søker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("12345678901"))
                        .build())
                .barn(Barn.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("12345678902"))
                        .build())
                .bosteder(Bosteder.builder()
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2022-12-30")).tilOgMed(LocalDate.parse("2023-10-10")).build(),
                                Bosteder.BostedPeriodeInfo.builder()
                                        .land(Landkode.of("POL"))
                                        .build()
                        )
                        .build())
                .utenlandsopphold(Utenlandsopphold.builder()
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2018-12-30")).tilOgMed(LocalDate.parse("2019-10-10")).build(),
                                Utenlandsopphold.UtenlandsoppholdPeriodeInfo.builder().land(Landkode.of("SWE")).årsak(Utenlandsopphold.UtenlandsoppholdÅrsak.of("barnetInnlagtIHelseinstitusjonForNorskOffentligRegning")).build())
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2018-10-10")).tilOgMed(LocalDate.parse("2018-10-30")).build(),
                                Utenlandsopphold.UtenlandsoppholdPeriodeInfo.builder().land(Landkode.of("NOR")).build())
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2021-10-10")).tilOgMed(LocalDate.parse("2050-01-05")).build(),
                                Utenlandsopphold.UtenlandsoppholdPeriodeInfo.builder().land(Landkode.of("DEN")).årsak(Utenlandsopphold.UtenlandsoppholdÅrsak.of("barnetInnlagtIHelseinstitusjonDekketEtterAvtaleMedEtAnnetLandOmTrygd")).build())
                        .build())
                .beredskap(Beredskap.builder()
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2018-10-10")).tilOgMed(LocalDate.parse("2018-12-29")).build(),
                                Beredskap.BeredskapPeriodeInfo.builder().tilleggsinformasjon("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.").build())
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2019-01-01")).tilOgMed(LocalDate.parse("2019-01-30")).build(),
                                Beredskap.BeredskapPeriodeInfo.builder().tilleggsinformasjon("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.").build())
                        .build())
                .nattevåk(Nattevåk.builder()
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2018-10-10")).tilOgMed(LocalDate.parse("2018-12-29")).build(),
                                Nattevåk.NattevåkPeriodeInfo.builder().tilleggsinformasjon("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.").build())
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2019-01-01")).tilOgMed(LocalDate.parse("2019-01-30")).build(),
                                Nattevåk.NattevåkPeriodeInfo.builder().tilleggsinformasjon("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.").build())
                        .build())
                .tilsynsordning(Tilsynsordning.builder()
                        .iTilsynsordning(TilsynsordningSvar.of("ja"))
                        .opphold(
                                Periode.builder().fraOgMed(LocalDate.parse("2019-01-01")).tilOgMed(LocalDate.parse("2019-01-01")).build(),
                                TilsynsordningOpphold.builder().lengde(Duration.parse("PT7H30M")).build())
                        .opphold(
                                Periode.builder().fraOgMed(LocalDate.parse("2020-01-02")).tilOgMed(LocalDate.parse("2020-01-02")).build(),
                                TilsynsordningOpphold.builder().lengde(Duration.parse("PT7H25M")).build())
                        .opphold(
                                Periode.builder().fraOgMed(LocalDate.parse("2020-01-03")).tilOgMed(LocalDate.parse("2020-01-09")).build(),
                                TilsynsordningOpphold.builder().lengde(Duration.parse("PT168H")).build())
                        .build())
                .arbeid(Arbeid.builder()
                        .arbeidstaker(Arbeidstaker.builder()
                                .organisasjonsnummer(Organisasjonsnummer.of("999999999"))
                                .periode(
                                        Periode.builder().fraOgMed(LocalDate.parse("2018-10-10")).tilOgMed(LocalDate.parse("2018-12-29")).build(),
                                        Arbeidstaker.ArbeidstakerPeriodeInfo.builder().skalJobbeProsent(BigDecimal.valueOf(50.25)).build())
                                .build())
                        .arbeidstaker(Arbeidstaker.builder()
                                .norskIdentitetsnummer(NorskIdentitetsnummer.of("29099012345"))
                                .periode(
                                        Periode.builder().fraOgMed(LocalDate.parse("2018-11-10")).tilOgMed(LocalDate.parse("2018-12-29")).build(),
                                        Arbeidstaker.ArbeidstakerPeriodeInfo.builder().skalJobbeProsent(BigDecimal.valueOf(20.00)).build())
                                .build())
                        .selvstendigNæringsdrivende(SelvstendigNæringsdrivende.builder()
                                .periode(
                                        Periode.builder().fraOgMed(LocalDate.parse("2018-11-11")).tilOgMed(LocalDate.parse("2018-11-30")).build(),
                                        SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder().build())
                                .build())
                        .frilanser(Frilanser.builder()
                                .periode(
                                        Periode.builder().fraOgMed(LocalDate.parse("2019-10-10")).tilOgMed(LocalDate.parse("2019-12-29")).build(),
                                        Frilanser.FrilanserPeriodeInfo.builder().build())
                                .build())
                        .build())
                .lovbestemtFerie(LovbestemtFerie.builder()
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2018-11-10")).tilOgMed(LocalDate.parse("2018-12-29")).build(),
                                LovbestemtFerie.LovbestemtFeriePeriodeInfo.builder().build())
                        .build());
    }
}
