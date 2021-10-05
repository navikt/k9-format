package no.nav.k9.søknad.ytelse.omsorgspenger.fraværskorrigering.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(Ytelse.OMSORGSPENGER_UTBETALING)
public class OmsorgspengerFraværskorrigeringInntektsmelding implements Ytelse {


    @Valid
    @NotNull
    @Size(min = 1, message = "Minst 1 fraværsperiode må oppgis")
    @JsonProperty(value = "fraværsperioder", required = true)
    private List<FraværPeriode> fraværsperioder;

    public OmsorgspengerFraværskorrigeringInntektsmelding() {
        //
    }

    @JsonCreator
    public OmsorgspengerFraværskorrigeringInntektsmelding(@JsonProperty(value = "fraværsperioder", required = true) @Valid @NotNull @Size(min = 1) List<FraværPeriode> fraværsperioder) {
        this.fraværsperioder = fraværsperioder;
    }

    public List<FraværPeriode> getFraværsperioder() {
        return fraværsperioder == null ? null : Collections.unmodifiableList(fraværsperioder);
    }


    /**
     * har ikke egen søknadsperiode for omsorgspenger, men angir på bakggrunn av angitte fraværsperioder.
     */
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
        return null;
    }

    @Override
    public Person getPleietrengende() {
        return null;
    }

    @Override
    public Person getAnnenPart() {
        return null;
    }

    public OmsorgspengerFraværskorrigeringInntektsmelding medFraværsperioder(List<FraværPeriode> fraværsperioder) {
        if (this.fraværsperioder == null)
            this.fraværsperioder = new ArrayList<>();
        this.fraværsperioder.addAll(fraværsperioder);
        return this;
    }

    @Override
    public YtelseValidator getValidator() {
        return new OmsorgspengerFraværskorrigeringInntektsmeldingValidator();
    }
}
