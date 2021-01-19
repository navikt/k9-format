package no.nav.k9.søknad;

import no.nav.k9.søknad.felles.type.Periode;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPerioder;
import static org.junit.Assert.*;


public class PeriodeTest {

    @Test
    public void TestFinnSisteTilOgMedDatoBlantLukkedePerioder() {
        Map<Periode, Boolean> map = Map.of(
                new Periode(LocalDate.parse("2017-01-01"),LocalDate.parse("2019-02-02")),
                true,
                new Periode(LocalDate.parse("2022-01-01"),LocalDate.parse("2023-02-02")),
                true,
                new Periode(LocalDate.parse("2010-01-01"), LocalDate.parse("2011-02-02")),
                true
        );

        LocalDate sisteTilOgMed = Periode.Utils.sisteTilOgMedBlantLukkedePerioder(map);
        assertEquals(LocalDate.parse("2023-02-02"), sisteTilOgMed);
    }

    @Test(expected = IllegalStateException.class)
    public void TestFinnSisteTilOgMedDatoBlantLukkedePerioderMedEnÅpenPeriode() {
        Map<Periode, Boolean> map = Map.of(
                new Periode(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-02-02")),
                true,
                new Periode(LocalDate.parse("2022-01-01"), LocalDate.parse("2023-02-02")),
                true,
                new Periode(LocalDate.parse("2010-01-01"), null),
                true
        );

        Periode.Utils.sisteTilOgMedBlantLukkedePerioder(map);
    }

    @Test
    public void TestFinnSisteTilOgMedDatoTillattÅpnePerioder() {
        Map<Periode, Boolean> map = Map.of(
                new Periode(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-02-02")),
                true,
                new Periode(LocalDate.parse("2010-01-01"), null),
                true
        );

        LocalDate sisteTilOgMed = Periode.Utils.sisteTilOgMedTillatÅpnePerioder(map);
        assertNull(sisteTilOgMed);
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
        assertEquals(åpnePeriode.getFraOgMed(), lukketPeriode.getFraOgMed());
        assertEquals(tilOgMed, lukketPeriode.getTilOgMed());

        lukketPeriode = Periode.forsikreLukketPeriode(Periode.parse("2020-02-01/2020-02-03"), LocalDate.parse("2022-01-01"));
        assertEquals(LocalDate.parse("2020-02-01"), lukketPeriode.getFraOgMed());
        assertEquals(LocalDate.parse("2020-02-03"), lukketPeriode.getTilOgMed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestLeggTilPerioderMedDuplikater() {
        var periode1 = Periode.parse("2020-01-01/2021-01-01");
        var periode2 = Periode.parse("2020-04-04/..");
        var periode3 = Periode.parse("2020-05-05/2020-06-06");

        var perioder = Map.of(
                periode1, true,
                periode2, true
        );
        var nyePerioder = Map.of(
                periode1, true,
                periode2, true,
                periode3, true
        );
        leggTilPerioder(perioder, nyePerioder);
    }

    @Test
    public void TestLeggTilPerioderUtenDuplikater() {
        var periode1 = Periode.parse("2020-01-01/2021-01-01");
        var periode2 = Periode.parse("2020-04-04/..");
        var periode3 = Periode.parse("2020-05-05/2020-06-06");

        var perioder = new HashMap<>(Map.of(
                periode1, true,
                periode2, true
        ));
        var nyePerioder = Map.of(
                periode3, true
        );

        leggTilPerioder(perioder, nyePerioder);
        assertEquals(3, perioder.size());
        assertTrue(perioder.keySet().containsAll(Set.of(periode1, periode2, periode3)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestLeggTilPeriodeMedDuplikater() {
        var periode1 = Periode.parse("2020-01-01/2021-01-01");
        var periode2 = Periode.parse("2020-04-04/..");

        var perioder = Map.of(
                periode1, true,
                periode2, true
        );

        leggTilPeriode(perioder, periode1, true);
    }

    @Test
    public void TestLeggTilPeriodeUtenDuplikater() {
        var periode1 = Periode.parse("2020-01-01/2021-01-01");
        var periode2 = Periode.parse("2020-04-04/..");

        var perioder = new HashMap<>(Map.of(
                periode1, true
        ));

        leggTilPeriode(perioder, periode2, true);
        assertEquals(2, perioder.size());
        assertTrue(perioder.keySet().containsAll(Set.of(periode1, periode2)));
    }
}
