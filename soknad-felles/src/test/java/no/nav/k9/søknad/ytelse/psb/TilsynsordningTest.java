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

    @Test
    public void fraTilsynsordningUkeVedÅrsskifte() {
        LocalDate fraOgMed = LocalDate.parse("2020-12-25"); // Fredag
        LocalDate tilOgMed = LocalDate.parse("2021-01-11"); // Mandag
        Duration mandagLengde = Duration.ofHours(10).plusMinutes(45);
        Duration tirsdagLengde = Duration.ofHours(6);
        Duration onsdagLengde = Duration.ZERO;
        Duration torsdagLengde = Duration.ofHours(4);
        Duration fredagLengde = null;

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

        Duration summertHeleUker = TilsynsordningUke.MAX_LENGDE_PER_DAG // Ettersom mandag er over 7:30
                .plus(tirsdagLengde)
                .plus(onsdagLengde)
                .plus(torsdagLengde);
                // Fredaglengde er null

        // Kun mandag som er over maks lengde
        Duration summertSisteUke = TilsynsordningUke.MAX_LENGDE_PER_DAG;

        // Første uke er kun fredag og fredag har ingen lengde -> Dermed blir ikke den uken ett opphold.
        int expectedAntallUkerIPerioden = 3;
        int expectedAntallHeleUkerIPerioden = 2;

        assertEquals(expectedAntallUkerIPerioden, tilsynsordning.opphold.size());

        AtomicInteger antallHeleUkerSjekket = new AtomicInteger();
        AtomicBoolean sisteUkeSjekket = new AtomicBoolean(false);

        tilsynsordning.opphold.forEach((p,opphold) -> {
            if (p.tilOgMed.isEqual(tilOgMed)) {
                sisteUkeSjekket.set(true);
                assertEquals(summertSisteUke, opphold.lengde);
            } else {
                assertEquals(summertHeleUker, opphold.lengde);
                antallHeleUkerSjekket.getAndIncrement();
            }
        });

        assertEquals(expectedAntallHeleUkerIPerioden, antallHeleUkerSjekket.get());
        assertTrue(sisteUkeSjekket.get());
    }
}
