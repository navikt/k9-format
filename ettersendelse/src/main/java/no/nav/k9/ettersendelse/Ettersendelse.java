package no.nav.k9.ettersendelse;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.Innsending;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.SøknadId;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Ettersendelse implements Innsending {
    public final SøknadId søknadId;

    public final Versjon versjon;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    public final Søker søker;

    public final Ytelse ytelse;

    @JsonCreator
    private Ettersendelse(
                          @JsonProperty("søknadId") SøknadId søknadId,
                          @JsonProperty("versjon") Versjon versjon,
                          @JsonProperty("mottattDato") ZonedDateTime mottattDato,
                          @JsonProperty("søker") Søker søker,
                          @JsonProperty("ytelse") Ytelse ytelse) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.ytelse = ytelse;
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
    public SøknadId getSøknadId() {
        return søknadId;
    }

    @Override
    public Versjon getVersjon() {
        return versjon;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class SerDes {
        private SerDes() {
        }

        public static String serialize(Ettersendelse ettersendelse) {
            return JsonUtils.toString(ettersendelse);
        }

        public static Ettersendelse deserialize(String ettersendelse) {
            return JsonUtils.fromString(ettersendelse, Ettersendelse.class);
        }
    }

    public static final class Builder {
        private final static EttersendelseValidator validator = new EttersendelseValidator();
        private final static Versjon versjon = Versjon.of("0.0.1");

        private String json;
        private SøknadId søknadId;
        private ZonedDateTime mottattDato;
        private Søker søker;
        private Ytelse ytelse;

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

        public Builder ytelse(Ytelse ytelse) {
            this.ytelse = ytelse;
            return this;
        }

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public Ettersendelse build() {
            Ettersendelse ettetrsendelse = (json == null) ? new Ettersendelse(
                søknadId,
                versjon,
                mottattDato,
                søker,
                ytelse) : SerDes.deserialize(json);
            validator.forsikreValidert(ettetrsendelse);
            return ettetrsendelse;
        }
    }
}
