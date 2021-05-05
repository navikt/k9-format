package no.nav.k9.søknad.ytelse.psb.v1;

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
public class Journalposter {

    private static final String GYLDIG = "^[\\p{Alnum}]+$";

    @JsonProperty(value = "søknadenInneholderInfomasjonSomIkkeKanPunsjes")
    @Valid
    private Boolean søknadenInneholderInfomasjonSomIkkeKanPunsjes;

    @JsonProperty(value = "journalpostId", required = true)
    @NotNull
    @Size(max = 50, min = 3)
    @Pattern(regexp = GYLDIG, message = "Saksnummer [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String journalpostId;

    public Journalposter() {
    }

    public Boolean getSøknadenInneholderInfomasjonSomIkkeKanPunsjes() {
        return søknadenInneholderInfomasjonSomIkkeKanPunsjes;
    }

    public Journalposter medSøknadenInneholderInfomasjonSomIkkeKanPunsjes(Boolean søknadenInneholderInfomasjonSomIkkeKanPunsjes) {
        this.søknadenInneholderInfomasjonSomIkkeKanPunsjes = søknadenInneholderInfomasjonSomIkkeKanPunsjes;
        return this;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public Journalposter medJournalpostId(String journalpostId) {
        this.journalpostId = Objects.requireNonNull(journalpostId);
        return this;
    }
}
