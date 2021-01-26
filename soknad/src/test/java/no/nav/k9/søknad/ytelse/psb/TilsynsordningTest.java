package no.nav.k9.søknad.ytelse.psb;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynsordningSvar;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.TilsynsordningUke;

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

        Periode periode = new Periode(fraOgMed, tilOgMed);

        TilsynsordningUke uke = TilsynsordningUke
                .builder()
                .periode(periode)
                .mandag(mandagLengde)
                .tirsdag(tirsdagLengde)
                .onsdag(onsdagLengde)
                .torsdag(torsdagLengde)
                .fredag(fredagLengde)
                .build();

        Tilsynsordning tilsynsordning = new Tilsynsordning(TilsynsordningSvar.JA, uke.getOpphold());

        assertThat(expectedAntallUkerIPerioden).isEqualTo(tilsynsordning.getOpphold().size());

        AtomicInteger antallHeleUkerSjekket = new AtomicInteger();
        AtomicBoolean føresteUkeSjekket = new AtomicBoolean(false);
        AtomicBoolean sisteUkeSjekket = new AtomicBoolean(false);
        tilsynsordning.getOpphold().forEach((p,opphold) -> {

            if (p.getFraOgMed().isEqual(fraOgMed)) {
                føresteUkeSjekket.set(true);
                assertThat(summertFørsteUke).isEqualTo(opphold.getLengde());
            } else if (p.getTilOgMed().isEqual(tilOgMed)) {
                sisteUkeSjekket.set(true);
                assertThat(summertSisteUke).isEqualTo(opphold.getLengde());
            } else {
                assertThat(summertHeleUker).isEqualTo(opphold.getLengde());
                antallHeleUkerSjekket.getAndIncrement();
            }
        });

        assertThat(expectedAntallHeleUkerIPerioden).isEqualTo(antallHeleUkerSjekket.get());
        assertThat(føresteUkeSjekket.get()).isTrue();
        assertThat(sisteUkeSjekket.get()).isTrue();
    }

    @Test
    public void fraTilsynsordningUkeEnDagsPeriode() {
        LocalDate torsdag = LocalDate.parse("2020-01-09");

        Periode periode = new Periode(torsdag, torsdag);

        TilsynsordningUke uke = TilsynsordningUke
                .builder()
                .periode(periode)
                .torsdag(Duration.ofHours(6).plusMinutes(30))
                .build();

        Tilsynsordning tilsynsordning = new Tilsynsordning(TilsynsordningSvar.JA, uke.getOpphold());

        assertThat(tilsynsordning.getOpphold()).hasSize(1);
        assertThat(
                Duration.ofHours(6).plusMinutes(30)).isEqualTo( tilsynsordning.getOpphold().get(new Periode(torsdag, torsdag)).getLengde()
        );
    }

    @Test
    public void fraTilsynsordningUkeHelgeOgPeriodeUtenTilsyn() {
        LocalDate lørdag = LocalDate.parse("2020-01-11");
        LocalDate onsdag = LocalDate.parse("2020-01-15");

        Periode periode = new Periode(lørdag, onsdag);

        TilsynsordningUke uke = TilsynsordningUke
                .builder()
                .periode(periode)
                .mandag(Duration.ZERO)
                .torsdag(Duration.ofDays(1))
                .fredag(Duration.ofMinutes(30))
                .build();

        Tilsynsordning tilsynsordning = new Tilsynsordning(TilsynsordningSvar.JA, uke.getOpphold());

        assertThat(0).isEqualTo(tilsynsordning.getOpphold().size());
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

        Periode periode = new Periode(fraOgMed, tilOgMed);

        TilsynsordningUke uke = TilsynsordningUke
                .builder()
                .periode(periode)
                .mandag(mandagLengde)
                .tirsdag(tirsdagLengde)
                .onsdag(onsdagLengde)
                .torsdag(torsdagLengde)
                .fredag(fredagLengde)
                .build();

        Tilsynsordning tilsynsordning = new Tilsynsordning(TilsynsordningSvar.JA, uke.getOpphold());

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

        assertThat(expectedAntallUkerIPerioden).isEqualTo(tilsynsordning.getOpphold().size());

        AtomicInteger antallHeleUkerSjekket = new AtomicInteger();
        AtomicBoolean sisteUkeSjekket = new AtomicBoolean(false);

        tilsynsordning.getOpphold().forEach((p,opphold) -> {
            if (p.getTilOgMed().isEqual(tilOgMed)) {
                sisteUkeSjekket.set(true);
                assertThat(summertSisteUke).isEqualTo(opphold.getLengde());
            } else {
                assertThat(summertHeleUker).isEqualTo(opphold.getLengde());
                antallHeleUkerSjekket.getAndIncrement();
            }
        });

        assertThat(expectedAntallHeleUkerIPerioden).isEqualTo(antallHeleUkerSjekket.get());
        assertThat(sisteUkeSjekket.get()).isTrue();
    }
}
