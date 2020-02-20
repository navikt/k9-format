package no.nav.k9.søknad.pleiepengerbarn;

import no.nav.k9.søknad.felles.Periode;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TilsynsordningTest {
    @Test
    public void fraTilsynsordningUke() {
        LocalDate fraOgMed = LocalDate.parse("2020-01-01");
        LocalDate tilOgMed = LocalDate.parse("2020-04-01");
        Duration mandagLengde = Duration.ofHours(7);
        Duration tirsdagLengde = Duration.ofHours(6);
        Duration onsdagLengde = Duration.ofHours(5);
        Duration torsdagLengde = Duration.ofHours(4);
        Duration fredagLengde = Duration.ofHours(3);

        Duration summertFørsteUke = onsdagLengde
                .plus(torsdagLengde)
                .plus(fredagLengde);

        Duration summertHeleUker = mandagLengde
                .plus(tirsdagLengde)
                .plus(onsdagLengde)
                .plus(torsdagLengde)
                .plus(fredagLengde);

        Duration summertSisteUke = mandagLengde
                .plus(tirsdagLengde)
                .plus(onsdagLengde);

        int expectedAntallUkerIPerioden = 14;
        int expectedAntallHeleUkerIPerioden = 12;


        Periode periode = Periode
                .builder()
                .fraOgMed(fraOgMed)
                .tilOgMed(tilOgMed)
                .build();

        TilsynsordningUke uke = TilsynsordningUke
                .builder()
                .periode(periode)
                .mandag(mandagLengde)
                .tirsdag(tirsdagLengde)
                .onsdag(onsdagLengde)
                .torsdag(torsdagLengde)
                .fredag(fredagLengde)
                .build();

        Tilsynsordning tilsynsordning = Tilsynsordning
                .builder()
                .iTilsynsordning(TilsynsordningSvar.JA)
                .uke(uke)
                .build();


        // TODO: JA da må du sette minst ett opphold.


        assertEquals(expectedAntallUkerIPerioden, tilsynsordning.opphold.size());

        AtomicInteger antallHeleUkerSjekket = new AtomicInteger();
        AtomicBoolean føresteUkeSjekket = new AtomicBoolean(false);
        AtomicBoolean sisteUkeSjekket = new AtomicBoolean(false);
        tilsynsordning.opphold.forEach((p,opphold) -> {

            if (p.fraOgMed.isEqual(fraOgMed)) {
                føresteUkeSjekket.set(true);
                assertEquals(summertFørsteUke, opphold.lengde);
            } else if (p.tilOgMed.isEqual(tilOgMed)) {
                sisteUkeSjekket.set(true);
                assertEquals(summertSisteUke, opphold.lengde);
            } else {
                assertEquals(summertHeleUker, opphold.lengde);
                antallHeleUkerSjekket.getAndIncrement();
            }
        });

        assertEquals(expectedAntallHeleUkerIPerioden, antallHeleUkerSjekket.get());
        assertTrue(føresteUkeSjekket.get());
        assertTrue(sisteUkeSjekket.get());
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

        assertEquals(1, tilsynsordning.opphold.size());
        assertEquals(
                Duration.ofHours(6).plusMinutes(30),
                tilsynsordning.opphold.get(Periode.builder().enkeltDag(torsdag).build()).lengde
        );
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

        assertEquals(0, tilsynsordning.opphold.size());
    }
}
