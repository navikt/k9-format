package no.nav.k9.innsyn;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(InnsynHendelseData.SØKNAD_TRUKKET)
public class SøknadTrukket implements InnsynHendelseData {

    @JsonProperty(value = "journalpostId", required = true)
    @Valid
    @NotNull
    @Size(max = 50, min = 3)
    @Pattern(regexp = "^[\\\\p{Alnum}]+$", message = "journalpostId [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String journalpostId;
    
    
    protected SøknadTrukket() {
        
    }
    
    public SøknadTrukket(String journalpostId) {
        this.journalpostId = Objects.requireNonNull(journalpostId, "journalpostId");
    }
    

    public String getJournalpostId() {
        return journalpostId;
    }
}
