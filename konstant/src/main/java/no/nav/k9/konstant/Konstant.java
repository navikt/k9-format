package no.nav.k9.konstant;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Konstant {
    private static final Long SAKSBEHANDLINGSTID_UKER = 8L;
    private static final Long ANTALL_UKEDAGER = 7L;
    /*
     * ChronoUnit.WEEKS har ikke en fast varighet fordi lengden av en uke kan variere for eksempel, pga.sommertid).
     * Bruker derfor ChronoUnit.DAYS for å være sikker på at vi får riktig varighet.
     */
    public static final Duration FORVENTET_SAKSBEHANDLINGSTID = Duration.of(SAKSBEHANDLINGSTID_UKER * ANTALL_UKEDAGER, ChronoUnit.DAYS);
}
