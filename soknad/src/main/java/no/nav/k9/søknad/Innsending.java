package no.nav.k9.søknad;

import java.time.ZonedDateTime;

import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.SøknadId;

public interface Innsending {

    ZonedDateTime getMottattDato();

    Versjon getVersjon();

    SøknadId getSøknadId();

    Søker getSøker();

}
