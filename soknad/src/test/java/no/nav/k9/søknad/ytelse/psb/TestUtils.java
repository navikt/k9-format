package no.nav.k9.søknad.ytelse.psb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import no.nav.k9.søknad.felles.Feil;

public class TestUtils {

    public static String testTekst() {
        return "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    }

    public static String okOrgnummer() {
        return "999999999";
    }

    public static String ikkeOkOrgnummer() {
        return "199999999";
    }

    public static String journalpostId() {
        return "sajhdasd83724234";
    }

    public static void feilInneholder(List<Feil> feil, String feilkode) {
        assertThat(feil
                .stream()
                .filter(f -> f.getFeilkode().equals(feilkode))
                .collect(Collectors.toList()))
                .withFailMessage("Finner ikke feilkode: " + feilkode)
                .isNotEmpty();
    }

    public static void feilInneholder(List<Feil> feil, String felt, String feilkode) {
        assertThat(feil
                .stream()
                .filter(f -> f.getFeilkode().equals(feilkode) && f.getFelt().equals(felt))
                .collect(Collectors.toList()))
                .withFailMessage("Finner ikke fetl og feilkode: " + felt + ", " + feilkode)
                .isNotEmpty();
    }

    public static void feilInneholder(List<Feil> feilList, Feil feil) {
        assertThat(feilList
                .stream()
                .filter(f ->
                    f.getFelt().equals(feil.getFelt()) &&
                    f.getFeilkode().equals(feil.getFeilkode()) &&
                    f.getFeilmelding().equals(feil.getFeilmelding())
                )
                .collect(Collectors.toList())
        )
                .withFailMessage("Finner ikke Feil: " + feil.toString())
                .isNotEmpty();
    }

}
