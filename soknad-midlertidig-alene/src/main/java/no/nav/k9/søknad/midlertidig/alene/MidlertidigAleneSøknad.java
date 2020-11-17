package no.nav.k9.søknad.midlertidig.alene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.SøknadId;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MidlertidigAleneSøknad {

    @JsonProperty(value = "søknadId", required = true)
    @Valid
    @NotNull
    public final SøknadId søknadId;

    @JsonProperty(value = "id", required = true)
    @Valid
    @NotNull
    public final String id;

    @JsonProperty(value = "versjon", required = true)
    @Valid
    @NotNull
    public final Versjon versjon;

    @JsonProperty(value = "arbeidssituasjon", required = true)
    @Valid
    @NotNull
    public final Arbeidssituasjon arbeidssituasjon;

    @JsonProperty(value = "mottattDato", required = true)
    @Valid
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    @JsonProperty(value = "søker", required = true)
    @Valid
    @NotNull
    public final Søker søker;

    @JsonProperty(value = "annenForelder", required = true)
    @Valid
    @NotNull
    public final AnnenForelder annenForelder;

    @JsonProperty(value = "antallBarn", required = true)
    @Valid
    @NotNull
    public final int antallBarn;

    @JsonProperty(value = "fødselsårBarn", required = true)
    @Valid
    @NotNull
    public final List<Integer> fødselsårBarn;

    @JsonProperty(value = "medlemskap", required = true)
    @Valid
    @NotNull
    public final Medlemskap medlemskap;

    @JsonCreator
    private MidlertidigAleneSøknad(
            @JsonProperty(value="søknadId")
            SøknadId søknadId,
            @JsonProperty(value="versjon")
            Versjon versjon,
            @JsonProperty(value="mottattDato")
            ZonedDateTime mottattDato,
            @JsonProperty(value="søker")
            Søker søker,
            @JsonProperty(value = "annenForelder")
            AnnenForelder annenForelder,
            @JsonProperty(value = "id")
            String id,
            @JsonProperty(value = "arbeidssituasjon")
            Arbeidssituasjon arbeidssituasjon,
            @JsonProperty(value = "antallBarn")
            int antallBarn,
            @JsonProperty(value = "fødselsårBarn")
            List<Integer> fødselsårBarn,
            @JsonProperty(value = "medlemskap")
            Medlemskap medlemskap) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.annenForelder = annenForelder;
        this.id = id;
        this.arbeidssituasjon = arbeidssituasjon;
        this.antallBarn = antallBarn;
        this.fødselsårBarn = fødselsårBarn;
        this.medlemskap = medlemskap;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class SerDes {
        private SerDes() {}
        public static String serialize(MidlertidigAleneSøknad søknad) {
            return JsonUtils.toString(søknad);
        }
        public static MidlertidigAleneSøknad deserialize(String søknad) {
            return JsonUtils.fromString(søknad, MidlertidigAleneSøknad.class);
        }
    }

    public static final class Builder {
        private final static MidlertidigAleneSøknadValidator validator = new MidlertidigAleneSøknadValidator();
        private final static Versjon versjon = Versjon.of("0.0.1");

        private String json;
        private SøknadId søknadId;
        private ZonedDateTime mottattDato;
        private Søker søker;
        private AnnenForelder annenForelder;
        private String id;
        private Arbeidssituasjon arbeidssituasjon;
        private int antallBarn;
        private List<Integer> fødselsårBarn;
        private Medlemskap medlemskap;

        private Builder() {}

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

        public Builder annenForelder(AnnenForelder annenForelder) {
            this.annenForelder = annenForelder;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder arbeidssituasjon(Arbeidssituasjon arbeidssituasjon) {
            this.arbeidssituasjon = arbeidssituasjon;
            return this;
        }

        public Builder antallBarn(int antallBarn) {
            this.antallBarn = antallBarn;
            return this;
        }

        public Builder fødselsårBarn(List<Integer> fødselsårBarn) {
            this.fødselsårBarn = fødselsårBarn;
            return this;
        }

        public Builder medlemskap(Medlemskap medlemskap) {
            this.medlemskap = medlemskap;
            return this;
        }

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public MidlertidigAleneSøknad build() {
            MidlertidigAleneSøknad søknad = (json == null) ? new MidlertidigAleneSøknad(
                    søknadId,
                    versjon,
                    mottattDato,
                    søker,
                    annenForelder,
                    id,
                    arbeidssituasjon,
                    antallBarn,
                    fødselsårBarn,
                    medlemskap
            ) : SerDes.deserialize(json);
            validator.forsikreValidert(søknad);
            return søknad;
        }
    }

}
