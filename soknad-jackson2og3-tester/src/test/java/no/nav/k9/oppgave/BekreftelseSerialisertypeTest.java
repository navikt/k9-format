package no.nav.k9.oppgave;

import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.EndretPeriodeBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.EndretSluttdatoBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.EndretStartdatoBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.FjernetPeriodeBekreftelse;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.type.Periode;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Regresjonstest for PR #598: Sikrer at type-feltet ikke serialiseres dobbelt
 * og at deserialisering av eksisterende typer fungerer uten "Unrecognized field 'type'".
 */
class BekreftelseSerialisertypeTest {

    @Test
    void endretStartdato_roundtrip_uten_duplikat_type() {
        var original = new EndretStartdatoBekreftelse(
                UUID.fromString("00000000-0000-0000-0001-000000000001"),
                LocalDate.of(2026, 1, 1),
                false);

        String json = JsonUtils.toString(original);

        assertThat(antallForekomster(json, "\"type\""))
                .as("type-feltet skal kun forekomme én gang i JSON")
                .isEqualTo(1);

        var roundtrip = (EndretStartdatoBekreftelse) JsonUtils.fromString(json, Bekreftelse.class);
        assertThat(roundtrip.getNyStartdato()).isEqualTo(original.getNyStartdato());
        assertThat(roundtrip.getOppgaveReferanse()).isEqualTo(original.getOppgaveReferanse());
        assertThat(roundtrip.harUttalelse()).isEqualTo(original.harUttalelse());
    }

    @Test
    void endretSluttdato_roundtrip_uten_duplikat_type() {
        var original = new EndretSluttdatoBekreftelse(
                UUID.fromString("00000000-0000-0000-0002-000000000001"),
                LocalDate.of(2026, 6, 30),
                true);

        String json = JsonUtils.toString(original);

        assertThat(antallForekomster(json, "\"type\""))
                .as("type-feltet skal kun forekomme én gang i JSON")
                .isEqualTo(1);

        var roundtrip = (EndretSluttdatoBekreftelse) JsonUtils.fromString(json, Bekreftelse.class);
        assertThat(roundtrip.getNySluttdato()).isEqualTo(original.getNySluttdato());
        assertThat(roundtrip.getOppgaveReferanse()).isEqualTo(original.getOppgaveReferanse());
    }

    @Test
    void endretPeriode_roundtrip_uten_duplikat_type() {
        var original = new EndretPeriodeBekreftelse(
                UUID.fromString("00000000-0000-0000-0003-000000000001"),
                new Periode(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 30)),
                false);

        String json = JsonUtils.toString(original);

        assertThat(antallForekomster(json, "\"type\""))
                .as("type-feltet skal kun forekomme én gang i JSON")
                .isEqualTo(1);

        var roundtrip = (EndretPeriodeBekreftelse) JsonUtils.fromString(json, Bekreftelse.class);
        assertThat(roundtrip.getNyPeriode()).isEqualTo(original.getNyPeriode());
        assertThat(roundtrip.getOppgaveReferanse()).isEqualTo(original.getOppgaveReferanse());
    }

    @Test
    void fjernetPeriode_roundtrip_uten_duplikat_type() {
        var original = new FjernetPeriodeBekreftelse(
                UUID.fromString("00000000-0000-0000-0004-000000000001"),
                new Periode(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 3, 31)),
                false);

        String json = JsonUtils.toString(original);

        assertThat(antallForekomster(json, "\"type\""))
                .as("type-feltet skal kun forekomme én gang i JSON")
                .isEqualTo(1);

        var roundtrip = (FjernetPeriodeBekreftelse) JsonUtils.fromString(json, Bekreftelse.class);
        assertThat(roundtrip.getFjernetPeriode()).isEqualTo(original.getFjernetPeriode());
        assertThat(roundtrip.getOppgaveReferanse()).isEqualTo(original.getOppgaveReferanse());
    }

    @Test
    void deserialiser_json_med_ukjente_felter_feiler_ikke() {
        // Simulerer bakoverkompatibilitet: JSON med et ekstra ukjent felt i tillegg til type-diskriminator
        String json = """
                {
                  "type": "UNG_ENDRET_STARTDATO",
                  "oppgaveReferanse": "00000000-0000-0000-0001-000000000002",
                  "nyStartdato": "2026-01-01",
                  "harUttalelse": false,
                  "ukjentFremtidigFelt": "skalIgnoreres"
                }
                """;

        var bekreftelse = (EndretStartdatoBekreftelse) JsonUtils.fromString(json, Bekreftelse.class);
        assertThat(bekreftelse.getNyStartdato()).isEqualTo(LocalDate.of(2026, 1, 1));
        assertThat(bekreftelse.getOppgaveReferanse()).isEqualTo(UUID.fromString("00000000-0000-0000-0001-000000000002"));
    }

    private static int antallForekomster(String tekst, String søk) {
        int count = 0;
        int idx = 0;
        while ((idx = tekst.indexOf(søk, idx)) != -1) {
            count++;
            idx += søk.length();
        }
        return count;
    }
}

