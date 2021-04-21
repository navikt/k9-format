package no.nav.k9.søknad.ytelse.psb.v1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    @JsonProperty(value = "barn", required = true)
    @NotNull
    private Barn barn;

    @Valid
    @JsonProperty(value = "søknadsperiodeList", required = true)
    private List<Periode> søknadsperiodeList = new ArrayList<>();

    @Valid
    @JsonProperty(value = "endringsperiodeList", required = true)
    private List<Periode> endringsperiodeList = new ArrayList<>();

    @Valid
    @JsonProperty(value = "opptjeningAktivitet")
    private OpptjeningAktivitet opptjeningAktivitet;

    @Valid
    @JsonProperty(value = "dataBruktTilUtledning")
    private DataBruktTilUtledning dataBruktTilUtledning;

    @JsonProperty(value = "infoFraPunsj")
    @Valid
    private InfoFraPunsj infoFraPunsj;

    @Valid
    @JsonProperty(value = "bosteder")
    private Bosteder bosteder;

    @Valid
    @JsonProperty(value = "utenlandsopphold")
    private Utenlandsopphold utenlandsopphold;

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

    @JsonProperty(value = "omsorg", required = true)
    @Valid
    private Omsorg omsorg = new Omsorg();

    public PleiepengerSyktBarn() {
    }

    public Barn getBarn() {
        return barn;
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
    
    public PleiepengerSyktBarn medBarn(Barn barn) {
        this.barn = barn;
        return this;
    }

    @Override
    public Periode getSøknadsperiode() {
        final List<Periode> perioder = new ArrayList<>(søknadsperiodeList);
        perioder.addAll(endringsperiodeList);

        final var fom = søknadsperiodeList
                .stream()
                .map(Periode::getFraOgMed)
                .min(LocalDate::compareTo)
                .orElseThrow();
        final var tom = søknadsperiodeList
                .stream()
                .map(Periode::getTilOgMed)
                .max(LocalDate::compareTo)
                .orElseThrow();
        return new Periode(fom, tom);
    }

    public List<Periode> getSøknadsperiodeList() {
        return søknadsperiodeList == null? null: Collections.unmodifiableList(søknadsperiodeList);
    }

    public PleiepengerSyktBarn medSøknadsperiodeList(List<Periode> søknadsperiodeList) {
        if (this.søknadsperiodeList == null)
            this.søknadsperiodeList = new ArrayList<>();
        this.søknadsperiodeList.addAll(søknadsperiodeList);
        return this;
    }

    public PleiepengerSyktBarn medSøknadsperiode(Periode søknadsperiode) {
        if (this.søknadsperiodeList == null)
            this.søknadsperiodeList = new ArrayList<>();
        this.søknadsperiodeList.add(søknadsperiode);
        return this;
    }

    public List<Periode> getEndringsperiodeList() {
        return endringsperiodeList == null? null: Collections.unmodifiableList(endringsperiodeList);
    }

    public PleiepengerSyktBarn medEndringsperiodeList(List<Periode> endringsperiodeList) {
        if (this.endringsperiodeList == null)
            this.endringsperiodeList = new ArrayList<>();
        this.endringsperiodeList.addAll(endringsperiodeList);
        return this;
    }

    public PleiepengerSyktBarn medEndringsperiode(Periode endringsperiode) {
        if (this.endringsperiodeList == null)
            this.endringsperiodeList = new ArrayList<>();
        this.endringsperiodeList.add(endringsperiode);
        return this;
    }

    public OpptjeningAktivitet getOpptjeningAktivitet() {
        return opptjeningAktivitet;
    }

    public PleiepengerSyktBarn medOpptjeningAktivitet(OpptjeningAktivitet arbeidAktivitet) {
        this.opptjeningAktivitet = arbeidAktivitet;
        return this;
    }

    public DataBruktTilUtledning getSøknadInfo() {
        return dataBruktTilUtledning;
    }

    public PleiepengerSyktBarn medSøknadInfo(DataBruktTilUtledning dataBruktTilUtledning) {
        this.dataBruktTilUtledning = dataBruktTilUtledning;
        return this;
    }

    public InfoFraPunsj getInfoFraPunsj() {
        return infoFraPunsj;
    }

    public PleiepengerSyktBarn medInfoFraPunsj(InfoFraPunsj infoFraPunsj) {
        this.infoFraPunsj = infoFraPunsj;
        return this;
    }

    public Bosteder getBosteder() {
        return bosteder;
    }

    public PleiepengerSyktBarn medBosteder(Bosteder bosteder) {
        this.bosteder = bosteder;
        return this;
    }

    public Utenlandsopphold getUtenlandsopphold() {
        return utenlandsopphold;
    }

    public PleiepengerSyktBarn medUtenlandsopphold(Utenlandsopphold utenlandsopphold) {
        this.utenlandsopphold = utenlandsopphold;
        return this;
    }

    public Beredskap getBeredskap() {
        return beredskap;
    }

    public PleiepengerSyktBarn medBeredskap(Beredskap beredskap) {
        this.beredskap = beredskap;
        return this;
    }

    public Nattevåk getNattevåk() {
        return nattevåk;
    }

    public PleiepengerSyktBarn medNattevåk(Nattevåk nattevåk) {
        this.nattevåk = nattevåk;
        return this;
    }

    public Tilsynsordning getTilsynsordning() {
        return tilsynsordning;
    }

    public PleiepengerSyktBarn medTilsynsordning(Tilsynsordning tilsynsordning) {
        this.tilsynsordning = tilsynsordning;
        return this;
    }

    public LovbestemtFerie getLovbestemtFerie() {
        return lovbestemtFerie;
    }

    public PleiepengerSyktBarn medLovbestemtFerie(LovbestemtFerie lovbestemtFerie) {
        this.lovbestemtFerie = lovbestemtFerie;
        return this;
    }

    public Arbeidstid getArbeidstid() {
        return arbeidstid;
    }

    public PleiepengerSyktBarn medArbeidstid(Arbeidstid arbeidstid) {
        this.arbeidstid = arbeidstid;
        return this;
    }

    public Uttak getUttak() {
        return uttak;
    }

    public PleiepengerSyktBarn medUttak(Uttak uttak) {
        this.uttak = uttak;
        return this;
    }

    public Omsorg getOmsorg() {
        return this.omsorg;
    }

    public PleiepengerSyktBarn medOmsorg(Omsorg omsorg) {
        this.omsorg = omsorg;
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
        return new PleiepengerSyktBarnValidator();
    }

    @Size(max=0, message="${validatedValue}")
    private List<Feil> getValiderAngittUtenlandsopphold() {
        return utenlandsopphold == null
            ? List.of()
            : new PeriodeValidator().validerIkkeTillattOverlapp(utenlandsopphold.getPerioder(), "utenlandsopphold.perioder");
    }

    @Size(max=0, message="${validatedValue}")
    private List<Feil> getValiderAngittBosteder() {
        return bosteder == null
            ? List.of()
            : new PeriodeValidator().validerIkkeTillattOverlapp(bosteder.getPerioder(), "bosteder.perioder");
    }

}
