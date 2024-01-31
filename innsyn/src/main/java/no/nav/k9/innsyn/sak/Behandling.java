package no.nav.k9.innsyn.sak;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.konstant.Konstant;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public record Behandling(
        @JsonProperty(value = "status", required = true)
        @Valid
        @NotNull
        BehandlingStatus status,

        @JsonProperty(value = "søknader", required = true)
        @Valid
        @NotNull
        Set<SøknadInfo> søknader,

        @JsonProperty(value = "aksjonspunkter", required = true)
        @Valid
        @NotNull
        Set<Aksjonspunkt> aksjonspunkter,

        @JsonProperty(value = "erUtenlands")
        boolean erUtenlands
) {
    public Optional<ZonedDateTime> utledSaksbehandlingsfrist(Duration overstyrSaksbehandlingstid) {
        if (erUtenlands) {
            return Optional.empty();
        }

        var tidligsteMottattDato = søknader.stream()
                .min(Comparator.comparing(SøknadInfo::mottattTidspunkt))
                .map(SøknadInfo::mottattTidspunkt);

        return tidligsteMottattDato.map(it -> {
            Duration saksbehandlingstid = overstyrSaksbehandlingstid != null ? overstyrSaksbehandlingstid : Konstant.FORVENTET_SAKSBEHANDLINGSTID;
            return it.plus(saksbehandlingstid);
        });
    }
}

