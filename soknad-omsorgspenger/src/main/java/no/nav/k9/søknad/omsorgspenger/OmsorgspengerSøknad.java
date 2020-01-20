package no.nav.k9.søknad.omsorgspenger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.Barn;
import no.nav.k9.søknad.felles.Søker;
import no.nav.k9.søknad.felles.SøknadId;
import no.nav.k9.søknad.felles.Versjon;

import java.time.ZonedDateTime;

public class OmsorgspengerSøknad {

    public final SøknadId søknadId;

    public final Versjon versjon;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    public final Søker søker;

    public final Barn barn;

    @JsonCreator
    private OmsorgspengerSøknad(
            @JsonProperty("søknadId")
            SøknadId søknadId,
            @JsonProperty("versjon")
            Versjon versjon,
            @JsonProperty("mottattDato")
            ZonedDateTime mottattDato,
            @JsonProperty("søker")
            Søker søker,
            @JsonProperty("barn")
            Barn barn) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.barn = barn;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final static OmsorgspengerSøknadValidator validator = new OmsorgspengerSøknadValidator();
        private final static Versjon versjon = Versjon.of("0.0.1");

        private String json;
        private SøknadId søknadId;
        private ZonedDateTime mottattDato;
        private Søker søker;
        private Barn barn;

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
                    barn
            ) : JsonUtils.fromString(json, OmsorgspengerSøknad.class);
            validator.forsikreValidert(søknad);
            return søknad;
        }
    }
}
