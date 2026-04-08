package no.nav.k9.innsyn.sak;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import no.nav.k9.innsyn.InnsynHendelse;
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
    void deserialiserFraGammelJsonString() {
        //language=JSON
        final var jsonString = """
                {
                  "oppdateringstidspunkt": "2021-06-01T12:00:00.000Z",
                  "data": {
                    "type": "BEHANDLING_INNHOLD",
                    "behandlingsId": "f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a",
                    "opprettetTidspunkt": "2024-02-13T12:00:00.000Z",
                    "fagsak": {
                      "saksnummer": "ABC123",
                      "søkerAktørId": "11111111111",
                      "pleietrengendeAktørId": "22222222222",
                      "ytelseType": "PSB"
                    },
                    "status": "OPPRETTET",
                    "erUtenlands": "false",
                    "avsluttetTidspunkt": "2024-02-14T12:00:00.000Z",
                    "behandlingResultat": "INNVILGET",
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
                        "venteårsak": "INNTEKTSMELDING",
                        "tidsfrist": "2024-02-15T12:00:00.000Z"
                      }
                    ]
                  }
                }
                """;

        final var hendelse = JsonUtils.fromString(jsonString, InnsynHendelse.class);
        final var behandling = (Behandling) hendelse.getData();
        final Fagsak saksinnhold = behandling.fagsak();

        assertThat(saksinnhold.saksnummer().verdi()).isEqualTo("ABC123");
        assertThat(saksinnhold.søkerAktørId().id()).isEqualTo("11111111111");
        assertThat(saksinnhold.pleietrengendeAktørId().id()).isEqualTo("22222222222");

        // behandlinger
        assertThat(behandling.status()).isEqualTo(BehandlingStatus.OPPRETTET);
        assertThat(behandling.erUtenlands()).isFalse();
        assertThat(behandling.opprettetTidspunkt()).isEqualTo(ZonedDateTime.parse("2024-02-13T12:00:00.000Z"));
        assertThat(behandling.avsluttetTidspunkt()).isEqualTo(ZonedDateTime.parse("2024-02-14T12:00:00.000Z"));
        assertThat(behandling.erUtenlands()).isFalse();
        assertThat(behandling.behandlingsId()).isEqualTo(UUID.fromString("f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a"));


        // Søknader
        Set<InnsendingInfo> innsendinger = behandling.innsendinger();
        assertThat(innsendinger).hasSize(1);
        InnsendingInfo innsendingInfo = innsendinger.stream().findFirst().get();
        assertThat(innsendingInfo.status()).isEqualTo(InnsendingStatus.MOTTATT);
        assertThat(innsendingInfo.kildesystem()).isEqualTo(Kildesystem.SØKNADSDIALOG);

        assertThat(innsendingInfo.journalpostId()).isEqualTo("f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a");
        assertThat(innsendingInfo.mottattTidspunkt()).isEqualTo(ZonedDateTime.parse("2021-06-01T12:00:00.000Z"));

        // Aksjonspunkter
        Set<Aksjonspunkt> aksjonspunkter = behandling.aksjonspunkter();
        assertThat(aksjonspunkter).hasSize(1);
        Aksjonspunkt aksjonspunkt = aksjonspunkter.stream().findFirst().get();
        assertThat(aksjonspunkt.venteårsak()).isEqualTo(Aksjonspunkt.Venteårsak.INNTEKTSMELDING);
        assertThat(aksjonspunkt.tidsfrist()).isEqualTo(ZonedDateTime.parse("2024-02-15T12:00:00.000Z"));

        String json = JsonUtils.toString(hendelse);
        assertThat(json).doesNotContain("kodeverk");
    }

    @Test
    void deserialiserFraNyJsonString() {
        //language=JSON
        final var jsonString = """
                {
                  "oppdateringstidspunkt": "2021-06-01T12:00:00.000Z",
                  "data": {
                    "type": "BEHANDLING_INNHOLD",
                    "behandlingsId": "f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a",
                    "opprettetTidspunkt": "2024-02-13T12:00:00.000Z",
                    "fagsak": {
                      "saksnummer": "ABC123",
                      "søkerAktørId": "11111111111",
                      "pleietrengendeAktørId": "22222222222",
                      "ytelseType": "PSB"
                    },
                    "status": "OPPRETTET",
                    "erUtenlands": "false",
                    "avsluttetTidspunkt": "2024-02-14T12:00:00.000Z",
                    "behandlingResultat": "INNVILGET",
                    "innsendinger": [
                      {
                        "status": "MOTTATT",
                        "journalpostId": "f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a",
                        "mottattTidspunkt": "2021-06-01T12:00:00.000Z",
                        "kildesystem": "søknadsdialog",
                        "type": "SØKNAD"
                      }
                    ],
                    "aksjonspunkter": [
                      {
                        "venteårsak": "INNTEKTSMELDING",
                        "tidsfrist": "2024-02-15T12:00:00.000Z"
                      }
                    ]
                  }
                }
                """;

        final var hendelse = JsonUtils.fromString(jsonString, InnsynHendelse.class);
        final var behandling = (Behandling) hendelse.getData();
        final Fagsak saksinnhold = behandling.fagsak();

        assertThat(saksinnhold.saksnummer().verdi()).isEqualTo("ABC123");
        assertThat(saksinnhold.søkerAktørId().id()).isEqualTo("11111111111");
        assertThat(saksinnhold.pleietrengendeAktørId().id()).isEqualTo("22222222222");

        // behandlinger
        assertThat(behandling.status()).isEqualTo(BehandlingStatus.OPPRETTET);
        assertThat(behandling.erUtenlands()).isFalse();
        assertThat(behandling.opprettetTidspunkt()).isEqualTo(ZonedDateTime.parse("2024-02-13T12:00:00.000Z"));
        assertThat(behandling.avsluttetTidspunkt()).isEqualTo(ZonedDateTime.parse("2024-02-14T12:00:00.000Z"));
        assertThat(behandling.erUtenlands()).isFalse();
        assertThat(behandling.behandlingsId()).isEqualTo(UUID.fromString("f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a"));


        // Søknader
        Set<InnsendingInfo> innsendinger = behandling.innsendinger();
        assertThat(innsendinger).hasSize(1);
        InnsendingInfo innsendingInfo = innsendinger.stream().findFirst().get();
        assertThat(innsendingInfo.status()).isEqualTo(InnsendingStatus.MOTTATT);
        assertThat(innsendingInfo.kildesystem()).isEqualTo(Kildesystem.SØKNADSDIALOG);
        assertThat(innsendingInfo.type()).isEqualTo(InnsendingType.SØKNAD);

        assertThat(innsendingInfo.journalpostId()).isEqualTo("f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a");
        assertThat(innsendingInfo.mottattTidspunkt()).isEqualTo(ZonedDateTime.parse("2021-06-01T12:00:00.000Z"));

        // Aksjonspunkter
        Set<Aksjonspunkt> aksjonspunkter = behandling.aksjonspunkter();
        assertThat(aksjonspunkter).hasSize(1);
        Aksjonspunkt aksjonspunkt = aksjonspunkter.stream().findFirst().get();
        assertThat(aksjonspunkt.venteårsak()).isEqualTo(Aksjonspunkt.Venteårsak.INNTEKTSMELDING);
        assertThat(aksjonspunkt.tidsfrist()).isEqualTo(ZonedDateTime.parse("2024-02-15T12:00:00.000Z"));

        String json = JsonUtils.toString(hendelse);
        assertThat(json).doesNotContain("kodeverk");
    }


    private static Behandling lagBehandling() {
        return lagBehandling(false, ZonedDateTime.now());
    }

    private static Behandling lagBehandling(boolean erUtenlands, ZonedDateTime... søknadtidspunkter) {
        final Set<InnsendingInfo> collect = Arrays.stream(søknadtidspunkter)
                .map(it -> lagSøknad(it, Kildesystem.SØKNADSDIALOG))
                .collect(Collectors.toSet());
        return lagBehandling(erUtenlands, collect);
    }

    private static InnsendingInfo lagSøknad(ZonedDateTime it, Kildesystem kilde) {
        return new InnsendingInfo(InnsendingStatus.MOTTATT, UUID.randomUUID().toString(), it, kilde, InnsendingType.SØKNAD);
    }

    private static Behandling lagBehandling(boolean erUtenlands, Set<InnsendingInfo> søknader) {

        Set<Aksjonspunkt> aksjonspunkter = Set.of(
                new Aksjonspunkt(Aksjonspunkt.Venteårsak.MEDISINSK_DOKUMENTASJON, ZonedDateTime.now())
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
                ZonedDateTime.now(),
                null,
                BehandlingResultat.INNVILGET,
                BehandlingStatus.OPPRETTET,
                søknader,
                aksjonspunkter,
                erUtenlands,
                saksinnhold

        );

        return behandling;
    }
}
