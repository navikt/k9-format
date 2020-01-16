package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.JsonUtils;
import no.nav.k9.soknad.felles.*;

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

    static String jsonForKomplettSoknad() {
        return jsonFromFile("komplett-soknad.json");
    }

    static PleiepengerBarnSoknad komplettSoknad() {
        final PleiepengerBarnSoknad soknad = JsonUtils.fromString(jsonForKomplettSoknad(), PleiepengerBarnSoknad.class);
        return soknad;
    }

    static PleiepengerBarnSoknad.Builder komplettBuilder() {
        return PleiepengerBarnSoknad.builder()
                .soknadId(SoknadId.of("1"))
                .periode(Periode.builder()
                        .fraOgMed(LocalDate.parse("2018-12-30"))
                        .tilOgMed(LocalDate.parse("2019-10-20"))
                        .build())
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .spraak(Spraak.of("nb"))
                .soker(Soker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("12345678901"))
                        .build())
                .barn(Barn.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("12345678902"))
                        .build())
                .utland(Utland.builder()
                        .harBoddIUtlandetSiste12Mnd(true)
                        .skalBoIUtlandetNeste12Mnd(true)
                        .opphold(
                                Periode.builder().fraOgMed(LocalDate.parse("2018-12-30")).tilOgMed(LocalDate.parse("2019-10-10")).build(),
                                UtlandOpphold.builder().land(Landkode.of("SWE")).build())
                        .opphold(
                                Periode.builder().fraOgMed(LocalDate.parse("2018-10-10")).tilOgMed(LocalDate.parse("2018-10-30")).build(),
                                UtlandOpphold.builder().land(Landkode.of("NOR")).build())
                        .opphold(
                                Periode.builder().fraOgMed(LocalDate.parse("2021-10-10")).build(),
                                UtlandOpphold.builder().land(Landkode.of("DEN")).build())
                        .build())
                .beredskap(Beredskap.builder()
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2018-10-10")).tilOgMed(LocalDate.parse("2018-12-29")).build(),
                                Beredskap.BeredskapPeriodeInfo.builder().tilleggsinformasjon("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.").build())
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2019-01-01")).tilOgMed(LocalDate.parse("2019-01-30")).build(),
                                Beredskap.BeredskapPeriodeInfo.builder().tilleggsinformasjon("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.").build())
                        .build())
                .nattevaak(Nattevaak.builder()
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2018-10-10")).tilOgMed(LocalDate.parse("2018-12-29")).build(),
                                Nattevaak.NattevaakPeriodeInfo.builder().tilleggsinformasjon("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.").build())
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2019-01-01")).tilOgMed(LocalDate.parse("2019-01-30")).build(),
                                Nattevaak.NattevaakPeriodeInfo.builder().tilleggsinformasjon("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.").build())
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
                        .build());
    }
}
