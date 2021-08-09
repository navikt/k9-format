package no.nav.k9.søknad;

import static no.nav.k9.søknad.felles.type.ÅpenPeriode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.type.ÅpenPeriode.Utils.leggTilPerioder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.type.ÅpenPeriode;

@Deprecated
public class PeriodeTest {

    @Test
    public void TestFinnSisteTilOgMedDatoBlantLukkedePerioder() {
        Map<ÅpenPeriode, Boolean> map = Map.of(
            new ÅpenPeriode(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-02-02")),
            true,
            new ÅpenPeriode(LocalDate.parse("2022-01-01"), LocalDate.parse("2023-02-02")),
            true,
            new ÅpenPeriode(LocalDate.parse("2010-01-01"), LocalDate.parse("2011-02-02")),
            true);

        LocalDate sisteTilOgMed = ÅpenPeriode.Utils.sisteTilOgMedBlantLukkedePerioder(map);
        assertEquals(LocalDate.parse("2023-02-02"), sisteTilOgMed);
    }

    @Test
    public void TestFinnSisteTilOgMedDatoBlantLukkedePerioderMedEnÅpenPeriode() {
        Map<ÅpenPeriode, Boolean> map = Map.of(
            new ÅpenPeriode(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-02-02")),
            true,
            new ÅpenPeriode(LocalDate.parse("2022-01-01"), LocalDate.parse("2023-02-02")),
            true,
            new ÅpenPeriode(LocalDate.parse("2010-01-01"), null),
            true);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            ÅpenPeriode.Utils.sisteTilOgMedBlantLukkedePerioder(map);
        });
    }

    @Test
    public void TestFinnSisteTilOgMedDatoTillattÅpnePerioder() {
        Map<ÅpenPeriode, Boolean> map = Map.of(
            new ÅpenPeriode(LocalDate.parse("2017-01-01"), LocalDate.parse("2019-02-02")),
            true,
            new ÅpenPeriode(LocalDate.parse("2010-01-01"), null),
            true);

        LocalDate sisteTilOgMed = ÅpenPeriode.Utils.sisteTilOgMedTillatÅpnePerioder(map);
        assertThat(sisteTilOgMed).isNull();
    }

    @Test
    public void TestFinnSisteTilOgMedDatoMedTomtMap() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            ÅpenPeriode.Utils.sisteTilOgMedTillatÅpnePerioder(Map.of());
        });
    }

    @Test
    public void TestFinnSisteTilOgMedDatoMedNull() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            ÅpenPeriode.Utils.sisteTilOgMedTillatÅpnePerioder(null);
        });
    }

    @Test
    public void TestForsikreLukketPeriode() {
        String fraOgMedIso = "2020-01-01";
        var åpnePeriode = ÅpenPeriode.parse(fraOgMedIso + "/..");
        LocalDate tilOgMed = LocalDate.parse("2020-01-20");
        var lukketPeriode = ÅpenPeriode.forsikreLukketPeriode(åpnePeriode, tilOgMed);
        assertThat(åpnePeriode.getFraOgMed()).isEqualTo(lukketPeriode.getFraOgMed());
        assertThat(tilOgMed).isEqualTo(lukketPeriode.getTilOgMed());

        lukketPeriode = ÅpenPeriode.forsikreLukketPeriode(ÅpenPeriode.parse("2020-02-01/2020-02-03"), LocalDate.parse("2022-01-01"));
        assertEquals(LocalDate.parse("2020-02-01"), lukketPeriode.getFraOgMed());
        assertEquals(LocalDate.parse("2020-02-03"), lukketPeriode.getTilOgMed());
    }

    @Test
    public void TestLeggTilPerioderMedDuplikater() {
        var periode1 = ÅpenPeriode.parse("2020-01-01/2021-01-01");
        var periode2 = ÅpenPeriode.parse("2020-04-04/..");
        var periode3 = ÅpenPeriode.parse("2020-05-05/2020-06-06");

        var perioder = Map.of(
            periode1, true,
            periode2, true);
        var nyePerioder = Map.of(
            periode1, true,
            periode2, true,
            periode3, true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            leggTilPerioder(perioder, nyePerioder);
        });
    }

    @Test
    public void TestLeggTilPerioderUtenDuplikater() {
        var periode1 = ÅpenPeriode.parse("2020-01-01/2021-01-01");
        var periode2 = ÅpenPeriode.parse("2020-04-04/..");
        var periode3 = ÅpenPeriode.parse("2020-05-05/2020-06-06");

        var perioder = new HashMap<>(Map.of(
            periode1, true,
            periode2, true));
        var nyePerioder = Map.of(
            periode3, true);

        leggTilPerioder(perioder, nyePerioder);
        assertThat(3).isEqualTo(perioder.size());
        assertThat(perioder.keySet()).containsAll(Set.of(periode1, periode2, periode3));
    }

    @Test
    public void TestLeggTilPeriodeMedDuplikater() {
        var periode1 = ÅpenPeriode.parse("2020-01-01/2021-01-01");
        var periode2 = ÅpenPeriode.parse("2020-04-04/..");

        var perioder = Map.of(
            periode1, true,
            periode2, true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            leggTilPeriode(perioder, periode1, true);
        });
    }

    @Test
    public void TestLeggTilPeriodeUtenDuplikater() {
        var periode1 = ÅpenPeriode.parse("2020-01-01/2021-01-01");
        var periode2 = ÅpenPeriode.parse("2020-04-04/..");

        var perioder = new HashMap<>(Map.of(
            periode1, true));

        leggTilPeriode(perioder, periode2, true);
        assertThat(perioder).hasSize(2);
        assertThat(perioder.keySet()).containsAll(Set.of(periode1, periode2));
    }
}
