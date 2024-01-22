package no.nav.k9.innsyn.sak;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
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

        @JsonProperty(value = "behandlingsfrist", required = true)
        @Valid
        @NotNull
        LocalDate behandlingsfrist
) {
}

