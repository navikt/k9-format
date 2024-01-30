package no.nav.k9.innsyn.sak;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.innsyn.InnsynHendelseData;

import java.util.Set;

import static no.nav.k9.innsyn.utils.RegexUtils.AKTØR_ID_REGEXP;
import static no.nav.k9.innsyn.utils.RegexUtils.SAKSNUMMER_REGEXP;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(InnsynHendelseData.SAK_INNHOLD)
public record Saksinnhold(
        @JsonProperty(value = "saksnummer", required = true)
        @Valid
        @NotNull
        @Pattern(regexp = SAKSNUMMER_REGEXP, message = "Saksnummer [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
        String saksnummer, // NOSONAR

        @JsonProperty(value = "søkerAktørId", required = true)
        @Valid
        @NotNull
        @Size(max = 20)
        @Pattern(regexp = AKTØR_ID_REGEXP, message = "søkerAktørId [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
        String søkerAktørId,

        @JsonProperty(value = "pleietrengendeAktørId", required = true)
        @Valid
        @NotNull
        @Size(max = 20)
        @Pattern(regexp = AKTØR_ID_REGEXP, message = "pleietrengendeAktørId [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
        String pleietrengendeAktørId,

        @JsonProperty(value = "behandlinger", required = true)
        @Valid
        @NotNull
        Set<Behandling> behandlinger,

        //TODO bør erstattes av kodeverk fra k9-sak
        @JsonProperty(value = "ytelseType", required = true)
        @Valid
        @NotNull
        String ytelseType

) implements InnsynHendelseData {

        @JsonCreator
        public Saksinnhold {
        }
}