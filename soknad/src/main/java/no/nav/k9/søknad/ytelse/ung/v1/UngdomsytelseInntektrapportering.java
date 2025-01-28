package no.nav.k9.søknad.ytelse.ung.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class UngdomsytelseInntektrapportering {

    private UngSøknadstype søknadType = UngSøknadstype.RAPPORTERING_SØKNAD;

    @JsonProperty(value = "inntekter", required = true)
    @Valid
    @NotNull
    private OppgittInntekt inntekter;

    public OppgittInntekt getInntekter() {
        return inntekter;
    }

    public UngdomsytelseInntektrapportering medInntekter(OppgittInntekt inntekter) {
        this.inntekter = Objects.requireNonNull(inntekter, "inntekt");
        return this;
    }

    public UngSøknadstype getSøknadType() {
        return søknadType;
    }

}
