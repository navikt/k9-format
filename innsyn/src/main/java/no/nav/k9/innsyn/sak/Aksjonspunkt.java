package no.nav.k9.innsyn.sak;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record Aksjonspunkt(
        @JsonProperty("venteårsak") @Valid @NotNull Venteårsak venteårsak
) {

    public enum Venteårsak {
        INNTEKTSMELDING, MEDISINSK_DOKUMENTASJON
    }
}
