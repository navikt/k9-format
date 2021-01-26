package no.nav.k9.søknad.frisinn;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Språk;
import no.nav.k9.søknad.felles.type.SøknadId;

public class FrisinnSøknadTest {

    @Test
    public void bygg_serialiser_og_deserialiser_roundtrip_førstegangssøknad() throws Exception {
        var søknad = førstegangssøknad();
        var json = FrisinnSøknad.SerDes.serialize(søknad);
        assertThat(json).isNotNull();
        var deserialisert = FrisinnSøknad.SerDes.deserialize(json);
        assertThat(deserialisert).isNotNull();
        assertThat(deserialisert.getInntekter().getSelvstendig().getInntekterFør()).isNotEmpty();
        assertThat(deserialisert.getInntekter().getSelvstendig().getRegnskapsførerNavn()).isNotNull();
        assertThat(deserialisert.getInntekter().getSelvstendig().getRegnskapsførerTlf()).isNotNull();
        JSONAssert.assertEquals(json, FrisinnSøknad.SerDes.serialize(deserialisert), true);
    }

    @Test
    public void bygg_serialiser_og_deserialiser_roundtrip_påfølgende_søknad() throws Exception {
        var søknad = påfølgendeSøknad();
        var json = FrisinnSøknad.SerDes.serialize(søknad);
        assertThat(json).isNotNull();
        var deserialisert = FrisinnSøknad.SerDes.deserialize(json);
        assertThat(deserialisert).isNotNull();
        assertThat(deserialisert.getInntekter().getSelvstendig().getInntekterFør()).isEmpty();
        assertThat(deserialisert.getInntekter().getSelvstendig().getRegnskapsførerNavn()).isNull();
        assertThat(deserialisert.getInntekter().getSelvstendig().getRegnskapsførerTlf()).isNull();
        JSONAssert.assertEquals(json, FrisinnSøknad.SerDes.serialize(deserialisert), true);
    }

    @Test
    public void deserilisere_og_validere_1_0_0_søknad() {
        var søknad = FrisinnSøknad.builder().json(jsonFromFile("1.0.0")).build();
        assertThat(Versjon.of("1.0.0")).isEqualTo(søknad.getVersjon());
        assertThat(søknad.getInntekter().getArbeidstaker()).isNull();
    }

    @Test
    public void deserilisere_og_validere_2_0_0_søknad() {
        var søknad = FrisinnSøknad.builder().json(jsonFromFile("2.0.0")).build();
        assertThat(Versjon.of("2.0.0")).isEqualTo(søknad.getVersjon());
        assertThat(søknad.getInntekter().getArbeidstaker()).isNotNull();
    }

    private FrisinnSøknad byggSøknad(
            Boolean medInntektFørSelvstendig,
            Boolean medRegnskapsførerNavn,
            Boolean medRegnskapsførerTelefon) {
        var dato = LocalDate.of(2020, 03, 13);
        var datoSøknad = LocalDate.of(2020, 04, 01);
        var beløp = new BigDecimal("1000000.00");
        var periodeFør = new Periode(dato.minusDays(20), dato.minusDays(1));
        var periodeEtter = new Periode(datoSøknad, datoSøknad.plusDays(20));
        var periodeInntekt = new PeriodeInntekt(beløp);

        var frilanser = new Frilanser(Map.of(periodeEtter, periodeInntekt), true);

        var selvstendig = new SelvstendigNæringsdrivende(
            medInntektFørSelvstendig ? Map.of(periodeFør, periodeInntekt, new Periode(null, dato.minusDays(21)), periodeInntekt)  : null,
            Map.of(periodeEtter, periodeInntekt, new Periode(datoSøknad, null), periodeInntekt),
            true,
            medRegnskapsførerNavn ? "Ola" : null,
            medRegnskapsførerTelefon ? "11111111" : null
        );

        var arbeidstaker = new Arbeidstaker(
                Map.of(
                        Periode.parse("2020-04-01/2020-04-22"),
                        new PeriodeInntekt(BigDecimal.valueOf(55555.50))
                )
        );

        var søknad = FrisinnSøknad.builder()
            .søknadId(SøknadId.of("100-abc"))
            .søknadsperiode("2020-04-01/2020-04-30")
            .mottattDato(ZonedDateTime.parse("2020-04-20T07:15:36.124Z"))
            .språk(Språk.of("nb"))
            .søker(Søker.builder()
                .norskIdentitetsnummer(NorskIdentitetsnummer.of("12345678901"))
                .build())
            .inntekter(new Inntekter(frilanser, selvstendig, arbeidstaker))
            .build();
        return søknad;
    }

    private FrisinnSøknad førstegangssøknad() {
        return byggSøknad(true, true, true);
    }

    private FrisinnSøknad påfølgendeSøknad() {
        return byggSøknad(false, false, false);
    }

    private static String jsonFromFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources/" + filename + ".json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
