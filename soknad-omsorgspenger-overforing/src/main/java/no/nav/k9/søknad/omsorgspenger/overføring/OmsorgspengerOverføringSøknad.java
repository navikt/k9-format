package no.nav.k9.søknad.omsorgspenger.overføring;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.Innsending;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.SøknadId;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class OmsorgspengerOverføringSøknad implements Innsending {

    @JsonProperty(value = "søknadId", required = true)
    @Valid
    @NotNull
    public final SøknadId søknadId;

    @JsonProperty(value = "versjon", required = true)
    @Valid
    @NotNull
    public final Versjon versjon;

    @JsonProperty(value = "mottattDato", required = true)
    @Valid
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    public final ZonedDateTime mottattDato;

    @JsonProperty(value = "søker", required = true)
    @Valid
    @NotNull
    public final Søker søker;

    @JsonProperty(value = "mottaker", required = true)
    @Valid
    @NotNull
    public final Mottaker mottaker;

    public final List<Barn> barn;

    @JsonCreator
    private OmsorgspengerOverføringSøknad(
            @JsonProperty(value="søknadId")
            SøknadId søknadId,
            @JsonProperty(value="versjon")
            Versjon versjon,
            @JsonProperty(value="mottattDato")
            ZonedDateTime mottattDato,
            @JsonProperty(value="søker")
            Søker søker,
            @JsonProperty(value="mottaker")
            Mottaker mottaker,
            @JsonProperty(value="barn")
            List<Barn> barn) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.mottaker = mottaker;
        this.barn = (barn == null) ? List.of() : barn;
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
    public Versjon getVersjon() {
        return versjon;
    }

    @Override
    public SøknadId getSøknadId() {
        return søknadId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class SerDes {
        private SerDes() {}
        public static String serialize(OmsorgspengerOverføringSøknad søknad) {
            return JsonUtils.toString(søknad);
        }
        public static OmsorgspengerOverføringSøknad deserialize(String søknad) {
            return JsonUtils.fromString(søknad, OmsorgspengerOverføringSøknad.class);
        }
    }

    public static final class Builder {
        private final static OmsorgspengerOverføringSøknadValidator validator = new OmsorgspengerOverføringSøknadValidator();
        private final static Versjon versjon = Versjon.of("0.0.1");

        private String json;
        private SøknadId søknadId;
        private ZonedDateTime mottattDato;
        private Søker søker;
        private Mottaker mottaker;
        List<Barn> barn;

        private Builder() {
            this.barn = new ArrayList<>();
        }

        public Builder søknadId(SøknadId søknadId) {
            this.søknadId = søknadId;
            return this;
        }

        public Builder mottattDato(ZonedDateTime mottattDato) {
            this.mottattDato = mottattDato;
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

        public Builder søker(Søker søker) {
            this.søker = søker;
            return this;
        }

        public Builder mottaker(Mottaker mottaker) {
            this.mottaker = mottaker;
            return this;
        }

        public Builder json(String json) {
            this.json = json;
            return this;
        }

        public OmsorgspengerOverføringSøknad build() {
            OmsorgspengerOverføringSøknad søknad = (json == null) ? new OmsorgspengerOverføringSøknad(
                    søknadId,
                    versjon,
                    mottattDato,
                    søker,
                    mottaker,
                    barn
            ) : SerDes.deserialize(json);
            validator.forsikreValidert(søknad);
            return søknad;
        }
    }
}
