package no.nav.k9.soknad.pleiepengerbarn;

import no.nav.k9.soknad.felles.Periode;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

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
        int antallDager = 66;


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

        assertEquals(antallDager, tilsynsordning.opphold.size());

        hentForUkeDag(tilsynsordning.opphold, DayOfWeek.MONDAY).forEach(opphold -> assertEquals(mandagLengde, opphold.lengde));
        hentForUkeDag(tilsynsordning.opphold, DayOfWeek.TUESDAY).forEach(opphold -> assertEquals(tirsdagLengde, opphold.lengde));
        hentForUkeDag(tilsynsordning.opphold, DayOfWeek.WEDNESDAY).forEach(opphold -> assertEquals(onsdagLengde, opphold.lengde));
        hentForUkeDag(tilsynsordning.opphold, DayOfWeek.THURSDAY).forEach(opphold -> assertEquals(torsdagLengde, opphold.lengde));
        hentForUkeDag(tilsynsordning.opphold, DayOfWeek.FRIDAY).forEach(opphold -> assertEquals(fredagLengde, opphold.lengde));
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

    private List<TilsynsordningOpphold> hentForUkeDag(
            Map<Periode, TilsynsordningOpphold> opphold,
            DayOfWeek dag) {
        List<TilsynsordningOpphold> tilsynsordningOpphold = new ArrayList<>();
        opphold
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().fraOgMed != null)
            .filter(entry -> dag == entry.getKey().fraOgMed.getDayOfWeek())
            .forEach(entry -> tilsynsordningOpphold.add(entry.getValue()));
        return tilsynsordningOpphold;
    }
}
