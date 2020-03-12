package no.nav.k9.søknad.pleiepengerbarn;

import no.nav.k9.søknad.felles.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.Organisasjonsnummer;
import no.nav.k9.søknad.felles.Periode;
import org.json.JSONException;
import org.junit.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;

import static no.nav.k9.søknad.pleiepengerbarn.PleiepengerBarnSøknad.Utils.alleArbeidstakerPerioderInneholderJobberNormaltPerUke;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PleiepengerBarnSøknadTest {
    @Test
    public void serialiseringAvJsonOgBrukAvBuilderGirSammeResultat() throws JSONException {
        String json = TestUtils.jsonForKomplettSøknad();
        PleiepengerBarnSøknad fraBuilder = TestUtils.komplettBuilder().build();
        JSONAssert.assertEquals(json, PleiepengerBarnSøknad.SerDes.serialize(fraBuilder), true);
    }

    @Test
    public void reserialisering() throws JSONException {
        String json = TestUtils.jsonForKomplettSøknad();
        PleiepengerBarnSøknad søknad = PleiepengerBarnSøknad.SerDes.deserialize(json);
        JSONAssert.assertEquals(json, PleiepengerBarnSøknad.SerDes.serialize(søknad), true);
    }

    @Test
    public void UtilPåOmAlleArbeidstakerPerioderInneholderJobberNormaltPerUke() {
        final var enTime = Duration.ofHours(1);
        final var builder = TestUtils.komplettBuilder();

        var arbeid = arbeid(null, null, null);
        assertFalse(alleArbeidstakerPerioderInneholderJobberNormaltPerUke(
                builder.arbeid(arbeid).build()
        ));

        arbeid = arbeid(enTime, null, null);
        assertFalse(alleArbeidstakerPerioderInneholderJobberNormaltPerUke(
                builder.arbeid(arbeid).build()
        ));

        arbeid = arbeid(enTime, enTime, null);
        assertFalse(alleArbeidstakerPerioderInneholderJobberNormaltPerUke(
                builder.arbeid(arbeid).build()
        ));

        arbeid = arbeid(enTime, null, enTime);
        assertFalse(alleArbeidstakerPerioderInneholderJobberNormaltPerUke(
                builder.arbeid(arbeid).build()
        ));

        arbeid = arbeid(enTime, enTime, enTime);
        assertTrue(alleArbeidstakerPerioderInneholderJobberNormaltPerUke(
                builder.arbeid(arbeid).build()
        ));
    }

    private Arbeid arbeid(
            Duration jobberNormaltPerUkePeriode1,
            Duration jobberNormaltPerUkePeriode2,
            Duration jobberNormaltPerUkePeriode3) {
        return Arbeid
                .builder()
                .arbeidstaker(Arbeidstaker
                        .builder()
                        .organisasjonsnummer(Organisasjonsnummer.of("88888888"))
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().plusDays(3)).build(),
                                Arbeidstaker.ArbeidstakerPeriodeInfo.builder()
                                        .skalJobbeProsent(BigDecimal.valueOf(40.00))
                                        .jobberNormaltPerUke(jobberNormaltPerUkePeriode1)
                                        .build())
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.now().plusDays(6)).tilOgMed(LocalDate.now().plusDays(10)).build(),
                                Arbeidstaker.ArbeidstakerPeriodeInfo.builder()
                                        .skalJobbeProsent(BigDecimal.valueOf(50.00))
                                        .jobberNormaltPerUke(jobberNormaltPerUkePeriode2)
                                        .build())
                        .build()
                )
                .arbeidstaker(Arbeidstaker
                        .builder()
                        .norskIdentitetsnummer(NorskIdentitetsnummer.of("123"))
                        .periode(
                                Periode.builder().fraOgMed(LocalDate.now()).tilOgMed(LocalDate.now().plusDays(3)).build(),
                                Arbeidstaker.ArbeidstakerPeriodeInfo.builder()
                                        .skalJobbeProsent(BigDecimal.valueOf(40.00))
                                        .jobberNormaltPerUke(jobberNormaltPerUkePeriode3)
                                        .build())
                        .build()
                )
                .build();
    }
}