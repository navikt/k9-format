package no.nav.k9.innsyn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.ettersendelse.Ettersendelse;
import no.nav.k9.søknad.Søknad;

/**
 * Innholdet til en ny søknad som har kommet inn i k9-sak.
 * <p>
 * {@code PsbSøknadsinnhold} identifiseres av {@code journalpostId}.
 * <p>
 * Merk at man skal bruke {@code søkerAktørId} og {@code pleietrengendeAktørId} fremfor å hente
 * identene direkte fra søknaden. Dette er for å støtte bytte av D-/fødselsnummer.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(InnsynHendelseData.PSB_SØKNADSINNHOLD)
public class PsbSøknadsinnhold implements InnsynHendelseData {

    @JsonProperty(value = "journalpostId", required = true)
    @Valid
    @NotNull
    @Size(max = 50, min = 3)
    @Pattern(regexp = "^[\\\\p{Alnum}]+$", message = "journalpostId [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String journalpostId;
    
    @JsonProperty(value = "søkerAktørId", required = true)
    @Valid
    @NotNull
    @Size(max = 20)
    @Pattern(regexp = "^\\d+$", message = "søkerAktørId [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String søkerAktørId;
    
    @JsonProperty(value = "pleietrengendeAktørId", required = true)
    @Valid
    @NotNull
    @Size(max = 20)
    @Pattern(regexp = "^\\d+$", message = "pleietrengendeAktørId [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String pleietrengendeAktørId;
    
    @JsonProperty(value = "søknad")
    @Valid
    @NotNull
    private Søknad søknad;

    @JsonProperty(value = "ettersendelse")
    @Valid
    private Ettersendelse ettersendelse;

    protected PsbSøknadsinnhold() {

    }

    @JsonCreator
    public PsbSøknadsinnhold(
            @JsonProperty(value = "journalpostId", required = true)
            String journalpostId,
            @JsonProperty(value = "søkerAktørId", required = true)
            String søkerAktørId,
            @JsonProperty(value = "pleietrengendeAktørId", required = true)
            String pleietrengendeAktørId,
            @JsonProperty(value = "søknad")
            Søknad søknad,
            @JsonProperty(value = "ettersendelse")
            Ettersendelse ettersendelse) {
        this.journalpostId = journalpostId;
        this.søkerAktørId = søkerAktørId;
        this.pleietrengendeAktørId = pleietrengendeAktørId;

        validerInput(søknad, ettersendelse);

        this.søknad = søknad;
        this.ettersendelse = ettersendelse;
    }

    private static void validerInput(Søknad søknad, Ettersendelse ettersendelse) {
        if (søknad == null && ettersendelse == null) {
            throw new IllegalArgumentException("Både søknad og ettersendelse kan ikke være null");
        }

        if (ettersendelse != null && søknad != null) {
            throw new IllegalArgumentException("Kan ikke sette både søknad og ettersendelse");
        }
    }


    public String getJournalpostId() {
        return journalpostId;
    }

    public String getSøkerAktørId() {
        return søkerAktørId;
    }

    public String getPleietrengendeAktørId() {
        return pleietrengendeAktørId;
    }

    public Søknad getSøknad() {
        return søknad;
    }

    public Ettersendelse getEttersendelse() {
        return ettersendelse;
    }
}
