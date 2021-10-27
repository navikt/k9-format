package no.nav.k9.søknad.ytelse.psb.v1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.k9.søknad.felles.opptjening.OpptjeningAktivitet;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;
import no.nav.k9.søknad.ytelse.psb.v1.tilsyn.Tilsynsordning;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(Ytelse.PLEIEPENGER_SYKT_BARN)
public class PleiepengerSyktBarn implements Ytelse {

    @Valid
    @NotNull
    @JsonProperty(value = "barn", required = true)
    private Barn barn;

    @Valid
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonProperty(value = "søknadsperiode", required = true)
    @NotNull
    private List<Periode> søknadsperiode = new ArrayList<>();

    @Deprecated
    @Valid
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonProperty(value = "endringsperiode", required = true)
    private List<Periode> endringsperiode = new ArrayList<>();

    @Valid
    @JsonProperty(value = "trekkKravPerioder", required = true)
    @NotNull
    private List<Periode> trekkKravPerioder = new ArrayList<>();

    @Valid
    @JsonProperty(value = "opptjeningAktivitet", required = true)
    @NotNull
    private OpptjeningAktivitet opptjeningAktivitet = new OpptjeningAktivitet();

    @Valid
    @JsonProperty(value = "dataBruktTilUtledning")
    private DataBruktTilUtledning dataBruktTilUtledning;

    @Deprecated
    @Valid
    @JsonProperty(value = "infoFraPunsj")
    private InfoFraPunsj infoFraPunsj;

    @Valid
    @JsonProperty(value = "bosteder", required = true)
    @NotNull
    private Bosteder bosteder = new Bosteder();

    @Valid
    @JsonProperty(value = "utenlandsopphold", required = true)
    private Utenlandsopphold utenlandsopphold = new Utenlandsopphold();

    @Valid
    @JsonProperty(value = "beredskap", required = true)
    private Beredskap beredskap = new Beredskap();

    @Valid
    @JsonProperty(value = "nattevåk", required = true)
    private Nattevåk nattevåk = new Nattevåk();

    @Valid
    @JsonProperty(value = "tilsynsordning", required = true)
    private Tilsynsordning tilsynsordning = new Tilsynsordning();

    @Valid
    @JsonProperty(value = "lovbestemtFerie", required = true)
    private LovbestemtFerie lovbestemtFerie = new LovbestemtFerie();

    @Valid
    @JsonProperty(value = "arbeidstid", required = true)
    private Arbeidstid arbeidstid = new Arbeidstid();

    @Valid
    @JsonProperty(value = "uttak", required = true)
    private Uttak uttak = new Uttak();

    @Valid
    @JsonProperty(value = "omsorg", required = true)
    private Omsorg omsorg = new Omsorg();

    public PleiepengerSyktBarn() {
    }

    public Barn getBarn() {
        return barn;
    }

    public PleiepengerSyktBarn medBarn(Barn barn) {
        this.barn = Objects.requireNonNull(barn, "barn");
        return this;
    }

    @Override
    public Person getPleietrengende() {
        return barn;
    }

    @Override
    public Person getAnnenPart() {
        // ikke relevant for pleiepenger sykt barn
        return null;
    }

    @Override
    public Periode getSøknadsperiode() {
        final List<Periode> perioder = new ArrayList<>(søknadsperiode);
        perioder.addAll(getEndringsperiode());

        final var fom = perioder
                .stream()
                .map(Periode::getFraOgMed)
                .min(LocalDate::compareTo)
                .orElseThrow();
        final var tom = perioder
                .stream()
                .map(Periode::getTilOgMed)
                .max(LocalDate::compareTo)
                .orElseThrow();
        return new Periode(fom, tom);
    }

    public List<Periode> getSøknadsperiodeList() {
        return søknadsperiode == null ? null : Collections.unmodifiableList(søknadsperiode);
    }

    public PleiepengerSyktBarn medSøknadsperiode(List<Periode> søknadsperiodeList) {
        this.søknadsperiode.addAll(Objects.requireNonNull(søknadsperiodeList, "søknadsperiodeList"));
        return this;
    }

    public PleiepengerSyktBarn medSøknadsperiode(Periode søknadsperiode) {
        this.søknadsperiode.add(Objects.requireNonNull(søknadsperiode, "søknadsperiode"));
        return this;
    }

    //@JsonProperty(value = "endringsperiode")
    public List<Periode> getEndringsperiode() {
        return PerioderMedEndringUtil.getEndringsperiode(this);
    }

    @Deprecated
    public List<Periode> getUtledetEndringsperiode() {
        return PerioderMedEndringUtil.getEndringsperiode(this);
    }

    @Deprecated
    public PleiepengerSyktBarn medEndringsperiode(List<Periode> endringsperiodeList) {
        this.endringsperiode.addAll(Objects.requireNonNull(endringsperiodeList, "endringsperiodeList"));
        return this;
    }

