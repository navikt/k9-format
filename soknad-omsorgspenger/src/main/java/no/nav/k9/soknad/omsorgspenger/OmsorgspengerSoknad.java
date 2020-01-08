package no.nav.k9.soknad.omsorgspenger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Barn;
import no.nav.k9.soknad.felles.Soker;
import no.nav.k9.soknad.felles.SoknadId;
import no.nav.k9.soknad.felles.Versjon;

import java.time.ZonedDateTime;

public class OmsorgspengerSoknad {

    public final SoknadId soknadId;

    public final Versjon versjon;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    public final Soker soker;

    public final Barn barn;

    @JsonCreator
    private OmsorgspengerSoknad(
            @JsonProperty("soknadId")
            SoknadId soknadId,
            @JsonProperty("versjon")
            Versjon versjon,
            @JsonProperty("mottattDato")
            ZonedDateTime mottattDato,
            @JsonProperty("soker")
            Soker soker,
            @JsonProperty("barn")
            Barn barn) {
        this.soknadId = soknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.soker = soker;
        this.barn = barn;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final static OmsorgspengerSoknadValidator validator = new OmsorgspengerSoknadValidator();
        private final static Versjon versjon = Versjon.of("0.0.1");

        private SoknadId soknadId;
        private ZonedDateTime mottattDato;
        private Soker soker;
        private Barn barn;

        private Builder() {}

        public Builder soknadId(SoknadId soknadId) {
            this.soknadId = soknadId;
            return this;
        }

        public Builder mottattDato(ZonedDateTime mottattDato) {
            this.mottattDato = mottattDato;
            return this;
        }

        public Builder soker(Soker soker) {
            this.soker = soker;
            return this;
        }

        public Builder barn(Barn barn) {
            this.barn = barn;
            return this;
        }

        public OmsorgspengerSoknad build() {
            OmsorgspengerSoknad soknad = new OmsorgspengerSoknad(
                    soknadId,
                    versjon,
                    mottattDato,
                    soker,
                    barn
            );
            validator.forsikreValidert(soknad);
            return soknad;
        }
    }
}
