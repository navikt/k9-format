package no.nav.k9.konstant;

import java.time.Period;

public class Konstant {
    private static final String SAKSBEHANDLINGSTID = "P8W";
    private static final String UTLAND_SAKSBEHANDLINGSTID = "P6M";

    public static final Period FORVENTET_SAKSBEHANDLINGSTID = Period.parse(SAKSBEHANDLINGSTID);
    public static final Period UTLAND_FORVENTET_SAKSBEHANDLINGSTID = Period.parse(UTLAND_SAKSBEHANDLINGSTID);
}
