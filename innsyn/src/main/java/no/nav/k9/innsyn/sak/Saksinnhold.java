package no.nav.k9.innsyn.sak;

import static no.nav.k9.innsyn.utils.RegexUtils.AKTØR_ID_REGEXP;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.kodeverk.behandling.FagsakYtelseType;
import no.nav.k9.sak.typer.AktørId;
import no.nav.k9.sak.typer.Saksnummer;

public record Saksinnhold(
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
