package no.nav.k9.soknad.omsorgspenger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.ValideringsFeil;
import no.nav.k9.soknad.felles.Barn;
import no.nav.k9.soknad.felles.Feil;
import no.nav.k9.soknad.felles.Soker;

import java.time.ZonedDateTime;
import java.util.List;

public class OmsorgspengerSoknad {

    public final String versjon;

    public final ZonedDateTime mottattDato;

    public final Soker soker;

    public final Barn barn;

    @JsonCreator
    private OmsorgspengerSoknad(
            @JsonProperty("versjon")
            String versjon,
            @JsonProperty("mottatt_dato")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
            ZonedDateTime mottattDato,
            @JsonProperty("soker")
            Soker soker,
            @JsonProperty("barn")
            Barn barn) {
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
        private final static String versjon = "0.0.1";

        private ZonedDateTime mottattDato;
        private Soker soker;
        private Barn barn;

        private Builder() {}

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
                    versjon,
                    mottattDato,
                    soker,
                    barn
            );
            List<Feil> feil = validator.valider(soknad);
            if (feil.isEmpty()) return soknad;
            throw new ValideringsFeil(feil);
        }
    }
}
