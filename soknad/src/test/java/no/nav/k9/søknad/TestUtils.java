package no.nav.k9.søknad;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;
import no.nav.k9.søknad.felles.type.Periode;

public class TestUtils {

    public static String testTekst() {
        return "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    }

    public static LocalDate mandagenFør(LocalDate d) {
        return d.minusDays(d.getDayOfWeek().getValue() - 1);
    }

    public static String okOrgnummerString() {
        return "999999999";
    }

    public static Organisasjonsnummer okOrgnummer() {
        return Organisasjonsnummer.of(okOrgnummerString());
    }

    public static String okPersonnummer() {
        return "33333333333";
    }

    public static NorskIdentitetsnummer okNorskIdentitetsnummer() {
        return NorskIdentitetsnummer.of("33333333333");
    }
    public static NorskIdentitetsnummer okNorskIdentitetsnummerBarn() {
        return NorskIdentitetsnummer.of("44444444444");
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
                .withFailMessage("Finner ikke felt: " + felt + " og feilkode: " + feilkode)
                .isNotEmpty();
    }

    public static void feilInneholder(List<Feil> feil, String felt, String feilkode, String feilmelding) {
        assertThat(feil
                .stream()
                .filter(f -> f.getFeilkode().equals(feilkode) && f.getFelt().equals(felt) && f.getFeilmelding().equals(feilmelding))
                .collect(Collectors.toList()))
                .withFailMessage("Finner ikke felt og feilkode: " + felt + ", " + feilkode + ", " + feilmelding)
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

    public static String periodeString(int i) {
        return "[" + i + "]";
    }

    public static String periodeString(Periode periode) {
        return "['" + periode + "']";
    }

}
