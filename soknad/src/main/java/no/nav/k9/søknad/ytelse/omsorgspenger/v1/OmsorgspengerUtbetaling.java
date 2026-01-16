package no.nav.k9.søknad.ytelse.omsorgspenger.v1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.fravær.FraværPeriode;
import no.nav.k9.søknad.felles.opptjening.OpptjeningAktivitet;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(Ytelse.OMSORGSPENGER_UTBETALING)
public class OmsorgspengerUtbetaling implements Ytelse {

    @JsonProperty("fosterbarn")
    private List<@Valid Barn> fosterbarn;

    @Valid
    @JsonProperty(value = "aktivitet")
    private OpptjeningAktivitet aktivitet;

    @Valid
    @JsonProperty(value = "bosteder")
    private Bosteder bosteder;

    @Valid
    @JsonProperty(value = "utenlandsopphold")
    private Utenlandsopphold utenlandsopphold;

    @JsonProperty(value = "fraværsperioder")
    private List<@Valid FraværPeriode> fraværsperioder;

    @JsonProperty(value = "fraværsperioderKorrigeringIm")
    private List<@Valid FraværPeriode> fraværsperioderKorrigeringIm;

    @JsonProperty(value = "dataBruktTilUtledning")
    @Valid
    private DataBruktTilUtledning dataBruktTilUtledning;

    public OmsorgspengerUtbetaling() {
        //
    }

    @JsonCreator
    public OmsorgspengerUtbetaling(@JsonProperty("fosterbarn") @Valid List<Barn> fosterbarn,
                                   @JsonProperty(value = "aktivitet") @Valid OpptjeningAktivitet opptjening,
                                   @JsonProperty(value = "fraværsperioder") @Valid List<FraværPeriode> fraværsperioder,
                                   @JsonProperty(value = "fraværsperioderKorrigeringIm") @Valid List<FraværPeriode> fraværsperioderKorrigeringIm,
                                   @JsonProperty(value = "bosteder") @Valid Bosteder bosteder,
                                   @JsonProperty(value = "utenlandsopphold") @Valid Utenlandsopphold utenlandsopphold) {
        this.fosterbarn = fosterbarn;
        this.aktivitet = opptjening;
        this.fraværsperioder = fraværsperioder;
        this.fraværsperioderKorrigeringIm = fraværsperioderKorrigeringIm;
        this.bosteder = bosteder;
        this.utenlandsopphold = utenlandsopphold;
    }

    public List<Barn> getFosterbarn() {
        return fosterbarn == null ? null : Collections.unmodifiableList(fosterbarn);
    }

    public OpptjeningAktivitet getAktivitet() {
        return aktivitet;
    }

    public List<FraværPeriode> getFraværsperioder() {
        return fraværsperioder == null? null: Collections.unmodifiableList(fraværsperioder);
    }

    public List<FraværPeriode> getFraværsperioderKorrigeringIm() {
        return fraværsperioderKorrigeringIm;
    }

    public Bosteder getBosteder() {
        return bosteder;
    }

    public Utenlandsopphold getUtenlandsopphold() {
        return utenlandsopphold;
    }

    @Override
    public Person getPleietrengende() {
        return null;
    }

    @Override
    public Person getAnnenPart() {
        return null;
    }

    /** har ikke egen søknadsperiode for omsorgspenger, men angir på bakggrunn av angitte fraværsperioder. */
    @Override
    public Periode getSøknadsperiode() {
        LocalDate fom;
        LocalDate tom;
        if (fraværsperioder != null && !fraværsperioder.isEmpty()) {
            fom = fraværsperioder.stream().min(FraværPeriode::compareTo).orElseThrow().getPeriode().getFraOgMed();
            tom = fraværsperioder.stream().max(FraværPeriode::compareTo).orElseThrow().getPeriode().getTilOgMed();
        } else if (fraværsperioderKorrigeringIm != null && !fraværsperioderKorrigeringIm.isEmpty()) {
            fom = fraværsperioderKorrigeringIm.stream().min(FraværPeriode::compareTo).orElseThrow().getPeriode().getFraOgMed();
            tom = fraværsperioderKorrigeringIm.stream().max(FraværPeriode::compareTo).orElseThrow().getPeriode().getTilOgMed();
        } else {
            // Skal fanges opp av validator
            throw new IllegalArgumentException("Må ha fravær fra enten søker eller fra fraværskorrigering av inntektsmelding");
        }
        return new Periode(fom, tom);
    }

    @Override
    public Type getType() {
        return Type.OMSORGSPENGER_UTBETALING;
    }

    @Override
    public List<Person> getBerørtePersoner() {
        return fosterbarn == null ? null : Collections.unmodifiableList(fosterbarn);
    }

    @Override
    public YtelseValidator getValidator(Versjon versjon) {
        return new OmsorgspengerUtbetalingValidator(versjon);
    }

    @Override
    public DataBruktTilUtledning getDataBruktTilUtledning() {
        return this.dataBruktTilUtledning;
    }

    @Override
    public Ytelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning) {
        this.dataBruktTilUtledning = dataBruktTilUtledning;
        return this;
    }

    public OmsorgspengerUtbetaling medFosterbarn(List<Barn> barn) {
        if (this.fraværsperioder == null)
            this.fosterbarn = new ArrayList<>();
        this.fosterbarn.addAll(barn);
        return this;
    }

    public OmsorgspengerUtbetaling medFraværsperioder(List<FraværPeriode> fraværsperioder) {
        if (this.fraværsperioder == null)
            this.fraværsperioder = new ArrayList<>();
        this.fraværsperioder.addAll(fraværsperioder);
        return this;
    }

    public OmsorgspengerUtbetaling medFraværsperioderKorrigeringIm(List<FraværPeriode> fraværsperioder) {
        if (this.fraværsperioderKorrigeringIm == null)
            this.fraværsperioderKorrigeringIm = new ArrayList<>();
        this.fraværsperioderKorrigeringIm.addAll(fraværsperioder);
        return this;
    }

    public OmsorgspengerUtbetaling medAktivitet(OpptjeningAktivitet arbeidAktivitet) {
        this.aktivitet = arbeidAktivitet;
        return this;
    }

    public OmsorgspengerUtbetaling medBosteder(Bosteder bosteder) {
        this.bosteder = bosteder;
        return this;
    }

    public OmsorgspengerUtbetaling medUtenlandsopphold(Utenlandsopphold utenlandsopphold) {
        this.utenlandsopphold = utenlandsopphold;
        return this;
    }
}
