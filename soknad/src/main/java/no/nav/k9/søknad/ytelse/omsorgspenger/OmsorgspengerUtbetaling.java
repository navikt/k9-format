package no.nav.k9.søknad.ytelse.omsorgspenger;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.k9.søknad.felles.opptjening.Opptjening;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(Ytelse.OMSORGSPENGER_UTBETALING)
public class OmsorgspengerUtbetaling implements Ytelse {

    @Valid
    @JsonProperty("fosterbarn")
    private final List<Barn> fosterbarn;

    @Valid
    @NotNull
    @JsonProperty(value = "opptjening", required = true)
    private final Opptjening opptjening;

    @Valid
    @NotNull
    @Size(min = 1)
    @JsonProperty(value = "fraværsperioder", required = true)
    private final List<FraværPeriode> fraværsperioder;

    @JsonCreator
    public OmsorgspengerUtbetaling(@JsonProperty("fosterbarn") @Valid List<Barn> fosterbarn,
                                   @JsonProperty(value = "aktivitet", required = true) @Valid @NotNull Opptjening opptjening,
                                   @JsonProperty(value = "fraværsperioder", required = true) @Valid @NotNull @Size(min = 1) List<FraværPeriode> fraværsperioder) {
        this.fosterbarn = fosterbarn;
        this.opptjening = opptjening;
        this.fraværsperioder = fraværsperioder;
    }

    public List<Barn> getFosterbarn() {
        return fosterbarn;
    }

    public Opptjening getOpptjening() {
        return opptjening;
    }

    public List<FraværPeriode> getFraværsperioder() {
        return fraværsperioder;
    }

    @Override
    public Type getType() {
        return Type.OMSORGSPENGER_UTBETALING;
    }

    @Override
    public YtelseValidator getValidator() {
        return new OmsorgspengerUtbetalingValidator();
    }

}
