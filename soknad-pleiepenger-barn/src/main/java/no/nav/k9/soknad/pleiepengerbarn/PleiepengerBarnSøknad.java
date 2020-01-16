package no.nav.k9.soknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.soknad.felles.*;

import java.time.ZonedDateTime;

public class PleiepengerBarnSøknad {

    public final SøknadId søknadId;

    public final Versjon versjon;

    public final Periode periode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    public final Språk språk;

    public final Søker søker;

    public final Barn barn;

    public final Utland utland;

    public final Beredskap beredskap;

    public final Nattevåk nattevåk;

    public final Tilsynsordning tilsynsordning;

    public final Arbeid arbeid;

    @JsonCreator
    private PleiepengerBarnSøknad(
            @JsonProperty("søknadId")
            SøknadId søknadId,
            @JsonProperty("versjon")
            Versjon versjon,
            @JsonProperty("periode")
            Periode periode,
            @JsonProperty("mottattDato")
            ZonedDateTime mottattDato,
            @JsonProperty("språk")
            Språk språk,
            @JsonProperty("søker")
            Søker søker,
            @JsonProperty("barn")
            Barn barn,
            @JsonProperty("utland")
            Utland utland,
            @JsonProperty("beredskap")
            Beredskap beredskap,
            @JsonProperty("nattevåk")
            Nattevåk nattevåk,
            @JsonProperty("tilsynsordning")
            Tilsynsordning tilsynsordning,
            @JsonProperty("arbeid")
            Arbeid arbeid) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.periode = periode;
        this.mottattDato = mottattDato;
        this.språk = språk;
        this.søker = søker;
        this.barn = barn;
        this.utland = utland;
        this.beredskap = beredskap;
        this.nattevåk = nattevåk;
        this.tilsynsordning = tilsynsordning;
        this.arbeid = arbeid;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private static final PleiepengerBarnSøknadValidator validator = new PleiepengerBarnSøknadValidator();
        private static final Versjon versjon = Versjon.of("0.0.1");

        private SøknadId søknadId;
        private ZonedDateTime mottattDato;
        private Periode periode;
        private Språk språk;
        private Søker søker;
        private Barn barn;
        private Utland utland;
        private Beredskap beredskap;
        private Nattevåk nattevåk;
        private Tilsynsordning tilsynsordning;
        private Arbeid arbeid;

        private Builder() {}

        public Builder soknadId(SøknadId søknadId) {
            this.søknadId = søknadId;
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

        public Builder språk(Språk språk) {
            this.språk = språk;
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

        public Builder utland(Utland utland) {
            this.utland = utland;
            return this;
        }

        public Builder beredskap(Beredskap beredskap) {
            this.beredskap = beredskap;
            return this;
        }

        public Builder nattevåk(Nattevåk nattevåk) {
            this.nattevåk = nattevåk;
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

        public PleiepengerBarnSøknad build() {
            PleiepengerBarnSøknad soknad = new PleiepengerBarnSøknad(
                    søknadId,
                    versjon,
                    periode,
                    mottattDato,
                    språk,
                    søker,
                    barn,
                    utland,
                    beredskap,
                    nattevåk,
                    tilsynsordning,
                    arbeid
            );
            validator.forsikreValidert(soknad);
            return soknad;
        }
    }
}
