package no.nav.k9.søknad.frisinn;

import com.fasterxml.jackson.annotation.*;
import no.nav.k9.søknad.Innsending;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Språk;
import no.nav.k9.søknad.felles.type.SøknadId;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class FrisinnSøknad implements Innsending {

    @JsonProperty(value = "søknadId")
    @Valid
    @NotNull
    private SøknadId søknadId;

    @JsonProperty(value = "versjon")
    @Valid
    @NotNull
    private Versjon versjon;

    @JsonProperty(value = "mottattDato")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    @Valid
    @NotNull
    private ZonedDateTime mottattDato;

    @JsonProperty(value = "språk")
    @Valid
    private Språk språk = Språk.NORSK_BOKMÅL;

    @JsonProperty(value = "søker")
    @Valid
    @NotNull
    private Søker søker;

    @JsonProperty(value = "søknadsperiode", required = true)
    @Valid
    @NotNull
    private Periode søknadsperiode;

    @JsonProperty(value = "inntekter", required = true)
    @Valid
    @NotNull
    private Inntekter inntekter;

    @JsonCreator
    private FrisinnSøknad(@JsonProperty(value = "søknadId", required = true) SøknadId søknadId,
                          @JsonProperty(value = "søknadsperiode", required = true) Periode søknadsperiode,
                          @JsonProperty(value = "versjon", required = true) Versjon versjon,
                          @JsonProperty(value = "mottattDato", required = true) ZonedDateTime mottattDato,
                          @JsonProperty(value = "søker", required = true) Søker søker,
                          @JsonProperty(value = "inntekter", required = true) Inntekter inntekter,
                          @JsonProperty(value = "språk") Språk språk) {
        this.søknadId = Objects.requireNonNull(søknadId, "søknadId");
        this.søknadsperiode = Objects.requireNonNull(søknadsperiode, "søknadsperiode");
        this.versjon = Objects.requireNonNull(versjon, "versjon");
        this.mottattDato = Objects.requireNonNull(mottattDato, "mottattDato");
        this.inntekter = Objects.requireNonNull(inntekter, "inntekter");
        this.språk = språk == null ? this.språk : språk;
        this.søker = søker;

        if (inntekter.getFrilanser() != null) {
            validerSøknadInntektPeriode("frilanser", inntekter.getFrilanser().getMaksSøknadsperiode());
        }
        if (inntekter.getSelvstendig() != null) {
            validerSøknadInntektPeriode("selvstendig", inntekter.getSelvstendig().getMaksSøknadsperiode());
        }
        if (inntekter.getArbeidstaker() != null) {
            var maks = inntekter.getArbeidstaker().getMaksSøknadsperiode();
            if (maks != null) {
                validerInnenforSøknadsperiode("arbeidstaker", maks);
            }
        }
    }

    private void validerSøknadInntektPeriode(String tekst, Periode inntektPeriode) {
        if (inntektPeriode==null) {
            throw new IllegalArgumentException("Mangler inntektperiode for " + tekst);
        }
        validerInnenforSøknadsperiode(tekst, inntektPeriode);
    }

    private void validerInnenforSøknadsperiode(String tekst, Periode inntektPeriode) {
        if (!søknadsperiode.inneholder(inntektPeriode)) {
            throw new IllegalArgumentException(
                    "Inntektperiode [" + inntektPeriode + "] må være innenfor søknadsperiode [" + søknadsperiode + " for " + tekst + "]");
        }
    }

    @Override
    public ZonedDateTime getMottattDato() {
        return mottattDato;
    }

    @Override
    public Versjon getVersjon() {
        return versjon;
    }

    @Override
    public SøknadId getSøknadId() {
        return søknadId;
    }

    public Språk getSpråk() {
        return språk;
    }

    @Override
    public Søker getSøker() {
        return søker;
    }

    public Inntekter getInntekter() {
        return inntekter;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
            "<søknadId=" + søknadId +
            ", versjon=" + versjon +
            ", mottattDato=" + mottattDato +
            ", søknadsperiode=" + søknadsperiode + ">";
    }

    public Periode getSøknadsperiode() {
        return søknadsperiode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class SerDes {
        private SerDes() {
        }

        public static String serialize(FrisinnSøknad søknad) {
            return JsonUtils.toString(søknad);
        }

        public static FrisinnSøknad deserialize(String søknad) {
            return JsonUtils.fromString(søknad, FrisinnSøknad.class);
        }
    }

    public static final class Builder {
        private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

        private static final Versjon versjon = Versjon.of("2.0.0");

        private SøknadId søknadId;

        private String json;
        private ZonedDateTime mottattDato;
        private Språk språk = Språk.NORSK_BOKMÅL;
        private Søker søker;
        private Periode søknadsperiode;
        private Inntekter inntekter;

        private Builder() {
        }

        public Builder søknadId(SøknadId søknadId) {
            this.søknadId = søknadId;
            return this;
        }

        public Builder mottattDato(ZonedDateTime mottattDato) {
            this.mottattDato = mottattDato;
            return this;
        }

        public Builder språk(Språk språk) {
            this.språk = språk;
            return this;
        }

        public Builder søknadsperiode(Periode periode) {
            this.søknadsperiode = periode;
            return this;
        }

        public Builder søknadsperiode(String iso8601) {
            return søknadsperiode(new Periode(iso8601));
        }

        public Builder søker(Søker søker) {
            this.søker = søker;
            return this;
        }

        public Builder inntekter(Inntekter inntekter) {
            this.inntekter = inntekter;
            return this;
        }

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public FrisinnSøknad build() {
            FrisinnSøknad søknad;
            if (json == null) {
                søknad = new FrisinnSøknad(
                    søknadId,
                    søknadsperiode,
                    versjon,
                    mottattDato,
                    søker,
                    inntekter,
                    språk);
            } else {
                søknad = SerDes.deserialize(json);
            }
            validerSøknad(søknad);
            return søknad;
        }

        private void validerSøknad(FrisinnSøknad søknad) {
            var constraints = VALIDATOR_FACTORY.getValidator().validate(søknad);
            if (constraints != null && !constraints.isEmpty()) {
                throw new IllegalArgumentException("Søknad har feil :" + constraints + ", søknad=" + søknad);
            }
        }

    }
}
