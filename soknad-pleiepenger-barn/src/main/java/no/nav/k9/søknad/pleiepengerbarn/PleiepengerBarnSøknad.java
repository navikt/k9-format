package no.nav.k9.søknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class PleiepengerBarnSøknad {

    public final SøknadId søknadId;

    public final Versjon versjon;

    public final Map<Periode, SøknadsperiodeInfo> søknadsperioder;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    public final Språk språk;

    public final Søker søker;

    public final Barn barn;

    public final Bosteder bosteder;

    public final Utenlandsopphold utenlandsopphold;

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
            @JsonProperty("søknadsperioder")
            Map<Periode, SøknadsperiodeInfo> søknadsperioder,
            @JsonProperty("mottattDato")
            ZonedDateTime mottattDato,
            @JsonProperty("språk")
            Språk språk,
            @JsonProperty("søker")
            Søker søker,
            @JsonProperty("barn")
            Barn barn,
            @JsonProperty("bosteder")
            Bosteder bosteder,
            @JsonProperty("utenlandsopphold")
            Utenlandsopphold utenlandsopphold,
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
        this.søknadsperioder = (søknadsperioder == null) ? emptyMap() : unmodifiableMap(søknadsperioder);
        this.mottattDato = mottattDato;
        this.språk = språk;
        this.søker = søker;
        this.barn = barn;
        this.bosteder = bosteder;
        this.utenlandsopphold = utenlandsopphold;
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
        private static final Versjon versjon = Versjon.of("1.0.0");

        private String json;
        private SøknadId søknadId;
        private ZonedDateTime mottattDato;
        Map<Periode, SøknadsperiodeInfo> søknadsperioder;
        private Språk språk;
        private Søker søker;
        private Barn barn;
        private Utenlandsopphold utenlandsopphold;
        private Bosteder bosteder;
        private Beredskap beredskap;
        private Nattevåk nattevåk;
        private Tilsynsordning tilsynsordning;
        private Arbeid arbeid;

        private Builder() {
            this.søknadsperioder = new HashMap<>();
        }

        public Builder søknadId(SøknadId søknadId) {
            this.søknadId = søknadId;
            return this;
        }

        public Builder mottattDato(ZonedDateTime mottattDato) {
            this.mottattDato = mottattDato;
            return this;
        }


        public Builder søknadsperiode(Periode periode, SøknadsperiodeInfo søknadsperiodeInfo) {
            this.søknadsperioder.put(periode, søknadsperiodeInfo);
            return this;
        }

        public Builder søknadsperioder(Map<Periode, SøknadsperiodeInfo> søknadsperioder) {
            this.søknadsperioder.putAll(søknadsperioder);
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

        public Builder bosteder(Bosteder bosteder) {
            this.bosteder = bosteder;
            return this;
        }

        public Builder utenlandsopphold(Utenlandsopphold utenlandsopphold) {
            this.utenlandsopphold = utenlandsopphold;
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

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public PleiepengerBarnSøknad build() {
            PleiepengerBarnSøknad søknad = (json == null) ? new PleiepengerBarnSøknad(
                    søknadId,
                    versjon,
                    søknadsperioder,
                    mottattDato,
                    språk,
                    søker,
                    barn,
                    bosteder,
                    utenlandsopphold,
                    beredskap,
                    nattevåk,
                    tilsynsordning,
                    arbeid
            ) : JsonUtils.fromString(json, PleiepengerBarnSøknad.class);
            validator.forsikreValidert(søknad);
            return søknad;
        }
    }
}
