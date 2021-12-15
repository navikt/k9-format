package no.nav.k9.søknad.ytelse.pls.v1;

import static no.nav.k9.søknad.ytelse.pls.v1.ValiderUtil.verifyIngenFeil;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.pls.SøknadJsonEksempel;

class SøknadJsonTest {

    @Test
    public void happycaseSøknad() {
        var søknad = SøknadJsonEksempel.komplettSøknad();

        verifyIngenFeil(søknad);

        PleipengerLivetsSluttfase ytelse = søknad.getYtelse();

        assertThat(ytelse.getPleietrengende().getPersonIdent().getVerdi()).isEqualTo("22111111111");
        assertThat(ytelse.getSøknadsperiode()).isEqualTo(new Periode(LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 13)));
        assertThat(ytelse.getBosteder().getPerioder().keySet()).containsExactly(new Periode(LocalDate.of(2018, 12, 30), LocalDate.of(2019, 10, 20)));

        //TODO PLS asserte alle felter som er satt (og ikke)
    }
}
