package no.nav.k9.søknad.felles.type;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Journalpost {

    private static final String GYLDIG = "^[\\p{Alnum}]+$";

    @JsonProperty(value = "inneholderInfomasjonSomIkkeKanPunsjes")
    @Valid
    private Boolean inneholderInfomasjonSomIkkeKanPunsjes;

    @JsonProperty(value = "inneholderMedisinskeOpplysninger")
    @Valid
    private Boolean inneholderMedisinskeOpplysninger;

    @JsonProperty(value = "journalpostId", required = true)
    @NotNull
    @Size(max = 50, min = 3)
    @Pattern(regexp = GYLDIG, message = "Saksnummer [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String journalpostId;

    public Journalpost() {
    }

    public Boolean getInneholderInfomasjonSomIkkeKanPunsjes() {
        return inneholderInfomasjonSomIkkeKanPunsjes;
    }

    public Journalpost medInfomasjonSomIkkeKanPunsjes(Boolean søknadenInneholderInfomasjonSomIkkeKanPunsjes) {
        this.inneholderInfomasjonSomIkkeKanPunsjes = søknadenInneholderInfomasjonSomIkkeKanPunsjes;
        return this;
    }

    public Boolean getInneholderMedisinskeOpplysninger() {
        return inneholderMedisinskeOpplysninger;
    }

    public Journalpost medInneholderMedisinskeOpplysninger(Boolean inneholderMedisinskeOpplysninger) {
        this.inneholderMedisinskeOpplysninger = inneholderMedisinskeOpplysninger;
        return this;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public Journalpost medJournalpostId(String journalpostId) {
        this.journalpostId = Objects.requireNonNull(journalpostId);
        return this;
    }
}
