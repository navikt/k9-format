package no.nav.k9.søknad.ytelse.psb;

import java.time.ZonedDateTime;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;

public class SøknadEksempel {

    public static Søknad søknad(PleiepengerSyktBarn ytelse) {
        return new Søknad(
                new SøknadId("1"),
                new Versjon("5.4.8"),
                ZonedDateTime.now(),
                new Søker(NorskIdentitetsnummer.of("22222222222")),
                ytelse
        );
    }


    public static Søknad søknadMedArbeidstid(Periode søknadsperiode) {
        return søknad(YtelseEksempel.ytelseMedSøknadsperideOgArbeidstid(søknadsperiode));
    }

    public static Søknad minimumSøknad(Periode søknadsperiode) {
        return søknad(YtelseEksempel.minimumYtelseMedSøknadsperiode(søknadsperiode));
    }
}
