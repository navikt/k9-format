package no.nav.k9.innsyn.sak;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.kodeverk.behandling.aksjonspunkt.Ventekategori;
import no.nav.k9.kodeverk.behandling.aksjonspunkt.Venteårsak;

public record Aksjonspunkt(
        @JsonProperty("ventekategori") @Valid @NotNull Ventekategori ventekategori,
        @JsonProperty("venteårsak") @Valid @NotNull Venteårsak venteårsak
) {
}
