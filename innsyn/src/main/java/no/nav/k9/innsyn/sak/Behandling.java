package no.nav.k9.innsyn.sak;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.innsyn.InnsynHendelseData;
import no.nav.k9.søknad.felles.DtoKonstanter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeName(InnsynHendelseData.BEHANDLING_INNHOLD)
public record Behandling(
        @JsonProperty(value = "behandlingsId", required = true)
        @Valid
        @NotNull
        UUID behandlingsId,

        @JsonProperty(value = "opprettetTidspunkt", required = true)
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoKonstanter.DATO_TID_FORMAT, timezone = DtoKonstanter.TIDSSONE)
        ZonedDateTime opprettetTidspunkt,

        @JsonInclude(value = JsonInclude.Include.NON_NULL)
        @JsonProperty(value = "avsluttetTidspunkt")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoKonstanter.DATO_TID_FORMAT, timezone = DtoKonstanter.TIDSSONE)
        ZonedDateTime avsluttetTidspunkt,

        @JsonInclude(value = JsonInclude.Include.NON_NULL)
        @JsonProperty(value = "resultat")
        BehandlingResultat resultat,

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
        boolean erUtenlands,

        @JsonProperty(value = "fagsak")
        Fagsak fagsak

) implements InnsynHendelseData {}

