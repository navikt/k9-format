package no.nav.k9.søknad;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.felles.type.Språk;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.Ytelse;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Søknad implements Innsending {

    @Valid
    @NotNull
    @JsonProperty(value = "søknadId", required = true)
    private SøknadId søknadId;

    @Valid
    @NotNull
    @JsonProperty(value = "versjon", required = true)
    private Versjon versjon;

    @Valid
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    @JsonProperty(value = "mottattDato", required = true)
    private ZonedDateTime mottattDato;

    @Valid
    @NotNull
    @JsonProperty(value = "søker", required = true)
    private Søker søker;

    @Valid
    @JsonProperty(value = "språk", required = false)
    private Språk språk = Språk.NORSK_BOKMÅL;

    @Valid
    @NotNull
    @JsonProperty(value = "ytelse", required = true)
    private Ytelse ytelse;

    @JsonCreator
    public Søknad(@JsonProperty(value = "søknadId", required = true) @Valid @NotNull SøknadId søknadId,
                  @JsonProperty(value = "versjon", required = true) @Valid @NotNull Versjon versjon,
                  @JsonProperty(value = "mottattDato", required = true) @Valid @NotNull ZonedDateTime mottattDato,
                  @JsonProperty(value = "søker", required = true) @Valid @NotNull Søker søker,
                  @JsonProperty(value = "språk", required = false) @Valid Språk språk,
                  @JsonProperty(value = "ytelse", required = true) @Valid @NotNull Ytelse ytelse) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.ytelse = ytelse;
        this.språk = språk;
    }

    public Søknad(@JsonProperty(value = "søknadId", required = true) @Valid @NotNull SøknadId søknadId,
                  @JsonProperty(value = "versjon", required = true) @Valid @NotNull Versjon versjon,
                  @JsonProperty(value = "mottattDato", required = true) @Valid @NotNull ZonedDateTime mottattDato,
                  @JsonProperty(value = "søker", required = true) @Valid @NotNull Søker søker,
                  @JsonProperty(value = "ytelse", required = true) @Valid @NotNull Ytelse ytelse) {
        this(søknadId, versjon, mottattDato, søker, Språk.NORSK_BOKMÅL, ytelse);
    }

    @Override
    public SøknadId getSøknadId() {
        return søknadId;
    }

    public void setSøknadId(SøknadId søknadId) {
        this.søknadId = søknadId;
    }

    @Override
    public Versjon getVersjon() {
        return versjon;
    }

    public void setVersjon(Versjon versjon) {
        this.versjon = versjon;
    }

    @Override
    public ZonedDateTime getMottattDato() {
        return mottattDato;
    }

    public Språk getSpråk() {
        return språk;
    }

    public void setMottattDato(ZonedDateTime mottattDato) {
        this.mottattDato = mottattDato;
    }

    public Søknad medMottattDato(ZonedDateTime mottattDato) {
        this.mottattDato = mottattDato;
        return this;
    }

    public Søknad medSpråk(Språk språk) {
        this.språk = språk;
        return this;
    }

    public List<Person> getBerørtePersoner() {
        return ytelse == null ? Collections.emptyList() : ytelse.getBerørtePersoner();
    }

    @Override
    public Søker getSøker() {
        return søker;
    }

    public void setSøker(Søker søker) {
        this.søker = søker;
    }

    @SuppressWarnings("unchecked")
    public <Y extends Ytelse> Y getYtelse() {
        return (Y) ytelse;
    }

    public void setYtelse(Ytelse ytelse) {
        this.ytelse = ytelse;
    }

    public static final class SerDes {
        private SerDes() {
        }

        public static String serialize(Søknad søknad) {
            return JsonUtils.toString(søknad);
        }

        public static Søknad deserialize(String søknad) {
            return JsonUtils.fromString(søknad, Søknad.class);
        }

        public static Søknad deserialize(ObjectNode node) {
            try {
                return JsonUtils.getObjectMapper().treeToValue(node, Søknad.class);
            } catch (IOException e) {
                throw new IllegalArgumentException("Kunne ikke konvertere til Søknad.class", e);
            }
        }

    }
}
