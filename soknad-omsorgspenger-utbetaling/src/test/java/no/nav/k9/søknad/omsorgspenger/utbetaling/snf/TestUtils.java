package no.nav.k9.søknad.omsorgspenger.utbetaling.snf;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import no.nav.k9.søknad.felles.opptjening.Frilanser;
import no.nav.k9.søknad.felles.opptjening.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.felles.type.VirksomhetType;

class TestUtils {

    static String jsonForKomplettSøknad() {
        try {
            return Files.readString(Path.of("src/test/resources/snf/komplett-søknad.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static String jsonForKomplettSøknadMedBarn() {
        try {
            return Files.readString(Path.of("src/test/resources/snf/komplett-søknad-med-barn.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static String jsonForKomplettSøknadUtenNæringsinntenkt() {
        try {
            return Files.readString(Path.of("src/test/resources/snf/komplett-søknad-uten-næringsinntekt.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static String jsonForSøknadUtenBarn() {
        try {
            return Files.readString(Path.of("src/test/resources/snf/uten-barn.json"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings({ "removal" })
    static OmsorgspengerUtbetalingSøknad.Builder komplettBuilder() {
        return OmsorgspengerUtbetalingSøknad
                .builder()
                .selvstendigNæringsdrivende(Collections.singletonList(
                        new SelvstendigNæringsdrivende()
                                .medOrganisasjonsnummer(Organisasjonsnummer.of("816338352"))
                                .medVirksomhetNavn("Alibaba Expresstoget")
                                .medPerioder(Collections.singletonMap(
                                        new Periode(LocalDate.parse("2018-11-11"), LocalDate.parse("2018-11-30")),
                                        new SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo()
                                                .medVirksomhetstyper(List.of(VirksomhetType.DAGMAMMA, VirksomhetType.FISKE, VirksomhetType.JORDBRUK_SKOGBRUK, VirksomhetType.ANNEN, VirksomhetType.UDEFINERT))
                                                .medBruttoInntekt(BigDecimal.valueOf(1_000_000))
                                                .medEndringBegrunnelse("fordi")
                                                .medEndringDato(LocalDate.parse("2018-12-12"))
                                                .medErNyoppstartet(false)
                                                .medErVarigEndring(true)
                                                .medErFiskerPåBladB(true)
                                                .medRegnskapsførerNavn("Regnskapsfører Svensen")
                                                .medRegnskapsførerTlf("12345678")
                                                .medRegistrertIUtlandet(true)
                                                .medLandkode(Landkode.of("DEU"))
                                ))
                ))
                .frilanser(new Frilanser()
                        .medStartDato(LocalDate.parse("2019-01-01"))
                        .medSluttDato(LocalDate.parse("2020-01-01")))
                .søker(new Søker(NorskIdentitetsnummer.of("11111111111")))
                .fosterbarn(new Barn().medNorskIdentitetsnummer(NorskIdentitetsnummer.of("11111111113")))
                .fosterbarn(new Barn().medFødselsdato(LocalDate.parse("2000-01-01")))
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .søknadId(SøknadId.of("123-123-123"));
    }

    @SuppressWarnings("removal")
    static OmsorgspengerUtbetalingSøknad.Builder komplettBuilderUtenNæringsinntekt() {
        return OmsorgspengerUtbetalingSøknad
                .builder()
                .selvstendigNæringsdrivende(Collections.singletonList(
                        new SelvstendigNæringsdrivende()
                                .medOrganisasjonsnummer(Organisasjonsnummer.of("816338352"))
                                .medVirksomhetNavn("Alibaba Expresstoget")
                                .medPerioder(Collections.singletonMap(
                                        new Periode(LocalDate.parse("2018-11-11"), LocalDate.parse("2018-11-30")),
                                        new SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo()
                                                .medVirksomhetstyper(List.of(VirksomhetType.DAGMAMMA, VirksomhetType.FISKE, VirksomhetType.JORDBRUK_SKOGBRUK, VirksomhetType.ANNEN, VirksomhetType.UDEFINERT))
                                                .medEndringBegrunnelse("fordi")
                                                .medEndringDato(LocalDate.parse("2018-12-12"))
                                                .medErNyoppstartet(false)
                                                .medErFiskerPåBladB(true)
                                                .medErVarigEndring(true)
                                                .medRegnskapsførerNavn("Regnskapsfører Svensen")
                                                .medRegnskapsførerTlf("12345678")
                                                .medRegistrertIUtlandet(true)
                                                .medLandkode(Landkode.of("DEU"))

                                ))
                ))
                .frilanser(new Frilanser()
                        .medStartDato(LocalDate.parse("2019-01-01")))
                .søker(new Søker(NorskIdentitetsnummer.of("11111111111")))
                .fosterbarn(new Barn().medNorskIdentitetsnummer(NorskIdentitetsnummer.of("11111111113")))
                .fosterbarn(new Barn().medFødselsdato(LocalDate.parse("2000-01-01")))
                .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
                .søknadId(SøknadId.of("123-123-123"));
    }
}
