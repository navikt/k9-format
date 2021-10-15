package no.nav.k9.søknad.felles.type;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BegrunnelseForInnsending {

    @JsonProperty(value = "begrunnelseForInnsending", required = true)
    @Valid
    @NotNull
    @NotEmpty
    private String begrunnelseForInnsending;

    public BegrunnelseForInnsending(String begrunnelseForInnsending) {
        this.begrunnelseForInnsending = begrunnelseForInnsending;
    }

    public String getBegrunnelseForInnsending() {
        return begrunnelseForInnsending;
    }

    public BegrunnelseForInnsending medBegrunnelseForInnsending(String begrunnelseForInnsending) {
        this.begrunnelseForInnsending = begrunnelseForInnsending;
        return this;
    }
}
