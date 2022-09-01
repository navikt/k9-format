package no.nav.k9.søknad.felles.fravær;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.Test;

class DelvisFraværTest {

    @Test
    void skal_regne_ut_delvis_fravær_normalisert_på_7h30m_dag_avrundet_opp_til_hel_time() {
        Duration standardDag = Duration.ofHours(7).plusMinutes(30);
        assertThat(new DelvisFravær(standardDag, standardDag).normalisertTilStandarddag()).isEqualByComparingTo(standardDag);
        assertThat(new DelvisFravær(standardDag, Duration.ofMinutes(30)).normalisertTilStandarddag()).isEqualByComparingTo(Duration.ofMinutes(30));

        Duration dobbelNormalarbeidstid = Duration.ofHours(15);
        assertThat(new DelvisFravær(dobbelNormalarbeidstid, dobbelNormalarbeidstid).normalisertTilStandarddag()).isEqualByComparingTo(standardDag);
        assertThat(new DelvisFravær(dobbelNormalarbeidstid, Duration.ofMinutes(30)).normalisertTilStandarddag()).isEqualByComparingTo(Duration.ofMinutes(30));
        assertThat(new DelvisFravær(dobbelNormalarbeidstid, Duration.ofHours(1)).normalisertTilStandarddag()).isEqualByComparingTo(Duration.ofMinutes(30));
        assertThat(new DelvisFravær(dobbelNormalarbeidstid, Duration.ofHours(2)).normalisertTilStandarddag()).isEqualByComparingTo(Duration.ofHours(1));
    }
}