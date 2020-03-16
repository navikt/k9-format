package no.nav.k9.søknad;

import no.nav.k9.søknad.felles.Periode;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static no.nav.k9.søknad.felles.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.Periode.Utils.leggTilPerioder;
import static org.junit.Assert.*;


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

    @Test(expected = IllegalStateException.class)
    public void TestFinnSisteTilOgMedDatoMedTomtMap() {
        Periode.Utils.sisteTilOgMedTillatÅpnePerioder(Map.of());
    }

    @Test(expected = IllegalStateException.class)
    public void TestFinnSisteTilOgMedDatoMedNull() {
        Periode.Utils.sisteTilOgMedTillatÅpnePerioder(null);
    }

    @Test
    public void TestForsikreLukketPeriode() {
        String fraOgMedIso = "2020-01-01";
        Periode åpnePeriode = Periode.parse(fraOgMedIso + "/..");
        LocalDate tilOgMed = LocalDate.parse("2020-01-20");
        Periode lukketPeriode = Periode.forsikreLukketPeriode(åpnePeriode, tilOgMed);
        assertEquals(åpnePeriode.fraOgMed, lukketPeriode.fraOgMed);
        assertEquals(tilOgMed, lukketPeriode.tilOgMed);

        lukketPeriode = Periode.forsikreLukketPeriode(Periode.parse("2020-02-01/2020-02-03"), LocalDate.parse("2022-01-01"));
        assertEquals(LocalDate.parse("2020-02-01"), lukketPeriode.fraOgMed);
        assertEquals(LocalDate.parse("2020-02-03"), lukketPeriode.tilOgMed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestLeggTilPerioderMedDuplikater() {
        var periode1 = Periode.parse("2020-01-01/2021-01-01");
        var periode2 = Periode.parse("2020-04-04/..");
        var periode3 = Periode.parse("2020-05-05/2020-06-06");

        var eksisterende = Map.of(
                periode1, true,
                periode2, true
        );
        var nytt = Map.of(
                periode1, true,
                periode2, true,
                periode3, true
        );
        leggTilPerioder(eksisterende, nytt);
    }

    @Test
    public void TestLeggTilPerioderUtenDuplikater() {
        var periode1 = Periode.parse("2020-01-01/2021-01-01");
        var periode2 = Periode.parse("2020-04-04/..");
        var periode3 = Periode.parse("2020-05-05/2020-06-06");

        var eksisterende = new HashMap<>(Map.of(
                periode1, true,
                periode2, true
        ));
        var nytt = Map.of(
                periode3, true
        );

        var oppdatert = leggTilPerioder(eksisterende, nytt);
        assertEquals(3, oppdatert.size());
        assertTrue(oppdatert.keySet().containsAll(Set.of(periode1, periode2, periode3)));
        assertEquals(eksisterende, oppdatert);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestLeggTilPeriodeMedDuplikater() {
        var periode1 = Periode.parse("2020-01-01/2021-01-01");
        var periode2 = Periode.parse("2020-04-04/..");

        var eksisterende = Map.of(
                periode1, true,
                periode2, true
        );

        leggTilPeriode(eksisterende, periode1, true);
    }

    @Test
    public void TestLeggTilPeriodeUtenDuplikater() {
        var periode1 = Periode.parse("2020-01-01/2021-01-01");
        var periode2 = Periode.parse("2020-04-04/..");

        var eksisterende = new HashMap<>(Map.of(
                periode1, true
        ));

        var oppdatert = leggTilPeriode(eksisterende, periode2, true);
        assertEquals(2, oppdatert.size());
        assertTrue(oppdatert.keySet().containsAll(Set.of(periode1, periode2)));
        assertEquals(eksisterende, oppdatert);
    }
}
