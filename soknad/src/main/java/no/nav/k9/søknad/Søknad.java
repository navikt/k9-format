package no.nav.k9.søknad;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.node.ObjectNode;

import no.nav.k9.søknad.felles.Kildesystem;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.personopplysninger.Søker;
import no.nav.k9.søknad.felles.type.BegrunnelseForInnsending;
import no.nav.k9.søknad.felles.type.Journalpost;
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
    @Size(max=1000)
    @JsonProperty(value = "journalposter")
    private List<Journalpost> journalposter = new ArrayList<>();

    @Valid
    @JsonProperty(value = "begrunnelseForInnsending")
    private BegrunnelseForInnsending begrunnelseForInnsending = new BegrunnelseForInnsending();

    @JsonManagedReference
    @Valid
    @NotNull
    @JsonProperty(value = "ytelse", required = true)
    private Ytelse ytelse;
    
    @Valid
    @JsonProperty(value = "kildesystem", required = false)
    private Kildesystem kildesystem;

    public Søknad() {
        //
    }

    @JsonCreator
    public Søknad(@JsonProperty(value = "søknadId", required = true) @Valid @NotNull SøknadId søknadId,
                  @JsonProperty(value = "versjon", required = true) @Valid @NotNull Versjon versjon,
                  @JsonProperty(value = "mottattDato", required = true) @Valid @NotNull ZonedDateTime mottattDato,
                  @JsonProperty(value = "søker", required = true) @Valid @NotNull Søker søker,
                  @JsonProperty(value = "språk", required = false) @Valid Språk språk,
                  @JsonProperty(value = "ytelse", required = true) @Valid @NotNull Ytelse ytelse) {
        this.søknadId = Objects.requireNonNull(søknadId, "søknadId");
        this.versjon = Objects.requireNonNull(versjon, "versjon");
        this.mottattDato = Objects.requireNonNull(mottattDato, "mottattDato");
        this.søker = Objects.requireNonNull(søker, "søker");
        this.ytelse = Objects.requireNonNull(ytelse, "ytelse");
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

    public BegrunnelseForInnsending getBegrunnelseForInnsending() {
        return begrunnelseForInnsending;
    }

    public List<Person> getBerørtePersoner() {
        return ytelse == null ? Collections.emptyList() : ytelse.getBerørtePersoner();
    }

    @Override
    public Søker getSøker() {
        return søker;
    }

    public List<Journalpost> getJournalposter() {
        return journalposter;
    }

    @SuppressWarnings("unchecked")
    public <Y extends Ytelse> Y getYtelse() {
        return (Y) ytelse;
    }
    
    /**
     * Dette feltet kan brukes til å oppgi kildesystem. For historiske data
     * er feltet kun garantert å være satt for alle søknader som kommer fra
     * endringsdialogen. Ved behov for å se på historiske data av andre typer,
     * se på journalpostens kanalfelt og/eller metadatafeltet på journalposten
     * kalt "k9.kilde". 
     * 
     * @return Systemet som søknadsdataene kommer fra.
     */
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

    public void setYtelse(Ytelse ytelse) {
        this.ytelse = Objects.requireNonNull(ytelse, "ytelse");
    }
    
    public void setKildesystem(Kildesystem kildesystem) {
        this.kildesystem = kildesystem;
    }

    public Søknad medMottattDato(ZonedDateTime mottattDato) {
        this.mottattDato = Objects.requireNonNull(mottattDato, "mottattDato");
        return this;
    }

    public Søknad medSpråk(Språk språk) {
        this.språk = språk;
        return this;
    }

    public Søknad medSøknadId(String søknadId) {
        this.søknadId = new SøknadId(Objects.requireNonNull(søknadId, "søknadId"));
        return this;
    }

    public Søknad medSøknadId(SøknadId søknadId) {
        this.søknadId = Objects.requireNonNull(søknadId, "søknadId");
        return this;
    }

    public Søknad medVersjon(String versjon) {
        this.versjon = new Versjon(Objects.requireNonNull(versjon, "versjon"));
        return this;
    }

    public Søknad medVersjon(Versjon versjon) {
        this.versjon = Objects.requireNonNull(versjon, "versjon");
        return this;
    }

    public Søknad medSøker(Søker søker) {
        this.søker = Objects.requireNonNull(søker, "søker");
        return this;
    }

    public Søknad medYtelse(Ytelse ytelse) {
        this.ytelse = Objects.requireNonNull(ytelse, "ytelse");
        return this;
    }

    public Søknad medBegrunnelseForInnsending(BegrunnelseForInnsending begrunnelseForInnsending) {
        this.begrunnelseForInnsending = Objects.requireNonNull(begrunnelseForInnsending, "begrunnelseForInnsending");
        return this;
    }

    public Søknad medJournalpost(Journalpost journalpost) {
        this.journalposter.add(Objects.requireNonNull(journalpost, "journalpost"));
        return this;
    }

    public Søknad medJournalposter(List<Journalpost> journalposter) {
        this.journalposter.addAll(Objects.requireNonNull(journalposter, "journalposter"));
        return this;
    }
    
    /**
     * @see #getKildesystem()
     */
    public Søknad medKildesystem(Kildesystem kildesystem) {
        this.kildesystem = kildesystem;
        return this;
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
