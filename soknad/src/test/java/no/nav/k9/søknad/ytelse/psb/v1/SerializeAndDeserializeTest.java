package no.nav.k9.søknad.ytelse.psb.v1;

import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyIngenFeil;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.SøknadEksempel;
import no.nav.k9.søknad.ytelse.psb.YtelseEksempel;

class SerializeAndDeserializeTest {

    @Test
    public void komplettSøknadTest() {
        var søknadsperiode = new Periode(LocalDate.now(), LocalDate.now().plusWeeks(4));
        var utelands = new Periode(LocalDate.now().minusYears(1), LocalDate.now());
        var bosteder = new Periode(LocalDate.now().minusYears(1), LocalDate.now().plusWeeks(4));
        var lovbestemtferie = new Periode(LocalDate.now().plusWeeks(3), LocalDate.now().plusWeeks(4));

        var ytelse = YtelseEksempel.komplettYtelseMedSøknadsperiode(
                søknadsperiode, lovbestemtferie, utelands, bosteder);
        var søknad = SøknadEksempel.søknad(ytelse);
        verifyIngenFeil(søknad);

        var serializedSøknad = Søknad.SerDes.serialize(søknad);
        var deserilizedSøknad = Søknad.SerDes.deserialize(serializedSøknad);

        verifyIngenFeil(deserilizedSøknad);
        assertEquals(Søknad.SerDes.serialize(deserilizedSøknad), serializedSøknad);
    }
}
