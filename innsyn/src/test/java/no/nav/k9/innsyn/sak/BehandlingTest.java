package no.nav.k9.innsyn.sak;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.*;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import no.nav.k9.innsyn.InnsynHendelse;
import no.nav.k9.kodeverk.behandling.FagsakYtelseType;
import no.nav.k9.sak.typer.AktørId;
import no.nav.k9.sak.typer.Saksnummer;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.Kildesystem;

class BehandlingTest {

    @Test
    void kanLageOgLeseBehandlingInnholdHendelse() {
        final Behandling behandling = lagBehandling();
        final InnsynHendelse<Behandling> hendelse = new InnsynHendelse<>(ZonedDateTime.now(), behandling);
        final var json = JsonUtils.toString(hendelse);

        final var deserialisertHendelse = JsonUtils.fromString(json, InnsynHendelse.class);
        final var reserialisertJson = JsonUtils.toString(deserialisertHendelse);

        assertThat(json).isEqualTo(reserialisertJson);
    }

    @Test
    void deserialiserFraJsonString() {
        //language=JSON
        final var jsonString = """
                {
                  "oppdateringstidspunkt": "2021-06-01T12:00:00.000Z",
                  "data": {
                    "type": "BEHANDLING_INNHOLD",
                    "behandlingsId": "f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a",
                    "opprettetDato": "2024-02-13",
                    "fagsak": {
                      "saksnummer": "ABC123",
                      "søkerAktørId": "11111111111",
                      "pleietrengendeAktørId": "22222222222",
                      "ytelseType": "PSB"
                    },
                    "status": "OPPRETTET",
                    "erUtenlands": "false",
                    "søknader": [
                      {
                        "status": "MOTTATT",
                        "journalpostId": "f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a",
                        "mottattTidspunkt": "2021-06-01T12:00:00.000Z",
                        "kildesystem": "søknadsdialog"
                      }
                    ],
                    "aksjonspunkter": [
                      {
                        "venteårsak": "INNTEKTSMELDING"
                      }
                    ]
                  }
                }
                """;

        final var hendelse = JsonUtils.fromString(jsonString, InnsynHendelse.class);
        final var behandling = (Behandling) hendelse.getData();
        final Fagsak saksinnhold = behandling.fagsak();

        assertThat(saksinnhold.saksnummer().getVerdi()).isEqualTo("ABC123");
        assertThat(saksinnhold.søkerAktørId().getId()).isEqualTo("11111111111");
        assertThat(saksinnhold.pleietrengendeAktørId().getId()).isEqualTo("22222222222");

        // behandlinger
        assertThat(behandling.status()).isEqualTo(BehandlingStatus.OPPRETTET);
        assertThat(behandling.erUtenlands()).isFalse();
        assertThat(behandling.behandlingsId()).isEqualTo(UUID.fromString("f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a"));

        // Søknader
        Set<SøknadInfo> søknader = behandling.søknader();
        assertThat(søknader).hasSize(1);
        SøknadInfo søknadInfo = søknader.stream().findFirst().get();
        assertThat(søknadInfo.status()).isEqualTo(SøknadStatus.MOTTATT);
        assertThat(søknadInfo.kildesystem()).isEqualTo(Kildesystem.SØKNADSDIALOG);

        assertThat(søknadInfo.journalpostId()).isEqualTo("f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a");
        assertThat(søknadInfo.mottattTidspunkt()).isEqualTo(ZonedDateTime.parse("2021-06-01T12:00:00.000Z"));

        // Aksjonspunkter
        Set<Aksjonspunkt> aksjonspunkter = behandling.aksjonspunkter();
        assertThat(aksjonspunkter).hasSize(1);
        Aksjonspunkt aksjonspunkt = aksjonspunkter.stream().findFirst().get();
        assertThat(aksjonspunkt.venteårsak()).isEqualTo(Aksjonspunkt.Venteårsak.INNTEKTSMELDING);
    }

    @Test
    void skalRegneUtSaksbehandlingsfrist() {
        ZonedDateTime tidligsteMottattTidspunkt = LocalDate.of(2024, 1, 5).atStartOfDay(ZoneId.systemDefault());
        var behandling = lagBehandling(false, tidligsteMottattTidspunkt.plusDays(10), tidligsteMottattTidspunkt, tidligsteMottattTidspunkt.plusMonths(20));
        ZonedDateTime saksbehandlingsfrist = behandling.utledSaksbehandlingsfrist(null).get();
        assertThat(saksbehandlingsfrist).isEqualTo(tidligsteMottattTidspunkt.plusWeeks(6));
    }

    @Test
    void skalOverstureOgRegneUtSaksbehandlingsfrist() {
        ZonedDateTime tidligsteMottattTidspunkt = LocalDate.of(2024, 1, 5).atStartOfDay(ZoneId.systemDefault());
        var behandling = lagBehandling(false, tidligsteMottattTidspunkt.plusDays(10), tidligsteMottattTidspunkt, tidligsteMottattTidspunkt.plusMonths(20));
        ZonedDateTime saksbehandlingsfrist = behandling.utledSaksbehandlingsfrist(Duration.ofDays(5)).get();
        assertThat(saksbehandlingsfrist).isEqualTo(tidligsteMottattTidspunkt.plusDays(5));
    }

    @Test
    void skalIkkeRegneUtSaksbehandlingsfristForUtenlandsbehandling() {
        ZonedDateTime mottattidspunkt = LocalDate.of(2024, 1, 5).atStartOfDay(ZoneId.systemDefault());
        var behandling = lagBehandling(true, mottattidspunkt);
        var saksbehandlingsfrist = behandling.utledSaksbehandlingsfrist(null);
        assertThat(saksbehandlingsfrist).isEmpty();
    }

    private static Behandling lagBehandling() {
        return lagBehandling(false, ZonedDateTime.now());
    }

    private static Behandling lagBehandling(boolean erUtenlands, ZonedDateTime... søknadtidspunkter) {

        Set<SøknadInfo> søknader = Arrays.stream(søknadtidspunkter).map(it -> new SøknadInfo(SøknadStatus.MOTTATT, UUID.randomUUID().toString(), it, Kildesystem.SØKNADSDIALOG)).collect(Collectors.toSet());

        Set<Aksjonspunkt> aksjonspunkter = Set.of(
                new Aksjonspunkt(Aksjonspunkt.Venteårsak.MEDISINSK_DOKUMENTASJON)
        );

        String saksnummer = "ABC123";
        String søkerAktørId = "11111111111";
        String pleietrengendeAktørId = "22222222222";

        Fagsak saksinnhold = new Fagsak(
                new Saksnummer(saksnummer),
                new AktørId(søkerAktørId),
                new AktørId(pleietrengendeAktørId),
                FagsakYtelseType.PLEIEPENGER_SYKT_BARN
        );

        Behandling behandling = new Behandling(
                UUID.randomUUID(),
                LocalDate.now(),
                null,
                BehandlingStatus.OPPRETTET,
                søknader,
                aksjonspunkter,
                erUtenlands,
                saksinnhold

        );

        return behandling;
    }
}
