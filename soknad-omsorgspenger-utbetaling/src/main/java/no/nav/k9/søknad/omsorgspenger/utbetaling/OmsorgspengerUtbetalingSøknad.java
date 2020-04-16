package no.nav.k9.søknad.omsorgspenger.utbetaling;

import com.fasterxml.jackson.annotation.*;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.*;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class OmsorgspengerUtbetalingSøknad {

    @JsonProperty("søknadId")
    @Valid
    @NotNull
    public final SøknadId søknadId;

    @JsonProperty("versjon")
    @Valid
    @NotNull
    public final Versjon versjon;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    @JsonProperty("mottattDato")
    @Valid
    @NotNull
    public final ZonedDateTime mottattDato;

    @JsonProperty("søker")
    @Valid
    @NotNull
    public final Søker søker;

    @JsonProperty("barn")
    public final List<Barn> barn;

    @JsonProperty("selvstendingNæringsdrivende")
    @Valid
    public final List<SelvstendigNæringsdrivende> selvstendingNæringsdrivende;

    @JsonProperty("frilanser")
    @Valid
    public final Frilanser frilanser;

    @JsonCreator
    private OmsorgspengerUtbetalingSøknad(
            @JsonProperty("søknadId") SøknadId søknadId,
            @JsonProperty("versjon") Versjon versjon,
            @JsonProperty("mottattDato") ZonedDateTime mottattDato,
            @JsonProperty("søker") Søker søker,
            @JsonProperty("barn") List<Barn> barn,
            @JsonProperty("selvstendingNæringsdrivende") List<SelvstendigNæringsdrivende> selvstendingNæringsdrivende,
            @JsonProperty("frilanser") Frilanser frilanser) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.barn = (barn == null) ? List.of() : barn;
        this.selvstendingNæringsdrivende = selvstendingNæringsdrivende;
        this.frilanser = frilanser;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class SerDes {
        private SerDes() {
        }

        public static String serialize(OmsorgspengerUtbetalingSøknad søknad) {
            return JsonUtils.toString(søknad);
        }

        public static OmsorgspengerUtbetalingSøknad deserialize(String søknad) {
            return JsonUtils.fromString(søknad, OmsorgspengerUtbetalingSøknad.class);
        }
    }

    public static final class Builder {
        private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
        private final static OmsorgspengerUtbetalingSøknadValidator validator = new OmsorgspengerUtbetalingSøknadValidator();
        private final static Versjon versjon = Versjon.of("0.0.1");

        private String json;
        private SøknadId søknadId;
        private ZonedDateTime mottattDato;
        private Søker søker;
        List<Barn> barn;
        private List<SelvstendigNæringsdrivende> selvstendingeVirksomheter;
        private Frilanser frilanser;

        private Builder() {
            barn = new ArrayList<>();
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

        public Builder barn(Barn barn) {
            if (barn != null) this.barn.add(barn);
            return this;
        }

        public Builder barn(List<Barn> barn) {
            if (barn != null) this.barn.addAll(barn);
            return this;
        }

        public Builder selvstendigNæringsdrivende(List<SelvstendigNæringsdrivende> selvstendingeVirksomheter) {
            this.selvstendingeVirksomheter = selvstendingeVirksomheter;
            return this;
        }

        public Builder frilanser(Frilanser frilanser) {
            this.frilanser = frilanser;
            return this;
        }

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public OmsorgspengerUtbetalingSøknad build() {
            OmsorgspengerUtbetalingSøknad søknad;
            if (json == null) søknad = new OmsorgspengerUtbetalingSøknad(
                    søknadId,
                    versjon,
                    mottattDato,
                    søker,
                    barn,
                    selvstendingeVirksomheter,
                    frilanser);
            else søknad = SerDes.deserialize(json);
            validator.forsikreValidert(søknad);
            return søknad;
        }
    }
}
