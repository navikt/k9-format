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
import no.nav.k9.søknad.felles.aktivitet.ArbeidAktivitet;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
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
    @JsonProperty(value = "aktivitet", required = true)
    private final ArbeidAktivitet aktivitet;

    @Valid
    @JsonProperty(value = "bosteder")
    private Bosteder bosteder;

    @Valid
    @JsonProperty(value = "utenlandsopphold")
    private Utenlandsopphold utenlandsopphold;

    @Valid
    @NotNull
    @Size(min = 1, message = "Minst 1 fraværsperiode må oppgis")
    @JsonProperty(value = "fraværsperioder", required = true)
    private final List<FraværPeriode> fraværsperioder;

    @JsonCreator
    public OmsorgspengerUtbetaling(@JsonProperty("fosterbarn") @Valid List<Barn> fosterbarn,
                                   @JsonProperty(value = "aktivitet", required = true) @Valid @NotNull ArbeidAktivitet opptjening,
                                   @JsonProperty(value = "fraværsperioder", required = true) @Valid @NotNull @Size(min = 1) List<FraværPeriode> fraværsperioder,
                                   @SuppressWarnings("unused") @JsonProperty(value = "bosteder") @Valid @NotNull Bosteder bosteder,
                                   @JsonProperty(value = "utenlandsopphold") @Valid @NotNull Utenlandsopphold utenlandsopphold) {
        this.fosterbarn = fosterbarn;
        this.aktivitet = opptjening;
        this.fraværsperioder = fraværsperioder;
        this.utenlandsopphold = utenlandsopphold;
    }

    public List<Barn> getFosterbarn() {
        return fosterbarn;
    }

    public ArbeidAktivitet getAktivitet() {
        return aktivitet;
    }

    public List<FraværPeriode> getFraværsperioder() {
        return fraværsperioder;
    }

    public Bosteder getBosteder() {
        return bosteder;
    }

    public Utenlandsopphold getUtenlandsopphold() {
        return utenlandsopphold;
    }

    /** har ikke egen søknadsperiode for omsorgspenger, men angir på bakggrunn av angitte fraværsperioder. */
    @Override
    public Periode getSøknadsperiode() {
        var fom = fraværsperioder.stream().min(FraværPeriode::compareTo).orElseThrow().getPeriode().getFraOgMed();
        var tom = fraværsperioder.stream().max(FraværPeriode::compareTo).orElseThrow().getPeriode().getTilOgMed();
        return new Periode(fom, tom);
    }

    @Override
    public Type getType() {
        return Type.OMSORGSPENGER_UTBETALING;
    }

    @Override
    public List<Person> getBerørtePersoner() {
        return List.copyOf(fosterbarn);
    }

    @Override
    public YtelseValidator getValidator() {
        return new OmsorgspengerUtbetalingValidator();
    }

    @Size(max = 0, message = "${validatedValue}")
    private List<Feil> validerAngittUtenlandsopphold() {
        if (utenlandsopphold == null)
            List.of();
        return new PeriodeValidator().validerIkkeTillattOverlapp(utenlandsopphold.perioder, "utenlandsopphold.perioder");
    }

    @Size(max = 0, message = "${validatedValue}")
    private List<Feil> validerAngittBosteder() {
        if (bosteder == null)
            return List.of();
        return new PeriodeValidator().validerIkkeTillattOverlapp(bosteder.perioder, "bosteder.perioder");
    }

    @Size(max = 0, message = "${validatedValue}")
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
