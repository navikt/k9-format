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

import no.nav.k9.søknad.felles.Barn;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.opptjening.Aktivitet;
import no.nav.k9.søknad.ytelse.Ytelse;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(Ytelse.OMSORGSPENGER)
public class Omsorgspenger implements Ytelse {

    @Valid
    @JsonProperty("fosterbarn")
    private final List<Barn> fosterbarn;

    @Valid
    @NotNull
    @JsonProperty(value = "aktivitet", required = true)
    private final Aktivitet aktivitet;

    @Valid
    @NotNull
    @Size(min = 1)
    @JsonProperty(value = "fraværsperioder", required = true)
    private final List<FraværPeriode> fraværsperioder;

    @JsonCreator
    public Omsorgspenger(@JsonProperty("fosterbarn") @Valid List<Barn> fosterbarn,
                         @JsonProperty(value = "aktivitet", required = true) @Valid @NotNull Aktivitet aktivitet,
                         @JsonProperty(value = "fraværsperioder", required = true) @Valid @NotNull @Size(min = 1) List<FraværPeriode> fraværsperioder) {
        this.fosterbarn = fosterbarn;
        this.aktivitet = aktivitet;
        this.fraværsperioder = fraværsperioder;
    }

    public List<Barn> getFosterbarn() {
        return fosterbarn;
    }

    public Aktivitet getAktivitet() {
        return aktivitet;
    }

    public List<FraværPeriode> getFraværsperioder() {
        return fraværsperioder;
    }

    @Override
    public Type getType() {
        return Type.OMSORGSPENGER;
    }

}
