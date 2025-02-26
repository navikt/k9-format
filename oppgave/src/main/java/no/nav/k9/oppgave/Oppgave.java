package no.nav.k9.oppgave;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.Innsending;
import no.nav.k9.søknad.JsonUtils;
import no.nav.k9.søknad.felles.DtoKonstanter;
import no.nav.k9.søknad.felles.Kildesystem;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.Språk;
import no.nav.k9.søknad.felles.type.SøknadId;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Oppgave implements Innsending {

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoKonstanter.DATO_TID_FORMAT, timezone = DtoKonstanter.TIDSSONE)
    @JsonProperty(value = "mottattDato", required = true)
    private ZonedDateTime mottattDato;

    @Valid
    @NotNull
    @JsonProperty(value = "søker", required = true)
    private Søker søker;

    @Valid
    @JsonProperty(value = "språk", required = false)
    private Språk språk = Språk.NORSK_BOKMÅL;

    @JsonManagedReference
    @Valid
    @NotNull
    @JsonProperty(value = "bekreftelse", required = true)
    private Bekreftelse bekreftelse;


    @Valid
    @JsonProperty(value = "kildesystem", required = false)
    private Kildesystem kildesystem;

    public Oppgave() {
        //
    }

    @JsonCreator
    public Oppgave(@JsonProperty(value = "søknadId", required = true) @Valid @NotNull SøknadId søknadId,
                   @JsonProperty(value = "versjon", required = true) @Valid @NotNull Versjon versjon,
                   @JsonProperty(value = "mottattDato", required = true) @Valid @NotNull ZonedDateTime mottattDato,
                   @JsonProperty(value = "søker", required = true) @Valid @NotNull Søker søker,
                   @JsonProperty(value = "språk", required = false) @Valid Språk språk,
                   @JsonProperty(value = "ytelse", required = true) @Valid @NotNull Bekreftelse bekreftelse) {
        this.søknadId = Objects.requireNonNull(søknadId, "søknadId");
        this.versjon = Objects.requireNonNull(versjon, "versjon");
        this.mottattDato = Objects.requireNonNull(mottattDato, "mottattDato");
        this.søker = Objects.requireNonNull(søker, "søker");
        this.bekreftelse = Objects.requireNonNull(bekreftelse, "bekreftelse");
        this.språk = språk;
    }

    public Oppgave(@JsonProperty(value = "søknadId", required = true) @Valid @NotNull SøknadId søknadId,
                   @JsonProperty(value = "versjon", required = true) @Valid @NotNull Versjon versjon,
                   @JsonProperty(value = "mottattDato", required = true) @Valid @NotNull ZonedDateTime mottattDato,
                   @JsonProperty(value = "søker", required = true) @Valid @NotNull Søker søker,
                   @JsonProperty(value = "bekreftelse", required = true) @Valid @NotNull Bekreftelse bekreftelse) {
        this(søknadId, versjon, mottattDato, søker, Språk.NORSK_BOKMÅL, bekreftelse);
    }

    @Override
    public SøknadId getSøknadId() {
        return søknadId;
    }

    @Override
    public Versjon getVersjon() {
        return versjon;
    }

    @Override
    public ZonedDateTime getMottattDato() {
        return mottattDato;
    }

    public Språk getSpråk() {
        return språk;
    }



    @Override
    public Søker getSøker() {
        return søker;
    }
    
    @SuppressWarnings("unchecked")
    public <B extends Bekreftelse> B getBekreftelse() {
        return (B) bekreftelse;
    }


    public Optional<Kildesystem> getKildesystem() {
        return Optional.ofNullable(kildesystem);
    }

    public void setSøknadId(SøknadId søknadId) {
        this.søknadId = Objects.requireNonNull(søknadId, "søknadId");
    }

    public void setVersjon(Versjon versjon) {
        this.versjon = Objects.requireNonNull(versjon);;
    }

    public void setMottattDato(ZonedDateTime mottattDato) {
        this.mottattDato = Objects.requireNonNull(mottattDato, "mottattDato");
    }

    public void setSøker(Søker søker) {
        this.søker = Objects.requireNonNull(søker, "søker");
    }

    public void setBekreftelse(Bekreftelse bekreftelse) {
        this.bekreftelse = Objects.requireNonNull(bekreftelse, "bekreftelse");
    }

    public void setKildesystem(Kildesystem kildesystem) {
        this.kildesystem = kildesystem;
    }

    public Oppgave medMottattDato(ZonedDateTime mottattDato) {
        this.mottattDato = Objects.requireNonNull(mottattDato, "mottattDato");
        return this;
    }

    public Oppgave medSpråk(Språk språk) {
        this.språk = språk;
        return this;
    }

    public Oppgave medSøknadId(String søknadId) {
        this.søknadId = new SøknadId(Objects.requireNonNull(søknadId, "søknadId"));
        return this;
    }

    public Oppgave medSøknadId(SøknadId søknadId) {
        this.søknadId = Objects.requireNonNull(søknadId, "søknadId");
        return this;
    }

    public Oppgave medVersjon(String versjon) {
        this.versjon = new Versjon(Objects.requireNonNull(versjon, "versjon"));
        return this;
    }

    public Oppgave medVersjon(Versjon versjon) {
        this.versjon = Objects.requireNonNull(versjon, "versjon");
        return this;
    }

    public Oppgave medSøker(Søker søker) {
        this.søker = Objects.requireNonNull(søker, "søker");
        return this;
    }

    public Oppgave medBekreftelse(Bekreftelse bekreftelse) {
        this.bekreftelse = Objects.requireNonNull(bekreftelse, "ytelse");
        return this;
    }
    

    public Oppgave medKildesystem(Kildesystem kildesystem) {
        this.kildesystem = kildesystem;
        return this;
    }

    public static final class SerDes {
        private SerDes() {
        }

        public static String serialize(Oppgave oppgave) {
            return JsonUtils.toString(oppgave);
        }

        public static Oppgave deserialize(String oppgave) {
            return JsonUtils.fromString(oppgave, Oppgave.class);
        }

        public static Oppgave deserialize(ObjectNode node) {
            try {
                return JsonUtils.getObjectMapper().treeToValue(node, Oppgave.class);
            } catch (IOException e) {
                throw new IllegalArgumentException("Kunne ikke konvertere til Oppgave.class", e);
            }
        }

    }
}
