package no.nav.k9.søknad;

import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.SøknadId;

import java.time.ZonedDateTime;

public interface Innsending {

    ZonedDateTime getMottattDato();

    Versjon getVersjon();

    SøknadId getSøknadId();

    Søker getSøker();

}
