package no.nav.k9.søknad.felles.type;

import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BegrunnelseForInnsending {

    @JsonProperty(value = "tekst", required = false)
    @Valid
    private String tekst;

    public BegrunnelseForInnsending() {
    }

    public String getTekst() {
        return tekst;
    }

    public BegrunnelseForInnsending medBegrunnelseForInnsending(String tekst) {
        this.tekst = Objects.requireNonNull(tekst);
        return this;
    }
}
