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

    public final Periode periode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    public final Språk språk;

    public final Søker søker;

    public final Barn barn;

    public final Map<Periode, Bosted> bosteder;

    public final Map<Periode, Utenlandsopphold> utenlandsopphold;

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
            @JsonProperty("bosteder")
            Map<Periode, Bosted> bosteder,
            @JsonProperty("utenlandsopphold")
            Map<Periode, Utenlandsopphold> utenlandsopphold,
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
        this.bosteder = (bosteder == null) ? emptyMap() : unmodifiableMap(bosteder);
        this.utenlandsopphold = (utenlandsopphold == null) ? emptyMap() : unmodifiableMap(utenlandsopphold);
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
        private Periode periode;
        private Språk språk;
        private Søker søker;
        private Barn barn;
        private Map<Periode, Utenlandsopphold> utenlandsopphold;
        private Map<Periode, Bosted> bosteder;
        private Beredskap beredskap;
        private Nattevåk nattevåk;
        private Tilsynsordning tilsynsordning;
        private Arbeid arbeid;

        private Builder() {
            this.utenlandsopphold = new HashMap<>();
            this.bosteder = new HashMap<>();
        }

        public Builder søknadId(SøknadId søknadId) {
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

        public Builder bosted(Periode periode, Bosted bosted) {
            this.bosteder.put(periode, bosted);
            return this;
        }

        public Builder bosteder(Map<Periode, Bosted> bosteder) {
            this.bosteder.putAll(bosteder);
            return this;
        }

        public Builder utenlandsopphold(Periode periode, Utenlandsopphold utenlandsopphold) {
            this.utenlandsopphold.put(periode, utenlandsopphold);
            return this;
        }

        public Builder utenlandsopphold(Map<Periode, Utenlandsopphold> utenlandsopphold) {
            this.utenlandsopphold.putAll(utenlandsopphold);
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
                    periode,
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
