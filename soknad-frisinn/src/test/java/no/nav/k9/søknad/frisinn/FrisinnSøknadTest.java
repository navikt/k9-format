package no.nav.k9.søknad.frisinn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import no.nav.k9.søknad.felles.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.Periode;
import no.nav.k9.søknad.felles.Språk;
import no.nav.k9.søknad.felles.Søker;
import no.nav.k9.søknad.felles.SøknadId;

public class FrisinnSøknadTest {

    @Test
    public void bygg_serialiser_og_deserialiser_roundtrip_søknad() throws Exception {
        var søknad = byggSøknad();

        var json = FrisinnSøknad.SerDes.serialize(søknad);
        Assert.assertNotNull(json);
        System.out.println(json);
        var søknadDeser = FrisinnSøknad.SerDes.deserialize(json);
        Assert.assertNotNull(søknadDeser);
        JSONAssert.assertEquals(json, FrisinnSøknad.SerDes.serialize(søknadDeser), true);
    }

    private FrisinnSøknad byggSøknad() {
        var dato = LocalDate.of(2020, 03, 13);
        var datoSøknad = LocalDate.of(2020, 04, 01);
        var beløp = new BigDecimal("1000000.00");
        var periodeFør = new Periode(dato.minusDays(20), dato.minusDays(1));
        var periodeEtter = new Periode(datoSøknad, datoSøknad.plusDays(20));
        var periodeInntekt = new PeriodeInntekt(beløp);

        var frilanser = new Frilanser(Map.of(periodeEtter, periodeInntekt), true);

        var selvstendig = new SelvstendigNæringsdrivende(
            Map.of(periodeFør, periodeInntekt,
                new Periode(null, dato.minusDays(21)), periodeInntekt),
            Map.of(periodeEtter, periodeInntekt,
                new Periode(datoSøknad, null), periodeInntekt),
            true);

        var søknad = FrisinnSøknad.builder()
            .søknadId(SøknadId.of("100-abc"))
            .søknadsperiode("2020-04-01/2020-04-30")
            .mottattDato(ZonedDateTime.parse("2020-04-20T07:15:36.124Z"))
            .språk(Språk.of("nb"))
            .søker(Søker.builder()
                .norskIdentitetsnummer(NorskIdentitetsnummer.of("12345678901"))
                .build())
            .inntekter(new Inntekter(frilanser, selvstendig))
            .build();
        return søknad;
    }
}