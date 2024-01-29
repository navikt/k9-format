package no.nav.k9.innsyn.sak;

import no.nav.k9.innsyn.InnsynHendelse;
import no.nav.k9.søknad.JsonUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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

    private Saksinnhold lagSaksinnhold() {
        Set<SøknadInfo> søknader = Set.of(
                new SøknadInfo(SøknadStatus.MOTTATT, UUID.randomUUID().toString(), ZonedDateTime.now())
        );

        Set<Aksjonspunkt> aksjonspunkter = Set.of(
                new Aksjonspunkt(Aksjonspunkt.Type.VENT_KOMPLETT_SØKNAD, Aksjonspunkt.Venteårsak.AVVENT_DOKUMTANSJON)
        );

        Set<Behandling> behandlinger = Set.of(new Behandling(
                BehandlingStatus.UNDER_BEHANDLING,
                søknader,
                aksjonspunkter,
                LocalDate.parse("2024-06-01")
        ));

        String saksnummer = "ABC123";
        String søkerAktørId = "11111111111";
        String pleietrengendeAktørId = "22222222222";

        return new Saksinnhold(
                saksnummer,
                søkerAktørId,
                pleietrengendeAktørId,
                behandlinger
        );
    }
}
