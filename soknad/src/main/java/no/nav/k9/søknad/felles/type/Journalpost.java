package no.nav.k9.søknad.felles.type;

import java.util.ArrayList;
import java.util.List;
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

    @Deprecated
    @JsonProperty(value = "inneholderInfomasjonSomIkkeKanPunsjes", required = true)
    @Valid
    private Boolean inneholderInfomasjonSomIkkeKanPunsjes;

    @JsonProperty(value = "inneholderInformasjonSomIkkeKanPunsjes", required = true)
    @Valid
    private Boolean inneholderInformasjonSomIkkeKanPunsjes;

    @JsonProperty(value = "inneholderMedisinskeOpplysninger", required = true)
    @Valid
    private Boolean inneholderMedisinskeOpplysninger;

    @JsonProperty(value = "brevkoder")
    private List<String> brevkoder;

    @JsonProperty(value = "journalpostId", required = true)
    @NotNull
    @Size(max = 50, min = 3)
    @Pattern(regexp = GYLDIG, message = "Saksnummer [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String journalpostId;

    public Journalpost() {
    }

    public Boolean getInneholderInformasjonSomIkkeKanPunsjes() {
        return (inneholderInformasjonSomIkkeKanPunsjes != null) ? inneholderInformasjonSomIkkeKanPunsjes : inneholderInfomasjonSomIkkeKanPunsjes;
    }

    public Journalpost medInformasjonSomIkkeKanPunsjes(Boolean søknadenInneholderInformasjonSomIkkeKanPunsjes) {
        this.inneholderInformasjonSomIkkeKanPunsjes = Objects.requireNonNull(søknadenInneholderInformasjonSomIkkeKanPunsjes, "søknadenInneholderInformasjonSomIkkeKanPunsjes");
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
        this.journalpostId = Objects.requireNonNull(journalpostId, "journalpostId");
        return this;
    }

    public List<String> getBrevkoder() {
        return brevkoder;
    }

    public Journalpost medBrevkoder(List<String> brevkoder) {
        this.brevkoder = new ArrayList<>(Objects.requireNonNull(brevkoder, "brevkoder"));
        return this;
    }
}
