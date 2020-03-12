package no.nav.k9.søknad.pleiepengerbarn;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class PleiepengerBarnSøknad {

    public final SøknadId søknadId;

    public final Versjon versjon;

    public final Map<Periode, SøknadsperiodeInfo> perioder;

    @JsonIgnore
    public final LocalDate sisteDagISistePeriode;

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

    public final LovbestemtFerie lovbestemtFerie;

    @JsonCreator
    private PleiepengerBarnSøknad(
            @JsonProperty("søknadId")
            SøknadId søknadId,
            @JsonProperty("versjon")
            Versjon versjon,
            @JsonProperty("perioder")
            Map<Periode, SøknadsperiodeInfo> perioder,
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
            Arbeid arbeid,
            @JsonProperty("lovbestemtFerie")
            LovbestemtFerie lovbestemtFerie) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.perioder = (perioder == null) ? emptyMap() : unmodifiableMap(perioder);
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
        this.lovbestemtFerie = lovbestemtFerie;
        if (this.perioder.isEmpty()) {
            this.sisteDagISistePeriode = null;
        } else {
            this.sisteDagISistePeriode = Periode.Utils.sisteTilOgMedBlantLukkedePerioder(perioder);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class SerDes {
        private SerDes() {}
        public static String serialize(PleiepengerBarnSøknad søknad) {
            return JsonUtils.toString(søknad);
        }
        public static PleiepengerBarnSøknad deserialize(String søknad) {
            return JsonUtils.fromString(søknad, PleiepengerBarnSøknad.class);
        }
    }

    public static final class Utils {
        private Utils() {}

        public static boolean alleArbeidstakerPerioderInneholderJobberNormaltPerUke(
                PleiepengerBarnSøknad søknad) {
            return søknad.arbeid.arbeidstaker
                    .stream()
                    .noneMatch(arbeidstaker ->
                            arbeidstaker.perioder.values()
                                    .stream()
                                    .anyMatch(periodeInfo ->
                                            periodeInfo.jobberNormaltPerUke == null
                                    )
                    );
        }
    }

    public static final class Builder {
        private static final PleiepengerBarnSøknadValidator validator = new PleiepengerBarnSøknadValidator();
        private static final Versjon versjon = Versjon.of("1.1.0");

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
        private LovbestemtFerie lovbestemtFerie;

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

        public Builder lovbestemtFerie(LovbestemtFerie lovbestemtFerie) {
            this.lovbestemtFerie = lovbestemtFerie;
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
                    arbeid,
                    lovbestemtFerie
            ) : SerDes.deserialize(json);
            validator.forsikreValidert(søknad);
            return søknad;
        }
    }
}
