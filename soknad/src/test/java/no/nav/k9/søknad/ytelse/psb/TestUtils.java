package no.nav.k9.søknad.ytelse.psb;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.LovbestemtFerie;
import no.nav.k9.søknad.felles.opptjening.Opptjening;
import no.nav.k9.søknad.felles.opptjening.arbeidstaker.Arbeidstaker;
import no.nav.k9.søknad.felles.opptjening.snf.Frilanser;
import no.nav.k9.søknad.felles.opptjening.snf.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.tilsyn.Tilsynsordning;
import no.nav.k9.søknad.ytelse.psb.tilsyn.TilsynsordningOpphold;
import no.nav.k9.søknad.ytelse.psb.tilsyn.TilsynsordningSvar;

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

    static PleiepengerSyktBarn komplettBuilder() {
        var søknadsperiodeMap = Map.of(Periode.builder().fraOgMed(LocalDate.parse("2018-12-30")).tilOgMed(LocalDate.parse("2019-10-20")).build(), SøknadsperiodeInfo.builder().build());
        var beredskap = Beredskap.builder()
                .periode(Periode.builder().fraOgMed(LocalDate.parse("2018-10-10")).tilOgMed(LocalDate.parse("2018-12-29")).build(),
                        Beredskap.BeredskapPeriodeInfo.builder().tilleggsinformasjon("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.").build())
                .periode(Periode.builder().fraOgMed(LocalDate.parse("2019-01-01")).tilOgMed(LocalDate.parse("2019-01-30")).build(),
                        Beredskap.BeredskapPeriodeInfo.builder().tilleggsinformasjon("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.").build())
                .build();
        var nattevåk = Nattevåk.builder()
                .periode(Periode.builder().fraOgMed(LocalDate.parse("2018-10-10")).tilOgMed(LocalDate.parse("2018-12-29")).build(),
                        Nattevåk.NattevåkPeriodeInfo.builder().tilleggsinformasjon("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.").build())
                .periode(Periode.builder().fraOgMed(LocalDate.parse("2019-01-01")).tilOgMed(LocalDate.parse("2019-01-30")).build(),
                        Nattevåk.NattevåkPeriodeInfo.builder().tilleggsinformasjon("Noe tilleggsinformasjon. Lorem ipsum æÆøØåÅ.").build())
                .build();
        var tilsynsordning = Tilsynsordning.builder()
                .iTilsynsordning(TilsynsordningSvar.of("ja"))
                .opphold(Periode.builder().fraOgMed(LocalDate.parse("2019-01-01")).tilOgMed(LocalDate.parse("2019-01-01")).build(),
                        TilsynsordningOpphold.builder().lengde(Duration.parse("PT7H30M")).build())
                .opphold(Periode.builder().fraOgMed(LocalDate.parse("2020-01-02")).tilOgMed(LocalDate.parse("2020-01-02")).build(),
                        TilsynsordningOpphold.builder().lengde(Duration.parse("PT7H25M")).build())
                .opphold(Periode.builder().fraOgMed(LocalDate.parse("2020-01-03")).tilOgMed(LocalDate.parse("2020-01-09")).build(),
                        TilsynsordningOpphold.builder().lengde(Duration.parse("PT168H")).build())
                .build();
        var arbeidstaker = List.of(Arbeidstaker.builder()
                .organisasjonsnummer(Organisasjonsnummer.of("999999999"))
                .periode(Periode.builder().fraOgMed(LocalDate.parse("2018-10-10")).tilOgMed(LocalDate.parse("2018-12-29")).build(),
                        Arbeidstaker.ArbeidstakerPeriodeInfo.builder()
                                .skalJobbeProsent(BigDecimal.valueOf(50.25))
                                .jobberNormaltPerUke(Duration.parse("PT37H30M"))
                                .build()).build());

        var lovbestemtFerie = LovbestemtFerie.builder()
                .periode(Periode.builder().fraOgMed(LocalDate.parse("2018-11-10")).tilOgMed(LocalDate.parse("2018-12-29")).build(),
                        LovbestemtFerie.LovbestemtFeriePeriodeInfo.builder().build())
                .build();

        var opptjening = Opptjening.builder()
                .selvstendigNæringsdrivende(SelvstendigNæringsdrivende.builder()
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.parse("2018-11-11")).tilOgMed(LocalDate.parse("2018-11-30")).build(),
                                SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder().build())
                        .build())
                .frilanser(Frilanser.builder()
                        .startdato(LocalDate.parse("2019-10-10"))
                        .jobberFortsattSomFrilans(true)
                        .build())
                .build();
        var barn = Barn.builder()
                .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                .fødselsdato(LocalDate.now())
                .build();

        return new PleiepengerSyktBarn(søknadsperiodeMap, barn, beredskap, nattevåk, tilsynsordning, opptjening, arbeidstaker, lovbestemtFerie);
    }
}
