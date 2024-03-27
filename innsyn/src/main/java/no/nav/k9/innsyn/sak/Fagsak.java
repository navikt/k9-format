package no.nav.k9.innsyn.sak;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record Fagsak(
        @JsonProperty(value = "saksnummer", required = true)
        @Valid
        @NotNull
        Saksnummer saksnummer, // NOSONAR

        @JsonProperty(value = "søkerAktørId", required = true)
        @Valid
        AktørId søkerAktørId,

        @JsonProperty(value = "pleietrengendeAktørId")
        @Valid
        AktørId pleietrengendeAktørId,

        @JsonProperty(value = "ytelseType", required = true)
        @Valid
        @NotNull
        FagsakYtelseType ytelseType

) {}
