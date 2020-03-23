package no.nav.k9.søknad.omsorgspenger.overføring;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.Søker;
import no.nav.k9.søknad.felles.SøknadId;
import no.nav.k9.søknad.felles.Versjon;

import java.time.ZonedDateTime;

public class OmsorgspengerOverføringSøknad {
    public final SøknadId søknadId;

    public final Versjon versjon;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    public final Søker søker;

    public final Mottaker mottaker;

    @JsonCreator
    private OmsorgspengerOverføringSøknad(
            @JsonProperty("søknadId")
            SøknadId søknadId,
            @JsonProperty("versjon")
            Versjon versjon,
            @JsonProperty("mottattDato")
            ZonedDateTime mottattDato,
            @JsonProperty("søker")
            Søker søker,
            @JsonProperty("mottaker")
            Mottaker mottaker) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.mottaker = mottaker;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class SerDes {
        private SerDes() {}
        public static String serialize(OmsorgspengerOverføringSøknad søknad) {
            return JsonUtils.toString(søknad);
        }
        public static OmsorgspengerOverføringSøknad deserialize(String søknad) {
            return JsonUtils.fromString(søknad, OmsorgspengerOverføringSøknad.class);
        }
    }

    public static final class Builder {
        private final static OmsorgspengerOverføringSøknadValidator validator = new OmsorgspengerOverføringSøknadValidator();
        private final static Versjon versjon = Versjon.of("0.0.1");

        private String json;
        private SøknadId søknadId;
        private ZonedDateTime mottattDato;
        private Søker søker;
        private Mottaker mottaker;

        private Builder() {}

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

        public Builder mottaker(Mottaker mottaker) {
            this.mottaker = mottaker;
            return this;
        }

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public OmsorgspengerOverføringSøknad build() {
            OmsorgspengerOverføringSøknad søknad = (json == null) ? new OmsorgspengerOverføringSøknad(
                    søknadId,
                    versjon,
                    mottattDato,
                    søker,
                    mottaker
            ) : SerDes.deserialize(json);
            validator.forsikreValidert(søknad);
            return søknad;
        }
    }
}
