package no.nav.k9.oppgave.bekreftelse.ung;

import no.nav.k9.oppgave.bekreftelse.Bekreftelse;

import java.time.LocalDate;

public interface Uttalelse extends Bekreftelse {

    String getUttalelseFraBruker();

    boolean harBrukerGodtattEndringen();

}
