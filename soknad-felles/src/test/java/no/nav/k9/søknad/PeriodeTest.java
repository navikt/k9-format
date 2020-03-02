package no.nav.k9.søknad;

import no.nav.k9.søknad.felles.Periode;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class PeriodeTest {

    @Test
    public void TestFinnSisteTilOgMedDatoBlantLukkedePerioder() {
        Map<Periode, Boolean> map = Map.of(
                Periode.builder()
                        .fraOgMed(LocalDate.parse("2017-01-01"))
                        .tilOgMed(LocalDate.parse("2019-02-02"))
                        .build(),
                true,
                Periode.builder()
                        .fraOgMed(LocalDate.parse("2022-01-01"))
                        .tilOgMed(LocalDate.parse("2023-02-02"))
                        .build(),
                true,
                Periode.builder()
                        .fraOgMed(LocalDate.parse("2010-01-01"))
                        .tilOgMed(LocalDate.parse("2011-02-02"))
                        .build(),
                true
        );

        LocalDate sisteTilOgMed = Periode.Utils.sisteTilOgMedBlantLukkedePerioder(map);
        assertEquals(LocalDate.parse("2023-02-02"), sisteTilOgMed);
    }

    @Test(expected = IllegalStateException.class)
    public void TestFinnSisteTilOgMedDatoBlantLukkedePerioderMedEnÅpenPeriode() {
        Map<Periode, Boolean> map = Map.of(
                Periode.builder()
                        .fraOgMed(LocalDate.parse("2017-01-01"))
                        .tilOgMed(LocalDate.parse("2019-02-02"))
                        .build(),
                true,
                Periode.builder()
                        .fraOgMed(LocalDate.parse("2022-01-01"))
                        .tilOgMed(LocalDate.parse("2023-02-02"))
                        .build(),
                true,
                Periode.builder()
                        .fraOgMed(LocalDate.parse("2010-01-01"))
                        .tilOgMed(null)
                        .build(),
                true
        );

        Periode.Utils.sisteTilOgMedBlantLukkedePerioder(map);
    }

    @Test
    public void TestFinnSisteTilOgMedDatoTillattÅpnePerioder() {
        Map<Periode, Boolean> map = Map.of(
                Periode.builder()
                        .fraOgMed(LocalDate.parse("2017-01-01"))
                        .tilOgMed(LocalDate.parse("2019-02-02"))
                        .build(),
                true,
                Periode.builder()
                        .fraOgMed(LocalDate.parse("2010-01-01"))
                        .tilOgMed(null)
                        .build(),
                true
        );

        LocalDate sisteTilOgMed = Periode.Utils.sisteTilOgMedTillatÅpnePerioder(map);
        assertEquals(null, sisteTilOgMed);
    }
}
