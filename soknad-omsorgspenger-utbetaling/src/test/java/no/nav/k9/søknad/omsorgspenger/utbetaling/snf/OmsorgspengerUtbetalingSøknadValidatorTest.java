package no.nav.k9.søknad.omsorgspenger.utbetaling.snf;

import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.List.of;
import static junit.framework.TestCase.assertEquals;
import static no.nav.k9.søknad.omsorgspenger.utbetaling.snf.TestUtils.jsonForKomplettSøknad;
import static no.nav.k9.søknad.omsorgspenger.utbetaling.snf.TestUtils.komplettBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class OmsorgspengerUtbetalingSøknadValidatorTest {
    private static final OmsorgspengerUtbetalingSøknadValidator validator = new OmsorgspengerUtbetalingSøknadValidator();

    @Test
    public void søknadUtenNoeSatt() {
        OmsorgspengerUtbetalingSøknad.Builder builder = OmsorgspengerUtbetalingSøknad.builder();
        OmsorgspengerUtbetalingSøknad søknad = OmsorgspengerUtbetalingSøknad.SerDes.deserialize("{\"versjon\":\"0.0.1\"}");
        List<Feil> builderFeil = verifyHarFeil(builder);
        builderFeil = Collections.unmodifiableList(builderFeil);
        List<Feil> jsonFeil = verifyHarFeil(søknad);
        jsonFeil = Collections.unmodifiableList(jsonFeil);
        assertThat(builderFeil.size(), is(jsonFeil.size()));
    }

    @Test
    public void komplettSøknadFraJson() {
        OmsorgspengerUtbetalingSøknad søknad = OmsorgspengerUtbetalingSøknad.SerDes.deserialize(jsonForKomplettSøknad());
        verifyIngenFeil(søknad);
    }

    @Test
    public void barnISøknad() {
        var builder = komplettBuilder();
        builder.barn = new ArrayList<>();
        verifyIngenFeil(builder);
        builder.fosterbarn(Barn.builder().build());
        assertEquals(1, verifyHarFeil(builder).size());
        builder.barn = new ArrayList<>();
        builder.fosterbarn(Barn.builder().fødselsdato(LocalDate.now()).norskIdentitetsnummer(NorskIdentitetsnummer.of("123")).build());
        assertEquals(1, verifyHarFeil(builder).size());
        builder.barn = new ArrayList<>();
        builder.fosterbarn(Barn.builder().fødselsdato(LocalDate.now()).build());
        builder.fosterbarn(Barn.builder().norskIdentitetsnummer(NorskIdentitetsnummer.of("123")).build());
        verifyIngenFeil(builder);
    }

    @Test
    public void selvstending_næringsdrivende_null_eller_tom() {
        var builder = komplettBuilder();
        builder.selvstendigNæringsdrivende(new ArrayList<>());
        verifyIngenFeil(builder);
    }

    @Test
    public void selvstending_næringsdrivende_mangler_organisajonsnummer_virksomhetsnavn_og_perioder() {
        var builder = komplettBuilder();
        List<SelvstendigNæringsdrivende> selvstendingeVirksomheter = of(
                SelvstendigNæringsdrivende.builder()
                        .build()
        );
        builder.selvstendigNæringsdrivende(selvstendingeVirksomheter);
        assertEquals(3, verifyHarFeil(builder).size());
    }

    @Test
    public void selvstending_næringsdrivende_mangler_påkrevde_felter() {
        var builder = komplettBuilder();
        List<SelvstendigNæringsdrivende> selvstendingeVirksomheter = of(
                SelvstendigNæringsdrivende.builder()
                        .organisasjonsnummer(Organisasjonsnummer.of("816338352"))
                        .periode(
                                new Periode(LocalDate.now().minusMonths(2), LocalDate.now()),
                                SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder().build()
                        ).build()
        );
        builder.selvstendigNæringsdrivende(selvstendingeVirksomheter);
        assertEquals(4, verifyHarFeil(builder).size());
    }

    @Test
    public void selvstending_næringsdrivende_har_varig_endring_uten_påkrevde_felter() {
        var builder = komplettBuilder();
        List<SelvstendigNæringsdrivende> selvstendingeVirksomheter = of(
                SelvstendigNæringsdrivende.builder()
                        .organisasjonsnummer(Organisasjonsnummer.of("816338352"))
                        .virksomhetNavn("ABC")
                        .periode(
                                new Periode(LocalDate.now().minusMonths(2), LocalDate.now()),
                                SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder()
                                        .bruttoInntekt(BigDecimal.valueOf(500_00))
                                        .virksomhetstyper(of(VirksomhetType.JORDBRUK_SKOGBRUK))
                                        .erVarigEndring(true)
                                        .build()
                        ).build()
        );
        builder.selvstendigNæringsdrivende(selvstendingeVirksomheter);
        assertEquals(2, verifyHarFeil(builder).size());
    }

    @Test
    public void selvstending_næringsdrivende_er_registrert_i_utlandet_uten_landkode() {
        var builder = komplettBuilder();
        List<SelvstendigNæringsdrivende> selvstendingeVirksomheter = of(
                SelvstendigNæringsdrivende.builder()
                        .organisasjonsnummer(Organisasjonsnummer.of("816338352"))
                        .virksomhetNavn("ABC")
                        .periode(
                                new Periode(LocalDate.now().minusMonths(2), LocalDate.now()),
                                SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder()
                                        .bruttoInntekt(BigDecimal.valueOf(500_000))
                                        .virksomhetstyper(of(VirksomhetType.JORDBRUK_SKOGBRUK))
                                        .registrertIUtlandet(true)
                                        .build()
                        ).build()
        );
        builder.selvstendigNæringsdrivende(selvstendingeVirksomheter);
        assertEquals(1, verifyHarFeil(builder).size());
    }

    @Test
    public void selvstending_næringsdrivende_er_registrert_i_utlandet_med_lankode_blankt() {
        var builder = komplettBuilder();
        List<SelvstendigNæringsdrivende> selvstendingeVirksomheter = of(
                SelvstendigNæringsdrivende.builder()
                        .organisasjonsnummer(Organisasjonsnummer.of("816338352"))
                        .virksomhetNavn("ABC")
                        .periode(
                                new Periode(LocalDate.now().minusMonths(2), LocalDate.now()),
                                SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder()
                                        .bruttoInntekt(BigDecimal.valueOf(500_000))
                                        .virksomhetstyper(of(VirksomhetType.JORDBRUK_SKOGBRUK))
                                        .registrertIUtlandet(true)
                                        .landkode(Landkode.of(""))
                                        .build()
                        ).build()
        );
        builder.selvstendigNæringsdrivende(selvstendingeVirksomheter);
        assertEquals(1, verifyHarFeil(builder).size());
    }

    @Test
    public void selvstending_næringsdrivende_er_registrert_i_utlandet_med_ugyldig_landkode() {
        var builder = komplettBuilder();
        List<SelvstendigNæringsdrivende> selvstendingeVirksomheter = of(
                SelvstendigNæringsdrivende.builder()
                        .organisasjonsnummer(Organisasjonsnummer.of("816338352"))
                        .virksomhetNavn("ABC")
                        .periode(
                                new Periode(LocalDate.now().minusMonths(2), LocalDate.now()),
                                SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder()
                                        .bruttoInntekt(BigDecimal.valueOf(500_000))
                                        .virksomhetstyper(of(VirksomhetType.JORDBRUK_SKOGBRUK))
                                        .registrertIUtlandet(true)
                                        .landkode(Landkode.of("UKJENT"))
                                        .build()
                        ).build()
        );
        builder.selvstendigNæringsdrivende(selvstendingeVirksomheter);
        assertEquals(1, verifyHarFeil(builder).size());
    }

    @Test
    public void frilanser_mangler_startdato() {
        var builder = komplettBuilder();
        builder.frilanser(Frilanser.builder().build());
        assertEquals(1, verifyHarFeil(builder).size());
    }

    private List<Feil> valider(OmsorgspengerUtbetalingSøknad.Builder builder) {
        try {
            builder.build();
            return Collections.emptyList();
        } catch (ValideringsFeil ex) {
            return ex.getFeil();
        }
    }

    private List<Feil> verifyHarFeil(OmsorgspengerUtbetalingSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }

    private List<Feil> verifyHarFeil(OmsorgspengerUtbetalingSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil, is(not(Collections.emptyList())));
        return feil;
    }

    private void verifyIngenFeil(OmsorgspengerUtbetalingSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        assertThat(feil, is(Collections.emptyList()));
    }

    private void verifyIngenFeil(OmsorgspengerUtbetalingSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        assertThat(feil, is(Collections.emptyList()));
    }

    private OmsorgspengerUtbetalingSøknad.Builder medSøker() {
        return OmsorgspengerUtbetalingSøknad
                .builder()
                .søknadId(SøknadId.of("123"))
                .mottattDato(ZonedDateTime.now())
                .søker(Søker
                        .builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("11111111111"))
                        .build()
                );
    }
}
