package no.nav.k9.søknad.ytelse.omsorgspenger.v1;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.k9.søknad.PeriodeValidator;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.opptjening.Opptjening;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
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
    @JsonProperty(value = "bosteder")
    private Bosteder bosteder;

    @Valid
    @JsonProperty(value = "utenlandsopphold")
    private Utenlandsopphold utenlandsopphold;
    
    @Valid
    @NotNull
    @Size(min = 1)
    @JsonProperty(value = "fraværsperioder", required = true)
    private final List<FraværPeriode> fraværsperioder;

    @JsonCreator
    public OmsorgspengerUtbetaling(@JsonProperty("fosterbarn") @Valid List<Barn> fosterbarn,
                                   @JsonProperty(value = "aktivitet", required = true) @Valid @NotNull Opptjening opptjening,
                                   @JsonProperty(value = "fraværsperioder", required = true) @Valid @NotNull @Size(min = 1) List<FraværPeriode> fraværsperioder,
                                   @SuppressWarnings("unused") @JsonProperty(value = "bosteder") @Valid @NotNull Bosteder bosteder,
                                   @JsonProperty(value = "utenlandsopphold") @Valid @NotNull Utenlandsopphold utenlandsopphold) {
        this.fosterbarn = fosterbarn;
        this.opptjening = opptjening;
        this.fraværsperioder = fraværsperioder;
        this.utenlandsopphold = utenlandsopphold;
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

    @Size(max=0)
    private List<Feil> validerAngittUtenlandsopphold() {
        if (utenlandsopphold == null) List.of();
        return new PeriodeValidator().validerIkkeTillattOverlapp(utenlandsopphold.perioder, "utenlandsopphold.perioder");
    }

    @Size(max=0)
    private List<Feil> validerAngittBosteder() {
        if (bosteder == null) return List.of();
        return new PeriodeValidator().validerIkkeTillattOverlapp(bosteder.perioder, "bosteder.perioder");
    }

    @Size(max=0)
    private List<Feil> getValiderAngittFosterbarn() {
        var barn = this.fosterbarn;
        if (barn == null || barn.isEmpty())
            return List.of();
        var index = 0;
        List<Feil> feil = new ArrayList<>();
        for (Barn b : barn) {
            if (b.norskIdentitetsnummer == null && b.fødselsdato == null) {
                feil.add(new Feil("fosterbarn[" + index + "]", "norskIdentitetsnummerEllerFødselsdatoPåkrevd",
                    "Må sette enten Personnummer/D-nummer på fosterbarn, eller fødselsdato."));
            } else if (b.norskIdentitetsnummer != null && b.fødselsdato != null) {
                feil.add(
                    new Feil("fosterbarn[" + index + "]", "ikkeEntydigIdPåBarnet", "Må sette enten Personnummer/D-nummer på fosterbarn, eller fødselsdato."));
            }
            index++;
        }
        return feil;
    }
}
