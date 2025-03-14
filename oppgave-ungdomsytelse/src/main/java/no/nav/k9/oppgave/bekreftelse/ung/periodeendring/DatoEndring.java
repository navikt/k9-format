package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import no.nav.k9.oppgave.bekreftelse.ung.Uttalelse;

import java.time.LocalDate;

public interface DatoEndring extends Uttalelse {

    LocalDate getNyDato();

}
