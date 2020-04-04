package no.nav.k9.søknad.pleiepengerbarn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
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

        var søknadDeser = FrisinnSøknad.SerDes.deserialize(json);
        Assert.assertNotNull(søknadDeser);
        JSONAssert.assertEquals(json, FrisinnSøknad.SerDes.serialize(søknadDeser), true);
    }

    private FrisinnSøknad byggSøknad() {
        var dato = LocalDate.now();
        var beløp = new BigDecimal("1000000.00");
        var periodeFør = new Periode(dato.minusDays(20), dato.minusDays(1));
        var periodeEtter = new Periode(dato.plusDays(1), dato.plusDays(20));
        var periodeInntekt = new PeriodeInntekt(beløp);
        
        var frilanser = Frilanser.builder()
                .inntektstapStartet(dato)
                .inntekterFør(Map.of(periodeFør, periodeInntekt))
                .inntekterEtter(Map.of(periodeEtter, periodeInntekt)).build();
        
        var selvstendig = SelvstendigNæringsdrivende.builder()
                .orgnummer("123")
                .inntektstapStartet(dato)
                .inntekterFør(Map.of(periodeFør, periodeInntekt))
                .inntekterEtter(Map.of(periodeEtter, periodeInntekt)).build();
        
        var søknad = FrisinnSøknad.builder()
            .søknadId(SøknadId.of("100-abc"))
            .søknadsperiode("2020-03-01/2020-03-31")
            .mottattDato(ZonedDateTime.parse("2019-10-20T07:15:36.124Z"))
            .språk(Språk.of("nb"))
            .søker(Søker.builder()
                .norskIdentitetsnummer(NorskIdentitetsnummer.of("12345678901"))
                .build())
            .inntekter(new Inntekter(frilanser, List.of(selvstendig)))
            .build();
        return søknad;
    }
}