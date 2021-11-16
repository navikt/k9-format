package no.nav.k9.innsyn;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.k9.søknad.Søknad;

/**
 * Innholdet til en ny søknad som har kommet inn i k9-sak.
 * 
 * {@code PsbSøknadsinnhold} identifiseres av {@code journalpostId}.
 * 
 * Merk at man skal bruke {@code søkerAktørId} og {@code pleietrengendeAktørId} fremfor å hente
 * identene direkte fra søknaden. Dette er for å støtte bytte av D-/fødselsnummer.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(InnsynHendelseData.PSB_SØKNADSDATA)
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
    
    @JsonProperty(value = "søknad", required = true)
    @Valid
    @NotNull
    private Søknad søknad;
    
    
    protected PsbSøknadsinnhold() {
        
    }
    
    public PsbSøknadsinnhold(String journalpostId, String søkerAktørId, String pleietrengendeAktørId, Søknad søknad) {
        this.journalpostId = journalpostId;
        this.søkerAktørId = søkerAktørId;
        this.pleietrengendeAktørId = pleietrengendeAktørId;
        this.søknad = søknad;
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
}
