package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import no.nav.k9.oppgave.bekreftelse.Bekreftelse;

import java.time.LocalDate;

public interface DatoEndring extends Bekreftelse {

    LocalDate getNyDato();

}
