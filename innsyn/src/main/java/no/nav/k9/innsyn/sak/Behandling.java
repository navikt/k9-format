package no.nav.k9.innsyn.sak;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.innsyn.InnsynHendelseData;
import no.nav.k9.konstant.Konstant;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeName(InnsynHendelseData.BEHANDLING_INNHOLD)
public record Behandling(
        @JsonProperty(value = "behandlingsId", required = true)
        @Valid
        @NotNull
        UUID behandlingsId,

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

) implements InnsynHendelseData  {
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

