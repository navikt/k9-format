package no.nav.k9.søknad.felles.type;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BegrunnelseForInnsending {

    @JsonProperty(value = "tekst", required = true)
    @Valid
    @NotNull
    @NotEmpty
    private String tekst;

    public BegrunnelseForInnsending(String tekst) {
        this.tekst = Objects.requireNonNull(tekst);
    }

    public String getTekst() {
        return tekst;
    }

    public BegrunnelseForInnsending medBegrunnelseForInnsending(String tekst) {
        this.tekst = Objects.requireNonNull(tekst);
        return this;
    }
}
