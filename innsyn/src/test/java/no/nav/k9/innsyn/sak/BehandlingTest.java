package no.nav.k9.innsyn.sak;

import no.nav.k9.kodeverk.behandling.BehandlingStatus;
import no.nav.k9.kodeverk.behandling.aksjonspunkt.Ventekategori;
import no.nav.k9.kodeverk.behandling.aksjonspunkt.Venteårsak;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class BehandlingTest {

    @Test
    void skalRegneUtSaksbehandlingsfrist() {
        ZonedDateTime tidligsteMottattTidspunkt = LocalDate.of(2024, 1, 5).atStartOfDay(ZoneId.systemDefault());
        var behandling = lagBehandling(false, tidligsteMottattTidspunkt.plusDays(10), tidligsteMottattTidspunkt, tidligsteMottattTidspunkt.plusMonths(20));
        ZonedDateTime saksbehandlingsfrist = behandling.utledSaksbehandlingsfrist(null).get();
        assertThat(saksbehandlingsfrist).isEqualTo(tidligsteMottattTidspunkt.plusWeeks(8));
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

    private static Behandling lagBehandling(boolean erUtenlands, ZonedDateTime... søknadtidspunkter) {
        return new Behandling(BehandlingStatus.OPPRETTET,
                Arrays.stream(søknadtidspunkter).map(it -> new SøknadInfo(SøknadStatus.MOTTATT, "123", it)).collect(Collectors.toSet()),
                Set.of(new Aksjonspunkt(Ventekategori.AVVENTER_ANNET, Venteårsak.VENT_OPDT_INNTEKTSMELDING)),
                erUtenlands);
    }

}