    @Deprecated
    public PleiepengerSyktBarn medEndringsperiode(Periode endringsperiode) {
        this.endringsperiode.add(Objects.requireNonNull(endringsperiode, "endringsperiode"));
        return this;
    }

    public PleiepengerSyktBarn addAllTrekkKravPerioder(List<Periode> trekkKravPerioder) {
        this.trekkKravPerioder.addAll(Objects.requireNonNull(trekkKravPerioder, "trekkKravPerioder"));
        return this;
    }

    public PleiepengerSyktBarn addTrekkKravPeriode(Periode trekkKravPeriode) {
        this.trekkKravPerioder.add(Objects.requireNonNull(trekkKravPeriode, "trekkKravPeriode"));
        return this;
    }

    public List<Periode> getTrekkKravPerioder() {
        return Collections.unmodifiableList(trekkKravPerioder);
    }

    public OpptjeningAktivitet getOpptjeningAktivitet() {
        return opptjeningAktivitet;
    }

    public PleiepengerSyktBarn medOpptjeningAktivitet(OpptjeningAktivitet arbeidAktivitet) {
        this.opptjeningAktivitet = Objects.requireNonNull(arbeidAktivitet, "arbeidAktivitet");
        return this;
    }

    public Optional<DataBruktTilUtledning> getSøknadInfo() {
        return Optional.ofNullable(dataBruktTilUtledning);
    }

    public PleiepengerSyktBarn medSøknadInfo(DataBruktTilUtledning dataBruktTilUtledning) {
        this.dataBruktTilUtledning = Objects.requireNonNull(dataBruktTilUtledning, "dataBruktTilUtledning");
        return this;
    }

    @Deprecated
    public Optional<InfoFraPunsj> getInfoFraPunsj() {
        return Optional.ofNullable(infoFraPunsj);
    }

    @Deprecated
    public PleiepengerSyktBarn medInfoFraPunsj(InfoFraPunsj infoFraPunsj) {
        this.infoFraPunsj = Objects.requireNonNull(infoFraPunsj, "infoFraPunsj");
        return this;
    }

    public Bosteder getBosteder() {
        return bosteder;
    }

    public PleiepengerSyktBarn medBosteder(Bosteder bosteder) {
        this.bosteder = Objects.requireNonNull(bosteder, "bosteder");
        return this;
    }

    public Utenlandsopphold getUtenlandsopphold() {
        return utenlandsopphold;
    }

    public PleiepengerSyktBarn medUtenlandsopphold(Utenlandsopphold utenlandsopphold) {
        this.utenlandsopphold = Objects.requireNonNull(utenlandsopphold, "utenlandsopphold");
        return this;
    }

    public Beredskap getBeredskap() {
        return beredskap;
    }

    public PleiepengerSyktBarn medBeredskap(Beredskap beredskap) {
        this.beredskap = Objects.requireNonNull(beredskap, "beredskap");
        return this;
    }

    public Nattevåk getNattevåk() {
        return nattevåk;
    }

    public PleiepengerSyktBarn medNattevåk(Nattevåk nattevåk) {
        this.nattevåk = Objects.requireNonNull(nattevåk, "nattevåk");
        return this;
    }

    public Tilsynsordning getTilsynsordning() {
        return tilsynsordning;
    }

    public PleiepengerSyktBarn medTilsynsordning(Tilsynsordning tilsynsordning) {
        this.tilsynsordning = Objects.requireNonNull(tilsynsordning, "tilsynsordning");
        return this;
    }

    public LovbestemtFerie getLovbestemtFerie() {
        return lovbestemtFerie;
    }

    public PleiepengerSyktBarn medLovbestemtFerie(LovbestemtFerie lovbestemtFerie) {
        this.lovbestemtFerie = Objects.requireNonNull(lovbestemtFerie, "lovbestemtFerie");
        return this;
    }

    public Arbeidstid getArbeidstid() {
        return arbeidstid;
    }

    public PleiepengerSyktBarn medArbeidstid(Arbeidstid arbeidstid) {
        this.arbeidstid = Objects.requireNonNull(arbeidstid, "arbeidstid");
        return this;
    }

    public Uttak getUttak() {
        return uttak;
    }

    public PleiepengerSyktBarn medUttak(Uttak uttak) {
        this.uttak = Objects.requireNonNull(uttak, "uttak");
        return this;
    }

    public Omsorg getOmsorg() {
        return this.omsorg;
    }

    public PleiepengerSyktBarn medOmsorg(Omsorg omsorg) {
        this.omsorg = Objects.requireNonNull(omsorg, "omsorg");
        return this;
    }

    @Override
    public Type getType() {
        return Type.PLEIEPENGER_SYKT_BARN;
    }

    @Override
    public List<Person> getBerørtePersoner() {
        return List.of(barn); // kjenner ikke de andre søkerne her, kun pleietrengende som er identifisert
    }

    @Override
    public YtelseValidator getValidator() {
        return new PleiepengerSyktBarnYtelseValidator();
    }
}
