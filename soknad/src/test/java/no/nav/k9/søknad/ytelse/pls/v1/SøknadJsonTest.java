package no.nav.k9.søknad.ytelse.pls.v1;

import static no.nav.k9.søknad.ytelse.pls.v1.ValiderUtil.verifyHarFeil;
import static no.nav.k9.søknad.ytelse.pls.v1.ValiderUtil.verifyIngenFeil;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import no.nav.k9.søknad.felles.Feil;
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

    @Test
    public void søknadUtenPleietrengendeNorskIdent() {
        var søknad = SøknadJsonEksempel.søknadUtenPleietrengendNorskIdent();

        verifyIngenFeil(søknad);

        PleipengerLivetsSluttfase ytelse = søknad.getYtelse();

        assertThat(ytelse.getPleietrengende().getFødselsdato()).isEqualTo("2000-01-01");
    }

    @Test
    public void søknadUtenPleietrengendeInfo() {
        var søknad = SøknadJsonEksempel.søknadUtenPleietrengendeInfo();

        List<Feil> feil = verifyHarFeil(søknad);

        assertThat(feil.size()).isEqualTo(1);
        Feil pleietrengendeFeil = feil.get(0);
        assertThat(pleietrengendeFeil.getFelt()).isEqualTo("ytelse.pleietrengende.ok");
        assertThat(pleietrengendeFeil.getFeilkode()).isEqualTo("påkrevd");
        assertThat(pleietrengendeFeil.getFeilmelding()).isEqualTo("norskIdentitetsnummer eller fødselsdato må være satt");
    }
}
