package no.nav.k9.søknad.omsorgspenger.utbetaling;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.Barn;
import no.nav.k9.søknad.felles.Søker;
import no.nav.k9.søknad.felles.SøknadId;
import no.nav.k9.søknad.felles.Versjon;

import java.time.ZonedDateTime;

public class OmsorgspengerUtbetalingSøknad {
    public final SøknadId søknadId;

    public final Versjon versjon;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    public final Søker søker;

    @JsonCreator
    private OmsorgspengerUtbetalingSøknad(
            @JsonProperty("søknadId")
            SøknadId søknadId,
            @JsonProperty("versjon")
            Versjon versjon,
            @JsonProperty("mottattDato")
            ZonedDateTime mottattDato,
            @JsonProperty("søker")
            Søker søker) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class SerDes {
        private SerDes() {}
        public static String serialize(OmsorgspengerUtbetalingSøknad søknad) {
            return JsonUtils.toString(søknad);
        }
        public static OmsorgspengerUtbetalingSøknad deserialize(String søknad) {
            return JsonUtils.fromString(søknad, OmsorgspengerUtbetalingSøknad.class);
        }
    }

    public static final class Builder {
        private final static OmsorgspengerUtbetalingSøknadValidator validator = new OmsorgspengerUtbetalingSøknadValidator();
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

        public OmsorgspengerUtbetalingSøknad build() {
            OmsorgspengerUtbetalingSøknad søknad = (json == null) ? new OmsorgspengerUtbetalingSøknad(
                    søknadId,
                    versjon,
                    mottattDato,
                    søker
            ) : SerDes.deserialize(json);
            validator.forsikreValidert(søknad);
            return søknad;
        }
    }
}
