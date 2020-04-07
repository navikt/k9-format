package no.nav.k9.søknad.pleiepengerbarn;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.Periode;
import no.nav.k9.søknad.felles.Språk;
import no.nav.k9.søknad.felles.Søker;
import no.nav.k9.søknad.felles.SøknadId;
import no.nav.k9.søknad.felles.Versjon;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class FrisinnSøknad {

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

    /** dato inntektstap startet. Påkrevd kun første søknad. */
    @JsonInclude(value = Include.NON_EMPTY)
    @JsonProperty(value = "inntektstapStartet")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo")
    @Valid
    private LocalDate inntektstapStartet;

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
                          @JsonProperty(value = "inntektstapStartet", required = true) LocalDate inntekstapStartet,
                          @JsonProperty(value = "versjon", required = true) Versjon versjon,
                          @JsonProperty(value = "mottattDato", required = true) ZonedDateTime mottattDato,
                          @JsonProperty(value = "søker", required = true) Søker søker,
                          @JsonProperty(value = "inntekter", required = true) Inntekter inntekter,
                          @JsonProperty(value = "språk") Språk språk) {
        this.inntektstapStartet = inntekstapStartet;
        this.søknadId = Objects.requireNonNull(søknadId, "søknadId");
        this.søknadsperiode = Objects.requireNonNull(søknadsperiode, "søknadsperiode");
        this.versjon = Objects.requireNonNull(versjon, "versjon");
        this.mottattDato = Objects.requireNonNull(mottattDato, "mottattDato");
        this.inntekter = Objects.requireNonNull(inntekter, "inntekter");
        this.språk = språk == null ? this.språk : språk;
        this.søker = søker;

        if (inntekstapStartet != null) {
            if (!inntektstapStartet.isBefore(mottattDato.toLocalDate())) {
                throw new IllegalArgumentException("Inntektstapstartet må være før mottattDato for søknad: " + inntekstapStartet + ">=" + mottattDato);
            }
            if (!inntektstapStartet.isBefore(søknadsperiode.getFraOgMed())) {
                throw new IllegalArgumentException("Inntektstap startdato [" + inntektstapStartet + "] må starte før søknadsperiode: " + søknadsperiode);
            }
            if (inntekter.getFrilanser() != null) {
                validerSøknadInntektPeriode(inntekter.getFrilanser().getMaksSøknadsperiode());
            }
            if (inntekter.getSelvstendig() != null) {
                validerSøknadInntektPeriode(inntekter.getSelvstendig().getMaksSøknadsperiode());
            }
        }
    }

    private void validerSøknadInntektPeriode(Periode inntektPeriode) {
        if (inntektstapStartet == null || inntektPeriode == null) {
            return;
        } else if (!inntektstapStartet.isBefore(inntektPeriode.getFraOgMed())) {
            throw new IllegalArgumentException("Inntektstap startdato [" + inntektstapStartet + "] må starte før inntekter periode: " + inntektPeriode);
        } else if (!søknadsperiode.inneholder(inntektPeriode)) {
            throw new IllegalArgumentException("Inntektperiode [" + inntektPeriode + "] må være innenfor søknadsperiode [" + søknadsperiode + "]");
        }

    }

    public LocalDate getInntektstapStartet() {
        return inntektstapStartet;
    }

    public SøknadId getSøknadId() {
        return søknadId;
    }

    public Versjon getVersjon() {
        return versjon;
    }

    public ZonedDateTime getMottattDato() {
        return mottattDato;
    }

    public Språk getSpråk() {
        return språk;
    }

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

        private static final Versjon versjon = Versjon.of("1.0.0");

        private SøknadId søknadId;

        private String json;
        private ZonedDateTime mottattDato;
        private Språk språk = Språk.NORSK_BOKMÅL;
        private Søker søker;
        private Periode søknadsperiode;
        private Inntekter inntekter;

        private LocalDate inntektstapStartet;

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

        public Builder inntektstapStartet(LocalDate dato) {
            this.inntektstapStartet = dato;
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
                    inntektstapStartet,
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
