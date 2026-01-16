package no.nav.k9.søknad.ytelse.olp.v1.kurs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Reise {
    @JsonProperty(value = "reiserUtenforKursdager", required = true)
    @NotNull
    @Valid
    private boolean reiserUtenforKursdager;

    @JsonProperty(value="reisedager")
    private List<@NotNull @Valid LocalDate> reisedager;

    @JsonProperty(value = "reisedagerBeskrivelse")
    @Size(max = 4000)
    @Pattern(regexp = "^[\\p{Pd}\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}§]*$", message = "[ugyldigSyntaks] matcher ikke tillatt pattern [{regexp}]")
    private String reisedagerBeskrivelse;

    public Reise() {
    }

    public Reise(boolean reiserUtenforKursdager, List<LocalDate> reisedager, String reisedagerBeskrivelse) {
        this.reiserUtenforKursdager = reiserUtenforKursdager;
        this.reisedager = reisedager;
        this.reisedagerBeskrivelse = reisedagerBeskrivelse;
    }

    public boolean isReiserUtenforKursdager() {
        return reiserUtenforKursdager;
    }

    public List<LocalDate> getReisedager() {
        return reisedager;
    }

    public String getReisedagerBeskrivelse() {
        return reisedagerBeskrivelse;
    }
}
