package no.nav.k9.søknad;

import java.time.ZonedDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.SøknadId;
import no.nav.k9.søknad.ytelse.Ytelse;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Søknad {

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
    @JsonProperty(value = "bosteder")
    private Bosteder bosteder;

    @Valid
    @JsonProperty(value = "utenlandsopphold")
    private Utenlandsopphold utenlandsopphold;

    @Valid
    @NotNull
    @JsonProperty(value = "ytelse", required = true)
    private Ytelse ytelse;

    @JsonCreator
    public Søknad(@JsonProperty(value = "søknadId", required = true) @Valid @NotNull SøknadId søknadId,
                  @JsonProperty(value = "versjon", required = true) @Valid @NotNull Versjon versjon,
                  @JsonProperty(value = "mottattDato", required = true) @Valid @NotNull ZonedDateTime mottattDato,
                  @JsonProperty(value = "søker", required = true) @Valid @NotNull Søker søker,
                  @JsonProperty(value = "bosteder") @Valid @NotNull Bosteder bosteder,
                  @JsonProperty(value = "utenlandsopphold") @Valid @NotNull Utenlandsopphold utenlandsopphold,
                  @JsonProperty(value = "ytelse", required = true) @Valid @NotNull Ytelse ytelse) {
        this.søknadId = søknadId;
        this.versjon = versjon;
        this.mottattDato = mottattDato;
        this.søker = søker;
        this.bosteder = bosteder;
        this.utenlandsopphold = utenlandsopphold;
        this.ytelse = ytelse;
    }

    public SøknadId getSøknadId() {
        return søknadId;
    }

    public void setSøknadId(SøknadId søknadId) {
        this.søknadId = søknadId;
    }

    public Versjon getVersjon() {
        return versjon;
    }

    public void setVersjon(Versjon versjon) {
        this.versjon = versjon;
    }

    public ZonedDateTime getMottattDato() {
        return mottattDato;
    }

    public void setMottattDato(ZonedDateTime mottattDato) {
        this.mottattDato = mottattDato;
    }

    public Bosteder getBosteder() {
        return bosteder;
    }

    public void setBosteder(Bosteder bosteder) {
        this.bosteder = bosteder;
    }

    public Utenlandsopphold getUtenlandsopphold() {
        return utenlandsopphold;
    }

    public void setUtenlandsopphold(Utenlandsopphold utenlandsopphold) {
        this.utenlandsopphold = utenlandsopphold;
    }

    public Søker getSøker() {
        return søker;
    }

    public void setSøker(Søker søker) {
        this.søker = søker;
    }

    public Ytelse getYtelse() {
        return ytelse;
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
    }
}
