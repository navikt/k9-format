package no.nav.k9.søknad.omsorgspenger.utbetaling;

import no.nav.k9.søknad.felles.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static no.nav.k9.søknad.felles.VirksomhetType.*;

class TestUtils {

    static String jsonForKomplettSøknad() {
        try {
            return Files.readString(Path.of("src/test/resources/komplett-søknad.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static String jsonForKomplettSøknadMedBarn() {
        try {
            return Files.readString(Path.of("src/test/resources/komplett-søknad-med-barn.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static String jsonForKomplettSøknadUtenNæringsinntenkt() {
        try {
            return Files.readString(Path.of("src/test/resources/komplett-søknad-uten-næringsinntekt.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static String jsonForSøknadUtenBarn() {
        try {
            return Files.readString(Path.of("src/test/resources/uten-barn.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static OmsorgspengerUtbetalingSøknad.Builder komplettBuilder() {
        return OmsorgspengerUtbetalingSøknad
                .builder()
                .selvstendigNæringsdrivende(Collections.singletonList(
                        SelvstendigNæringsdrivende.builder()
                                .organisasjonsnummer(Organisasjonsnummer.of("816338352"))
                                .virksomhetNavn("Alibaba Expresstoget")
                                .perioder(Collections.singletonMap(
                                        new Periode(LocalDate.parse("2018-11-11"), LocalDate.parse("2018-11-30")),
                                        SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder()
                                                .virksomhetstyper(List.of(DAGMAMMA, ENKELTPERSONFORETAK, FISKE, FRILANSER, JORDBRUK_SKOGBRUK, ANNEN, UDEFINERT))
                                                .bruttoInntekt(BigDecimal.valueOf(1_000_000))
                                                .endringBegrunnelse("fordi")
                                                .endringDato(LocalDate.parse("2018-12-12"))
                                                .erNyoppstartet(false)
                                                .erVarigEndring(true)
                                                .regnskapsførerNavn("Regnskapsfører Svensen")
                                                .regnskapsførerTelefon("12345678")
                                                .build()
                                )).build()
                ))
                .frilanser(Frilanser.builder()
                        .startdato(LocalDate.parse("2019-01-01"))
                        .jobberFortsattSomFrilans(false)
                        .build())
                .søker(Søker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build())
                .fosterbarn(Barn.builder().norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111113")).build())
                .fosterbarn(Barn.builder().fødselsdato(LocalDate.parse("2000-01-01")).build())
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .søknadId(SøknadId.of("123-123-123"));
    }

    static OmsorgspengerUtbetalingSøknad.Builder komplettBuilderUtenNæringsinntekt() {
        return OmsorgspengerUtbetalingSøknad
                .builder()
                .selvstendigNæringsdrivende(Collections.singletonList(
                        SelvstendigNæringsdrivende.builder()
                                .organisasjonsnummer(Organisasjonsnummer.of("816338352"))
                                .virksomhetNavn("Alibaba Expresstoget")
                                .perioder(Collections.singletonMap(
                                        new Periode(LocalDate.parse("2018-11-11"), LocalDate.parse("2018-11-30")),
                                        SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder()
                                                .virksomhetstyper(List.of(DAGMAMMA, ENKELTPERSONFORETAK, FISKE, FRILANSER, JORDBRUK_SKOGBRUK, ANNEN, UDEFINERT))
                                                .endringBegrunnelse("fordi")
                                                .endringDato(LocalDate.parse("2018-12-12"))
                                                .erNyoppstartet(false)
                                                .erVarigEndring(true)
                                                .regnskapsførerNavn("Regnskapsfører Svensen")
                                                .regnskapsførerTelefon("12345678")
                                                .build()
                                )).build()
                ))
                .frilanser(Frilanser.builder()
                        .startdato(LocalDate.parse("2019-01-01"))
                        .jobberFortsattSomFrilans(false)
                        .build())
                .søker(Søker.builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build())
                .fosterbarn(Barn.builder().norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111113")).build())
                .fosterbarn(Barn.builder().fødselsdato(LocalDate.parse("2000-01-01")).build())
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .søknadId(SøknadId.of("123-123-123"));
    }
}
