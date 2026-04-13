package no.nav.k9.ettersendelse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.Innsending;
import no.nav.k9.søknad.felles.DtoKonstanter;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.SøknadId;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Ettersendelse implements Innsending {

    @JsonProperty(value="søknadId", required = true)
    @NotNull
    @Valid
    private final SøknadId søknadId;

    @JsonProperty(value="versjon")
    @Valid
    private final Versjon versjon;

    @JsonProperty(value="mottattDato")
    @Valid
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoKonstanter.DATO_TID_FORMAT, timezone = DtoKonstanter.TIDSSONE)
    private final ZonedDateTime mottattDato;

    @JsonProperty(value="søker", required = true)
    @Valid
    @NotNull
    private final Søker søker;

    @JsonProperty(value="ytelse", required = true)
    @Valid
    @NotNull
    private final Ytelse ytelse;

    @JsonProperty(value="pleietrengende")
    @Valid
    private final Pleietrengende pleietrengende;

    @JsonProperty(value="type") //TODO set required = true og @NotNull
    @Valid
    private final EttersendelseType type;

    @JsonCreator
    private Ettersendelse(
            @JsonProperty("søknadId") SøknadId søknadId,
            @JsonProperty("versjon") Versjon versjon,
            @JsonProperty("mottattDato") ZonedDateTime mottattDato,
            @JsonProperty("søker") Søker søker,
            @JsonProperty("ytelse") Ytelse ytelse,
            @JsonProperty("pleietrengende") Pleietrengende pleietrengende,
            @JsonProperty("type") EttersendelseType type) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.ytelse = ytelse;
        this.pleietrengende = pleietrengende;
        this.type = type;
    }

    @Override
    public ZonedDateTime getMottattDato() {
        return mottattDato;
    }

    @Override
    public Søker getSøker() {
        return søker;
    }

    @Override
    public SøknadId getSøknadId() {
        return søknadId;
    }

    @Override
    public Versjon getVersjon() {
        return versjon;
    }

    public Ytelse getYtelse() {
        return ytelse;
    }

    public Pleietrengende getPleietrengende() {
        return pleietrengende;
    }

    public EttersendelseType getType() {
        return type;
    }

    public static Builder builder() {
        return new Builder();
    }



    public static final class Builder {
        private final static EttersendelseValidator validator = new EttersendelseValidator();
        private final static Versjon versjon = Versjon.of("0.0.1");

        private SøknadId søknadId;
        private ZonedDateTime mottattDato;
        private Søker søker;
        private Ytelse ytelse;
        private Pleietrengende pleietrengende;
        private EttersendelseType type;

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

        public Builder søker(Søker søker) {
            this.søker = søker;
            return this;
        }

        public Builder ytelse(Ytelse ytelse) {
            this.ytelse = ytelse;
            return this;
        }

        public Builder pleietrengende(Pleietrengende pleietrengende) {
            this.pleietrengende = pleietrengende;
            return this;
        }

        public Builder type(EttersendelseType type) {
            this.type = type;
            return this;
        }

        public Ettersendelse build() {
            Ettersendelse ettersendelse = new Ettersendelse(
                    søknadId,
                    versjon,
                    mottattDato,
                    søker,
                    ytelse,
                    pleietrengende,
                    type);
            validator.forsikreValidert(ettersendelse);
            return ettersendelse;
        }
    }
}
