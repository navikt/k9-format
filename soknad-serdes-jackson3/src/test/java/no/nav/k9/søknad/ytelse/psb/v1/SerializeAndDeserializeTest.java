package no.nav.k9.søknad.ytelse.psb.v1;

import no.nav.k9.søknad.SøknadSerDes;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.psb.SøknadEksempel;
import no.nav.k9.søknad.ytelse.psb.YtelseEksempel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static no.nav.k9.søknad.ytelse.psb.v1.ValiderUtil.verifyIngenFeil;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        var serializedSøknad = SøknadSerDes.serialize(søknad);
        var deserilizedSøknad = SøknadSerDes.deserialize(serializedSøknad);

        verifyIngenFeil(deserilizedSøknad);
        assertEquals(SøknadSerDes.serialize(deserilizedSøknad), serializedSøknad);
    }
}
