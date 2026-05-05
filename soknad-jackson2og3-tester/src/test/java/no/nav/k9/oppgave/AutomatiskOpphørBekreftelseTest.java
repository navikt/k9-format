package no.nav.k9.oppgave;

import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.opphor.AutomatiskOpphørBekreftelse;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AutomatiskOpphørBekreftelseTest {

    @Test
    void roundtrip_uten_valgfrie_felter() {
        var original = new AutomatiskOpphørBekreftelse(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                LocalDate.of(2026, 5, 12),
                true);

        String json = JsonUtils.toString(original);
        var roundtrip = (AutomatiskOpphørBekreftelse) JsonUtils.fromString(json, Bekreftelse.class);

        assertThat(roundtrip).isEqualTo(original);
        assertThat(roundtrip.getType()).isEqualTo(Bekreftelse.Type.UNG_AUTOMATISK_OPPHOR);
        assertThat(roundtrip.getOppgaveReferanse()).isEqualTo(original.oppgaveReferanse());
        assertThat(roundtrip.getSluttdato()).isEqualTo(original.sluttdato());
        assertThat(roundtrip.harUttalelse()).isTrue();
    }

    @Test
    void roundtrip_med_uttalelse_og_dataBruktTilUtledning() {
        var original = new AutomatiskOpphørBekreftelse(
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                LocalDate.of(2026, 5, 12),
                true)
                .medUttalelseFraBruker("Jeg er uenig i dette vedtaket");

        var medData = original.medDataBruktTilUtledning(
                new DataBruktTilUtledning()
                        .medHarBekreftetOpplysninger(true)
                        .medHarForståttRettigheterOgPlikter(true));

        String json = JsonUtils.toString(medData);
        var roundtrip = (AutomatiskOpphørBekreftelse) JsonUtils.fromString(json, Bekreftelse.class);

        assertThat(roundtrip.oppgaveReferanse()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        assertThat(roundtrip.sluttdato()).isEqualTo(LocalDate.of(2026, 5, 12));
        assertThat(roundtrip.harUttalelse()).isTrue();
        assertThat(roundtrip.getUttalelseFraBruker()).isEqualTo("Jeg er uenig i dette vedtaket");
        assertThat(roundtrip.getDataBruktTilUtledning()).isNotNull();
        assertThat(roundtrip.getDataBruktTilUtledning().getHarBekreftetOpplysninger()).isTrue();
    }

    @Test
    void json_inneholder_forventede_feltnavn() {
        var bekreftelse = new AutomatiskOpphørBekreftelse(
                UUID.fromString("00000000-0000-0000-0000-000000000003"),
                LocalDate.of(2026, 1, 31),
                false);

        String json = JsonUtils.toString(bekreftelse);

        assertThat(json).contains("\"oppgaveReferanse\"");
        assertThat(json).contains("\"sluttdato\"");
        assertThat(json).contains("\"harUttalelse\"");
    }

    @Test
    void med_metoder_gir_ny_instans_og_muterer_ikke_original() {
        var original = new AutomatiskOpphørBekreftelse(
                UUID.fromString("00000000-0000-0000-0000-000000000004"),
                LocalDate.of(2026, 6, 1),
                false);

        var medUttalelse = (AutomatiskOpphørBekreftelse) original.medUttalelseFraBruker("kommentar");
        var medData = (AutomatiskOpphørBekreftelse) original.medDataBruktTilUtledning(new DataBruktTilUtledning());

        assertThat(original.getUttalelseFraBruker()).isNull();
        assertThat(original.getDataBruktTilUtledning()).isNull();
        assertThat(medUttalelse.getUttalelseFraBruker()).isEqualTo("kommentar");
        assertThat(medData.getDataBruktTilUtledning()).isNotNull();
    }
}
