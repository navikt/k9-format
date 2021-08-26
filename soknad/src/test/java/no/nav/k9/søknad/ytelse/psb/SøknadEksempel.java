package no.nav.k9.søknad.ytelse.psb;

import java.time.ZonedDateTime;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;

class SøknadEksempel {

    public static Søknad komplettSøknad(Periode søknadsperiode) {
        return new Søknad(
                new SøknadId("1"),
                new Versjon("5.4.8"),
                ZonedDateTime.now(),
                new Søker(NorskIdentitetsnummer.of("11111111111")),
                YtelseEksempel.komplettYtelsePsb(søknadsperiode)
        );

    }
}
