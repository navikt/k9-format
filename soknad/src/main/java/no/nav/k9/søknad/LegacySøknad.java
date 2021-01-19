package no.nav.k9.søknad;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.SøknadId;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Forenkler lesing av gamle søknadsformater (som omdistribueres til oppgave).
 * 
 * @deprecated kun for lesing av gamle formater (takler kun felter som benyttes i å opprette oppgave.
 */
@Deprecated(forRemoval = true, since = "5.0.2")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class LegacySøknad implements Innsending {

    @Valid
    @NotNull
    @JsonProperty(value = "søknadId", required = true)
    private SøknadId søknadId;

    @Valid
    @NotNull
    @JsonProperty(value = "versjon", required = true)
    private Versjon versjon;

    @Valid
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    @JsonProperty(value = "mottattDato", required = true)
    private ZonedDateTime mottattDato;

    @Valid
    @NotNull
    @JsonProperty(value = "søker", required = true)
    private Søker søker;

    @JsonInclude(value = Include.NON_EMPTY)
    @JsonFormat(with = { JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY })
    @JsonAlias(value = { "fosterbarn" })
    @JsonProperty(value = "barn")
    @Valid
    private List<Barn> barn;

    @Valid
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "perioder", required = false)
    private Map<Periode, Dummy> perioder;

    @JsonCreator
    public LegacySøknad(@JsonProperty(value = "søknadId", required = true) @Valid @NotNull SøknadId søknadId,
                        @JsonProperty(value = "versjon", required = true) @Valid @NotNull Versjon versjon,
                        @JsonProperty(value = "mottattDato", required = true) @Valid @NotNull ZonedDateTime mottattDato,
                        @JsonProperty(value = "søker", required = true) @Valid @NotNull Søker søker,
                        @JsonAlias(value = { "fosterbarn" }) @JsonProperty(value = "barn", required = true) List<Barn> barn,
                        @JsonProperty(value = "perioder", required = false) Map<Periode, Dummy> perioder) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.barn = barn;
        this.perioder = perioder;
    }

    @Override
    public SøknadId getSøknadId() {
        return søknadId;
    }

    public List<Barn> getBarn() {
        return barn;
    }

    @Override
    public Versjon getVersjon() {
        return versjon;
    }

    @Override
    public ZonedDateTime getMottattDato() {
        return mottattDato;
    }

    public List<Periode> getPerioder() {
        return perioder == null ? Collections.emptyList() : List.copyOf(perioder.keySet());
    }

    @Override
    public Søker getSøker() {
        return søker;
    }

    public static final class SerDes {
        private SerDes() {
        }

        public static String serialize(LegacySøknad søknad) {
            return JsonUtils.toString(søknad);
        }

        public static LegacySøknad deserialize(String søknad) {
            return JsonUtils.fromString(søknad, LegacySøknad.class);
        }
    }

    /** @deprecated legacy. */
    @Deprecated(forRemoval = true, since = "5.0.2")
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
    public static class Dummy {
    }

}
