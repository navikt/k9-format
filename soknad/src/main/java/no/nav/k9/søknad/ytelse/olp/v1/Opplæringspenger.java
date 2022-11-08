package no.nav.k9.søknad.ytelse.olp.v1;

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

import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.opptjening.OpptjeningAktivitet;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.olp.v1.kurs.Kurs;
import no.nav.k9.søknad.ytelse.psb.v1.DataBruktTilUtledning;
import no.nav.k9.søknad.ytelse.psb.v1.LovbestemtFerie;
import no.nav.k9.søknad.ytelse.psb.v1.Omsorg;
import no.nav.k9.søknad.ytelse.psb.v1.Uttak;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(Ytelse.OPPLÆRINGSPENGER)
public class Opplæringspenger implements Ytelse {

    @Valid
    @NotNull
    @JsonProperty(value = "barn", required = true)
    private Barn barn;

    @Valid
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonProperty(value = "søknadsperiode", required = true)
    @NotNull
    private List<@NotNull @Valid Periode> søknadsperiode = new ArrayList<>();

    @Valid
    @JsonProperty(value = "trekkKravPerioder", required = true)
    @NotNull
    private List<@NotNull @Valid Periode> trekkKravPerioder = new ArrayList<>();

    @Valid
    @JsonProperty(value = "opptjeningAktivitet", required = true)
    @NotNull
    private OpptjeningAktivitet opptjeningAktivitet = new OpptjeningAktivitet();

    @Valid
    @JsonProperty(value = "dataBruktTilUtledning")
    private DataBruktTilUtledning dataBruktTilUtledning;

    @Valid
    @JsonProperty(value = "bosteder", required = true)
    private Bosteder bosteder = new Bosteder();

    @Valid
    @JsonProperty(value = "utenlandsopphold", required = true)
    private Utenlandsopphold utenlandsopphold = new Utenlandsopphold();

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

    @Valid
    @NotNull
    @JsonProperty(value = "kurs", required = true)
    private Kurs kurs = new Kurs();

    public Opplæringspenger() {
    }

    public Barn getBarn() {
        return barn;
    }

    public Opplæringspenger medBarn(Barn barn) {
        this.barn = Objects.requireNonNull(barn, "barn");
        return this;
    }

    @Override
    public Person getPleietrengende() {
        return barn;
    }

    @Override
    public Person getAnnenPart() {
        // ikke relevant for opplæringspenger
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

    public Opplæringspenger medSøknadsperiode(List<Periode> søknadsperiodeList) {
        this.søknadsperiode.addAll(Objects.requireNonNull(søknadsperiodeList, "søknadsperiodeList"));
        return this;
    }

    public Opplæringspenger medSøknadsperiode(Periode søknadsperiode) {
        this.søknadsperiode.add(Objects.requireNonNull(søknadsperiode, "søknadsperiode"));
        return this;
    }

    public List<Periode> getEndringsperiode() {
        return PerioderMedEndringUtil.getEndringsperiode(this);
    }


    public Opplæringspenger addAllTrekkKravPerioder(List<Periode> trekkKravPerioder) {
        this.trekkKravPerioder.addAll(Objects.requireNonNull(trekkKravPerioder, "trekkKravPerioder"));
        return this;
    }

    public Opplæringspenger addTrekkKravPeriode(Periode trekkKravPeriode) {
        this.trekkKravPerioder.add(Objects.requireNonNull(trekkKravPeriode, "trekkKravPeriode"));
        return this;
    }

    public List<Periode> getTrekkKravPerioder() {
        return Collections.unmodifiableList(trekkKravPerioder);
    }

    public OpptjeningAktivitet getOpptjeningAktivitet() {
        return opptjeningAktivitet;
    }

    public Opplæringspenger medOpptjeningAktivitet(OpptjeningAktivitet arbeidAktivitet) {
        this.opptjeningAktivitet = Objects.requireNonNull(arbeidAktivitet, "arbeidAktivitet");
        return this;
    }

    public Optional<DataBruktTilUtledning> getSøknadInfo() {
        return Optional.ofNullable(dataBruktTilUtledning);
    }

    public Opplæringspenger medSøknadInfo(DataBruktTilUtledning dataBruktTilUtledning) {
        this.dataBruktTilUtledning = Objects.requireNonNull(dataBruktTilUtledning, "dataBruktTilUtledning");
        return this;
    }

    public Bosteder getBosteder() {
        return bosteder;
    }

    public Opplæringspenger medBosteder(Bosteder bosteder) {
        this.bosteder = Objects.requireNonNull(bosteder, "bosteder");
        return this;
    }

    public Utenlandsopphold getUtenlandsopphold() {
        return utenlandsopphold;
    }

    public Opplæringspenger medUtenlandsopphold(Utenlandsopphold utenlandsopphold) {
        this.utenlandsopphold = Objects.requireNonNull(utenlandsopphold, "utenlandsopphold");
        return this;
    }

    public LovbestemtFerie getLovbestemtFerie() {
        return lovbestemtFerie;
    }

    public Opplæringspenger medLovbestemtFerie(LovbestemtFerie lovbestemtFerie) {
        this.lovbestemtFerie = Objects.requireNonNull(lovbestemtFerie, "lovbestemtFerie");
        return this;
    }

    public Arbeidstid getArbeidstid() {
        return arbeidstid;
    }

    public Opplæringspenger medArbeidstid(Arbeidstid arbeidstid) {
        this.arbeidstid = Objects.requireNonNull(arbeidstid, "arbeidstid");
        return this;
    }

    public Uttak getUttak() {
        return uttak;
    }

    public Opplæringspenger medUttak(Uttak uttak) {
        this.uttak = Objects.requireNonNull(uttak, "uttak");
        return this;
    }

    public Omsorg getOmsorg() {
        return this.omsorg;
    }

    public Opplæringspenger medOmsorg(Omsorg omsorg) {
        this.omsorg = Objects.requireNonNull(omsorg, "omsorg");
        return this;
    }

    public Kurs getKurs() {
        return kurs;
    }

    public Opplæringspenger medKurs(Kurs kurs) {
        this.kurs = Objects.requireNonNull(kurs, "kurs");
        return this;
    }

    @Override
    public Type getType() {
        return Type.OPPLÆRINGSPENGER;
    }

    @Override
    public List<Person> getBerørtePersoner() {
        return List.of(barn); // kjenner ikke de andre søkerne her, kun pleietrengende som er identifisert
    }

    @Override
    public YtelseValidator getValidator(Versjon versjon) {
        return new OpplæringspengerYtelseValidator();
    }
}
