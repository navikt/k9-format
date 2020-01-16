package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.*;

import java.time.ZonedDateTime;

public class PleiepengerBarnSoknad {

    public final SoknadId soknadId;

    public final Versjon versjon;

    public final Periode periode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    public final Spraak spraak;

    public final Soker soker;

    public final Barn barn;

    public final Utland utland;

    public final Beredskap beredskap;

    public final Nattevaak nattevaak;

    public final Tilsynsordning tilsynsordning;

    public final Arbeid arbeid;

    @JsonCreator
    private PleiepengerBarnSoknad(
            @JsonProperty("soknadId")
            SoknadId soknadId,
            @JsonProperty("versjon")
            Versjon versjon,
            @JsonProperty("periode")
            Periode periode,
            @JsonProperty("mottattDato")
            ZonedDateTime mottattDato,
            @JsonProperty("spraak")
            Spraak spraak,
            @JsonProperty("soker")
            Soker soker,
            @JsonProperty("barn")
            Barn barn,
            @JsonProperty("utland")
            Utland utland,
            @JsonProperty("beredskap")
            Beredskap beredskap,
            @JsonProperty("nattevaak")
            Nattevaak nattevaak,
            @JsonProperty("tilsynsordning")
            Tilsynsordning tilsynsordning,
            @JsonProperty("arbeid")
            Arbeid arbeid) {
        this.soknadId = soknadId;
        this.versjon = versjon;
        this.periode = periode;
        this.mottattDato = mottattDato;
        this.spraak = spraak;
        this.soker = soker;
        this.barn = barn;
        this.utland = utland;
        this.beredskap = beredskap;
        this.nattevaak = nattevaak;
        this.tilsynsordning = tilsynsordning;
        this.arbeid = arbeid;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private static final PleiepengerBarnSoknadValidator validator = new PleiepengerBarnSoknadValidator();
        private static final Versjon versjon = Versjon.of("0.0.1");

        private SoknadId soknadId;
        private ZonedDateTime mottattDato;
        private Periode periode;
        private Spraak spraak;
        private Soker soker;
        private Barn barn;
        private Utland utland;
        private Beredskap beredskap;
        private Nattevaak nattevaak;
        private Tilsynsordning tilsynsordning;
        private Arbeid arbeid;

        private Builder() {}

        public Builder soknadId(SoknadId soknadId) {
            this.soknadId = soknadId;
            return this;
        }

        public Builder mottattDato(ZonedDateTime mottattDato) {
            this.mottattDato = mottattDato;
            return this;
        }

        public Builder periode(Periode periode) {
            this.periode = periode;
            return this;
        }

        public Builder spraak(Spraak spraak) {
            this.spraak = spraak;
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

        public Builder utland(Utland utland) {
            this.utland = utland;
            return this;
        }

        public Builder beredskap(Beredskap beredskap) {
            this.beredskap = beredskap;
            return this;
        }

        public Builder nattevaak(Nattevaak nattevaak) {
            this.nattevaak = nattevaak;
            return this;
        }

        public Builder tilsynsordning(Tilsynsordning tilsynsordning) {
            this.tilsynsordning = tilsynsordning;
            return this;
        }

        public Builder arbeid(Arbeid arbeid) {
            this.arbeid = arbeid;
            return this;
        }

        public PleiepengerBarnSoknad build() {
            PleiepengerBarnSoknad soknad = new PleiepengerBarnSoknad(
                    soknadId,
                    versjon,
                    periode,
                    mottattDato,
                    spraak,
                    soker,
                    barn,
                    utland,
                    beredskap,
                    nattevaak,
                    tilsynsordning,
                    arbeid
            );
            validator.forsikreValidert(soknad);
            return soknad;
        }
    }
}
