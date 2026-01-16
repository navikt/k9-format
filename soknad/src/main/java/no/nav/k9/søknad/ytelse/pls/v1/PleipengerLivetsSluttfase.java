package no.nav.k9.søknad.ytelse.pls.v1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.opptjening.OpptjeningAktivitet;
import no.nav.k9.søknad.felles.personopplysninger.Bosteder;
import no.nav.k9.søknad.felles.personopplysninger.Utenlandsopphold;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.felles.validering.periode.LukketPeriode;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.psb.v1.LovbestemtFerie;
import no.nav.k9.søknad.ytelse.psb.v1.Uttak;
import no.nav.k9.søknad.ytelse.psb.v1.arbeidstid.Arbeidstid;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(Ytelse.PLEIEPENGER_LIVETS_SLUTTFASE)
public class PleipengerLivetsSluttfase implements Ytelse {

    @Valid
    @NotNull
    @JsonProperty(value = "pleietrengende", required = true)
    private Pleietrengende pleietrengende;

    @NotNull
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonProperty(value = "søknadsperiode", required = true)
    private List<@Valid @NotNull @LukketPeriode Periode> søknadsperiode = new ArrayList<>();

    @JsonProperty(value = "trekkKravPerioder", required = true)
    @NotNull
    private List<@Valid @NotNull @LukketPeriode Periode> trekkKravPerioder = new ArrayList<>();

    @Valid
    @JsonProperty(value = "opptjeningAktivitet")
    private OpptjeningAktivitet opptjeningAktivitet = new OpptjeningAktivitet();

    @Valid
    @JsonProperty(value = "bosteder", required = true)
    private Bosteder bosteder = new Bosteder();

    @Valid
    @JsonProperty(value = "utenlandsopphold", required = true)
    private Utenlandsopphold utenlandsopphold = new Utenlandsopphold();

    @Valid
    @NotNull
    @JsonProperty(value = "arbeidstid", required = true)
    private Arbeidstid arbeidstid = new Arbeidstid();

    @Valid
    @JsonProperty(value = "uttak"/* ,required = true TODO skal skrus på før lansering */)
    private Uttak uttak = new Uttak(); //TODO vurder å lage egen variant for å ha riktigere navngiving

    @Valid
    @JsonProperty(value = "lovbestemtFerie")
    private LovbestemtFerie lovbestemtFerie = new LovbestemtFerie();

    @JsonProperty(value = "dataBruktTilUtledning")
    @Valid
    private DataBruktTilUtledning dataBruktTilUtledning;

    @Override
    public Type getType() {
        return Type.PLEIEPENGER_LIVETS_SLUTTFASE;
    }

    @Override
    public YtelseValidator getValidator(Versjon versjon) {
        return new PleiepengerLivetsSluttfaseYtelseValidator();
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

    @Override
    public List<Person> getBerørtePersoner() {
        return List.of(pleietrengende);
    }

    public Pleietrengende getPleietrengende() {
        return pleietrengende;
    }

    @Override
    public Person getAnnenPart() {
        return null;
    }

    @Override
    public Periode getSøknadsperiode() {
        final List<Periode> perioder = new ArrayList<>(søknadsperiode);
        perioder.addAll(getEndringsperiode());

        var fom = perioder
                .stream()
                .map(Periode::getFraOgMed)
                .min(LocalDate::compareTo)
                .orElseThrow();
        var tom = perioder
                .stream()
                .map(Periode::getTilOgMed)
                .max(LocalDate::compareTo)
                .orElseThrow();
        return new Periode(fom, tom);
    }

    public List<Periode> getSøknadsperiodeList() {
        return Collections.unmodifiableList(søknadsperiode);
    }

    public List<Periode> getEndringsperiode() {
        return PleiepengerLivetsSluttfasePerioderMedEndringUtil.getEndringsperiode(this);
    }

    public OpptjeningAktivitet getOpptjeningAktivitet() {
        return opptjeningAktivitet;
    }

    public Bosteder getBosteder() {
        return bosteder;
    }

    public Utenlandsopphold getUtenlandsopphold() {
        return utenlandsopphold;
    }

    public List<Periode> getTrekkKravPerioder() {
        return Collections.unmodifiableList(trekkKravPerioder);
    }

    public Arbeidstid getArbeidstid() {
        return arbeidstid;
    }

    public Uttak getUttak() {
        return uttak;
    }

    public PleipengerLivetsSluttfase medPleietrengende(Pleietrengende pleietrengende) {
        this.pleietrengende = Objects.requireNonNull(pleietrengende, "pleietrengende");
        return this;
    }

    public PleipengerLivetsSluttfase medSøknadsperiode(List<Periode> søknadsperiodeList) {
        this.søknadsperiode.addAll(Objects.requireNonNull(søknadsperiodeList, "søknadsperiodeList"));
        return this;
    }

    public PleipengerLivetsSluttfase medSøknadsperiode(Periode søknadsperiode) {
        this.søknadsperiode.add(Objects.requireNonNull(søknadsperiode, "søknadsperiode"));
        return this;
    }

    public PleipengerLivetsSluttfase medOpptjeningAktivitet(OpptjeningAktivitet opptjeningAktivitet) {
        this.opptjeningAktivitet = Objects.requireNonNull(opptjeningAktivitet, "opptjeningAktivitet");
        return this;
    }

    public PleipengerLivetsSluttfase ignorerOpplysningerOmOpptjening() {
        this.opptjeningAktivitet = null;
        return this;
    }

    public PleipengerLivetsSluttfase medBosteder(Bosteder bosteder) {
        this.bosteder = Objects.requireNonNull(bosteder, "bosteder");
        return this;
    }

    public PleipengerLivetsSluttfase medUtenlandsopphold(Utenlandsopphold utenlandsopphold) {
        this.utenlandsopphold = Objects.requireNonNull(utenlandsopphold, "utenlandsopphold");
        return this;
    }

    public PleipengerLivetsSluttfase leggTilTrekkKravPerioder(List<Periode> trekkKravPerioder) {
        this.trekkKravPerioder.addAll(Objects.requireNonNull(trekkKravPerioder, "trekkKravPerioder"));
        return this;
    }

    public PleipengerLivetsSluttfase medArbeidstid(Arbeidstid arbeidstid) {
        this.arbeidstid = Objects.requireNonNull(arbeidstid, "arbeidstid");
        return this;
    }

    public PleipengerLivetsSluttfase medUttak(Uttak uttak) {
        this.uttak = Objects.requireNonNull(uttak, "uttak");
        return this;
    }

    public LovbestemtFerie getLovbestemtFerie() {
        return lovbestemtFerie;
    }

    public PleipengerLivetsSluttfase medLovbestemtFerie(LovbestemtFerie lovbestemtFerie) {
        this.lovbestemtFerie = Objects.requireNonNull(lovbestemtFerie, "lovbestemtFerie");
        return this;
    }

    @AssertTrue
    public boolean skalHaOpplysningOmOpptjeningVedNyPeriode() {
        if (søknadsperiode != null && !søknadsperiode.isEmpty()) {
            return opptjeningAktivitet != null;
        }
        return true;
    }

}
