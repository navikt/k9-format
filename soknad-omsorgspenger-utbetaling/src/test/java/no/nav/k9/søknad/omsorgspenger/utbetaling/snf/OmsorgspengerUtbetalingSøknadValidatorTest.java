package no.nav.k9.søknad.omsorgspenger.utbetaling.snf;

import static java.util.List.of;
import static no.nav.k9.søknad.omsorgspenger.utbetaling.snf.TestUtils.jsonForKomplettSøknad;
import static no.nav.k9.søknad.omsorgspenger.utbetaling.snf.TestUtils.komplettBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.ValideringsFeil;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.opptjening.Frilanser;
import no.nav.k9.søknad.felles.opptjening.SelvstendigNæringsdrivende;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.VirksomhetType;

@SuppressWarnings("removal")
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
        assertThat(builderFeil).hasSameSizeAs(jsonFeil);
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
        Assertions.assertThat(verifyHarFeil(builder)).hasSize(1);
        builder.barn = new ArrayList<>();
        builder.fosterbarn(Barn.builder().fødselsdato(LocalDate.now()).norskIdentitetsnummer(NorskIdentitetsnummer.of("123")).build());
        Assertions.assertThat(verifyHarFeil(builder)).hasSize(1);
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
        Assertions.assertThat(verifyHarFeil(builder)).hasSize(2);

    }

    @Test
    public void selvstending_næringsdrivende_mangler_påkrevde_felter() {
        var builder = komplettBuilder();
        List<SelvstendigNæringsdrivende> selvstendingeVirksomheter = of(
                SelvstendigNæringsdrivende.builder()
                        .organisasjonsnummer(Organisasjonsnummer.of("816338352"))
                        .periode(
                                new Periode(LocalDate.now().minusMonths(2), LocalDate.now()),
                                SelvstendigNæringsdrivende.SelvstendigNæringsdrivendePeriodeInfo.builder()
                                    .build()
                        ).build()
        );
        builder.selvstendigNæringsdrivende(selvstendingeVirksomheter);
        Assertions.assertThat(verifyHarFeil(builder)).hasSize(2);
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
        Assertions.assertThat(verifyHarFeil(builder)).hasSize(2);
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
        Assertions.assertThat(verifyHarFeil(builder)).hasSize(1);
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
        Assertions.assertThat(verifyHarFeil(builder)).hasSize(1);
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
        Assertions.assertThat(verifyHarFeil(builder)).hasSize(1);
    }

    @Test
    public void frilanser_mangler_startdato() {
        var builder = komplettBuilder();
        builder.frilanser(Frilanser.builder().build());
        Assertions.assertThat(verifyHarFeil(builder)).hasSize(1);
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
        Assertions.assertThat(feil).isNotEmpty();
        return feil;
    }

    private List<Feil> verifyHarFeil(OmsorgspengerUtbetalingSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        Assertions.assertThat(feil).isNotEmpty();
        return feil;
    }

    private void verifyIngenFeil(OmsorgspengerUtbetalingSøknad.Builder builder) {
        final List<Feil> feil = valider(builder);
        Assertions.assertThat(feil).isEmpty();

    }

    private void verifyIngenFeil(OmsorgspengerUtbetalingSøknad søknad) {
        final List<Feil> feil = validator.valider(søknad);
        Assertions.assertThat(feil).isEmpty();
    }

}
