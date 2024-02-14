package no.nav.k9.innsyn.sak;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.felles.DtoKonstanter;

public record Aksjonspunkt(
        @JsonProperty("venteårsak")
        @Valid
        @NotNull
        Venteårsak venteårsak,

        @JsonProperty("tidsfrist")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoKonstanter.DATO_TID_FORMAT, timezone = DtoKonstanter.TIDSSONE)
        ZonedDateTime tidsfrist
) {

    public enum Venteårsak {
        INNTEKTSMELDING, MEDISINSK_DOKUMENTASJON, FOR_TIDLIG_SOKNAD, MELDEKORT
    }
}
