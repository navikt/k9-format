package no.nav.k9.innsyn.sak;

import no.nav.k9.innsyn.InnsynHendelse;
import no.nav.k9.kodeverk.behandling.BehandlingStatus;
import no.nav.k9.kodeverk.behandling.FagsakYtelseType;
import no.nav.k9.kodeverk.behandling.aksjonspunkt.Ventekategori;
import no.nav.k9.kodeverk.behandling.aksjonspunkt.Venteårsak;
import no.nav.k9.sak.typer.AktørId;
import no.nav.k9.sak.typer.Saksnummer;
import no.nav.k9.søknad.JsonUtils;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SaksinnholdTest {

    @Test
    void kanLageOgLeseSaksinnholdHendelse() {
        final Saksinnhold saksinnhold = lagSaksinnhold();
        final InnsynHendelse<Saksinnhold> hendelse = new InnsynHendelse<>(ZonedDateTime.now(), saksinnhold);
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
                     "type": "SAK_INNHOLD",
                     "saksnummer": "ABC123",
                     "søkerAktørId": "11111111111",
                     "pleietrengendeAktørId": "22222222222",
                     "ytelseType": "PSB",
                     "behandlinger": [
                       {
                         "status": "OPPRE",
                         "erUtenlands": "false",
                         "søknader": [
                           {
                             "status": "MOTTATT",
                             "søknadId": "f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a",
                             "mottattTidspunkt": "2021-06-01T12:00:00.000Z"
                           }
                         ],
                         "aksjonspunkter": [
                           {
                             "ventekategori": "AVVENTER_ANNET",
                             "venteårsak": "VENT_OPDT_INNTEKTSMELDING"
                           }
                         ]
                       }
                     ]
                   }
                 }
                """;

        final var hendelse = JsonUtils.fromString(jsonString, InnsynHendelse.class);
        final var saksinnhold = (Saksinnhold) hendelse.getData();

        assertThat(saksinnhold.saksnummer().getVerdi()).isEqualTo("ABC123");
        assertThat(saksinnhold.søkerAktørId().getId()).isEqualTo("11111111111");
        assertThat(saksinnhold.pleietrengendeAktørId().getId()).isEqualTo("22222222222");

        // behandlinger
        Set<Behandling> behandlinger = saksinnhold.behandlinger();
        assertThat(behandlinger).hasSize(1);
        Behandling behandling = behandlinger.stream().findFirst().get();
        assertThat(behandling.status()).isEqualTo(BehandlingStatus.OPPRETTET);

        // Søknader
        Set<SøknadInfo> søknader = behandling.søknader();
        assertThat(søknader).hasSize(1);
        SøknadInfo søknadInfo = søknader.stream().findFirst().get();
        assertThat(søknadInfo.status()).isEqualTo(SøknadStatus.MOTTATT);
        assertThat(søknadInfo.søknadId()).isEqualTo("f1b3f3c3-0b1a-4e4a-9b1a-3c3f3b1a4e4a");
        assertThat(søknadInfo.mottattTidspunkt()).isEqualTo(ZonedDateTime.parse("2021-06-01T12:00:00.000Z"));

        // Aksjonspunkter
        Set<Aksjonspunkt> aksjonspunkter = behandling.aksjonspunkter();
        assertThat(aksjonspunkter).hasSize(1);
        Aksjonspunkt aksjonspunkt = aksjonspunkter.stream().findFirst().get();
        assertThat(aksjonspunkt.ventekategori()).isEqualTo(Ventekategori.AVVENTER_ANNET);
        assertThat(aksjonspunkt.venteårsak()).isEqualTo(Venteårsak.VENT_OPDT_INNTEKTSMELDING);
    }

    private Saksinnhold lagSaksinnhold() {
        Set<SøknadInfo> søknader = Set.of(
                new SøknadInfo(SøknadStatus.MOTTATT, UUID.randomUUID().toString(), ZonedDateTime.now())
        );

        Set<Aksjonspunkt> aksjonspunkter = Set.of(
                new Aksjonspunkt(Ventekategori.AVVENTER_SØKER, Venteårsak.LEGEERKLÆRING)
        );

        Set<Behandling> behandlinger = Set.of(new Behandling(
                BehandlingStatus.OPPRETTET,
                søknader,
                aksjonspunkter,
                false
        ));

        String saksnummer = "ABC123";
        String søkerAktørId = "11111111111";
        String pleietrengendeAktørId = "22222222222";

        return new Saksinnhold(
                new Saksnummer(saksnummer),
                new AktørId(søkerAktørId),
                new AktørId(pleietrengendeAktørId),
                behandlinger,
                FagsakYtelseType.PLEIEPENGER_SYKT_BARN
        );
    }
}
