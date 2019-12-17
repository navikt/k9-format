package no.nav.k9.soknad.omsorgspenger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.Barn;
import no.nav.k9.soknad.felles.Soker;

import java.time.ZonedDateTime;
import java.util.Objects;

public class OmsorgspengerSoknad {

    private final String versjon = "0.1.1";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private ZonedDateTime mottattDato;

    @JsonProperty(required = true)
    private Soker soker;

    @JsonProperty(required = true)
    private Barn barn;

    private OmsorgspengerSoknad() {}

    private OmsorgspengerSoknad(
            ZonedDateTime mottattDato,
            Soker soker,
            Barn barn) {
        this.mottattDato = mottattDato;
        this.soker = soker;
        this.barn = barn;
    }

    public String getVersjon() {
        return versjon;
    }

    public ZonedDateTime getMottattDato() {
        return mottattDato;
    }

    public Soker getSoker() {
        return soker;
    }

    public Barn getBarn() {
        return barn;
    }

    private static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
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
            Objects.requireNonNull(mottattDato);
            Objects.requireNonNull(barn);
            Objects.requireNonNull(soker);
            return new OmsorgspengerSoknad(
                    mottattDato,
                    soker,
                    barn
            );
        }
    }
}
