package no.nav.k9.søknad.omsorgspenger;

import java.time.ZonedDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.Innsending;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerUtvidetRett;

/** byttet til {@link OmsorgspengerUtvidetRett} */
@Deprecated(forRemoval = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class OmsorgspengerSøknad implements Innsending {

    @JsonProperty(value = "søknadId", required = true)
    @Valid
    @NotNull
    public final SøknadId søknadId;

    @JsonProperty(value = "versjon", required = true)
    @Valid
    @NotNull
    public final Versjon versjon;

    @JsonAlias("mottatt")
    @JsonProperty(value = "mottattDato", required = true)
    @Valid
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    @JsonProperty(value = "søker", required = true)
    @Valid
    @NotNull
    public final Søker søker;

    @JsonAlias("fosterbarn")
    @JsonProperty(value = "barn")
    @Valid
    public final Barn barn;

    @JsonCreator
    private OmsorgspengerSøknad(
                                @JsonProperty("søknadId") SøknadId søknadId,
                                @JsonProperty("versjon") Versjon versjon,
                                @JsonProperty("mottattDato") ZonedDateTime mottattDato,
                                @JsonProperty("søker") Søker søker,
                                @JsonAlias("fosterbarn") @JsonProperty("barn") Barn barn) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.barn = barn;
    }

    @Override
    public ZonedDateTime getMottattDato() {
        return mottattDato;
    }

    @Override
    public Søker getSøker() {
        return søker;
    }

    @Override
    public Versjon getVersjon() {
        return versjon;
    }

    @Override
    public SøknadId getSøknadId() {
        return søknadId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class SerDes {
        private SerDes() {
        }

        public static String serialize(OmsorgspengerSøknad søknad) {
            return JsonUtils.toString(søknad);
        }

        public static OmsorgspengerSøknad deserialize(String søknad) {
            return JsonUtils.fromString(søknad, OmsorgspengerSøknad.class);
        }
    }

    public static final class Builder {
        private final static OmsorgspengerSøknadValidator validator = new OmsorgspengerSøknadValidator();
        private final static Versjon versjon = Versjon.of("0.0.1");

        private String json;
        private SøknadId søknadId;
        private ZonedDateTime mottattDato;
        private Søker søker;
        private Barn barn;

        private Builder() {
        }

        public Builder søknadId(SøknadId søknadId) {
            this.søknadId = søknadId;
            return this;
        }

        public Builder mottattDato(ZonedDateTime mottattDato) {
            this.mottattDato = mottattDato;
            return this;
        }

        public Builder søker(Søker søker) {
            this.søker = søker;
            return this;
        }

        public Builder barn(Barn barn) {
            this.barn = barn;
            return this;
        }

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public OmsorgspengerSøknad build() {
            OmsorgspengerSøknad søknad = (json == null) ? new OmsorgspengerSøknad(
                søknadId,
                versjon,
                mottattDato,
                søker,
                barn) : SerDes.deserialize(json);
            validator.forsikreValidert(søknad);
            return søknad;
        }
    }

}
