package no.nav.k9.søknad.ytelse.ung;

import no.nav.k9.søknad.Søknad;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.ung.v1.Ungdomsytelse;

import java.time.ZonedDateTime;

public class SøknadEksempel {

    public static Søknad søknad(Ungdomsytelse ytelse) {
        return new Søknad(
                new SøknadId("1"),
                new Versjon("6.0.1"),
                ZonedDateTime.now(),
                new Søker(NorskIdentitetsnummer.of("22222222222")),
                ytelse
        );
    }
}
