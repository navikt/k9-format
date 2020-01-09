package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.felles.Periode;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;


public class TilsynsordningTest {
    @Test
    public void fraTilsynsordningUke() {
        LocalDate fraOgMed = LocalDate.parse("2020-01-01");
        LocalDate tilOgMed = LocalDate.parse("2020-04-01");
        int mandager = 13;
        int tirsdager = 13;
        int onsdager = 14;
        int torsdag = 13;
        int fredag = 13;
        int antallDager = mandager + tirsdager + onsdager + torsdag + fredag;


        Periode periode = Periode
                .builder()
                .fraOgMed(fraOgMed)
                .tilOgMed(tilOgMed)
                .build();

        TilsynsordningUke uke = TilsynsordningUke
                .builder()
                .periode(periode)
                .mandag(Duration.ofHours(7))
                .tirsdag(Duration.ofHours(6))
                .onsdag(Duration.ofHours(5))
                .torsdag(Duration.ofHours(4))
                .fredag(Duration.ofHours(3))
                .build();

        Tilsynsordning tilsynsordning = Tilsynsordning
                .builder()
                .iTilsynsordning(TilsynsordningSvar.JA)
                .uke(uke)
                .build();

        Assert.assertEquals(antallDager, tilsynsordning.opphold.size());
    }

    @Test
    public void fraTilsynsordningUkeEnDagsPeriode() {
        LocalDate torsdag = LocalDate.parse("2020-01-09");

        Periode periode = Periode
                .builder()
                .fraOgMed(torsdag)
                .tilOgMed(torsdag)
                .build();

        TilsynsordningUke uke = TilsynsordningUke
                .builder()
                .periode(periode)
                .torsdag(Duration.ofHours(6).plusMinutes(30))
                .build();

        Tilsynsordning tilsynsordning = Tilsynsordning
                .builder()
                .iTilsynsordning(TilsynsordningSvar.JA)
                .uke(uke)
                .build();

        Assert.assertEquals(1, tilsynsordning.opphold.size());
    }

    @Test
    public void fraTilsynsordningUkeHelgeOgPeriodeUtenTilsyn() {
        LocalDate lørdag = LocalDate.parse("2020-01-11");
        LocalDate onsdag = LocalDate.parse("2020-01-15");

        Periode periode = Periode
                .builder()
                .fraOgMed(lørdag)
                .tilOgMed(onsdag)
                .build();

        TilsynsordningUke uke = TilsynsordningUke
                .builder()
                .periode(periode)
                .mandag(Duration.ZERO)
                .torsdag(Duration.ofDays(1))
                .fredag(Duration.ofMinutes(30))
                .build();

        Tilsynsordning tilsynsordning = Tilsynsordning
                .builder()
                .iTilsynsordning(TilsynsordningSvar.JA)
                .uke(uke)
                .build();

        Assert.assertEquals(0, tilsynsordning.opphold.size());
    }
}
