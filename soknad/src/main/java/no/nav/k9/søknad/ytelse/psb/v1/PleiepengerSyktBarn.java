package no.nav.k9.søknad.ytelse.psb.v1;

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
    @JsonProperty(value = "søknadsperiodeList")
    private List<Periode> søknadsperiodeList;

    @Valid
    @JsonProperty(value = "endringsperiodeList")
    private List<Periode> endringsperiodeList;

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
    @JsonProperty(value = "beredskap")
    private Beredskap beredskap;

    @Valid
    @JsonProperty(value = "nattevåk")
    private Nattevåk nattevåk;

    @Valid
    @JsonProperty(value = "tilsynsordning")
    private Tilsynsordning tilsynsordning;

    @Valid
    @JsonProperty(value = "lovbestemtFerie")
    private LovbestemtFerie lovbestemtFerie;

    @Valid
    @JsonProperty(value = "arbeidstid")
    private Arbeidstid arbeidstid;

    @Valid
    @JsonProperty(value = "uttak")
    private Uttak uttak;

    @JsonProperty(value = "omsorg")
    @Valid
    private Omsorg omsorg;

    public PleiepengerSyktBarn() {
    }
    
    @JsonCreator
    public PleiepengerSyktBarn(@JsonProperty(value = "søknadsperiodeList") @Valid List<Periode> søknadsperiodeList,
                               @JsonProperty(value = "endringsperiodeList") @Valid List<Periode> endringsperiodeList,
                               @JsonProperty(value = "dataBruktTilUtledning") @Valid DataBruktTilUtledning dataBruktTilUtledning,
                               @JsonProperty(value = "infoFraPunsj") @Valid InfoFraPunsj infoFraPunsj,
                               @JsonProperty(value = "barn", required = true) @NotNull @Valid Barn barn,
                               @JsonProperty(value = "opptjeningAktivitet") @Valid OpptjeningAktivitet opptjeningAktivitet1,
                               @JsonProperty(value = "beredskap") @Valid Beredskap beredskap,
                               @JsonProperty(value = "nattevåk") @Valid Nattevåk nattevåk,
                               @JsonProperty(value = "tilsynsordning") @Valid Tilsynsordning tilsynsordning,
                               @JsonProperty(value = "arbeidstid") @Valid Arbeidstid arbeidstid,
                               @JsonProperty(value = "uttak") @Valid Uttak uttak,
                               @JsonProperty(value = "omsorg") @Valid Omsorg omsorg,
                               @JsonProperty(value = "lovbestemtFerie") @Valid LovbestemtFerie lovbestemtFerie,
                               @JsonProperty(value = "bosteder") @Valid Bosteder bosteder,
                               @JsonProperty(value = "utenlandsopphold") @Valid Utenlandsopphold utenlandsopphold) {
        this.søknadsperiodeList = søknadsperiodeList;
        this.endringsperiodeList = endringsperiodeList;
        this.dataBruktTilUtledning = dataBruktTilUtledning;
        this.infoFraPunsj = infoFraPunsj;
        this.barn = Objects.requireNonNull(barn, "barn");
        this.opptjeningAktivitet = opptjeningAktivitet1;
        this.beredskap = beredskap;
        this.nattevåk = nattevåk;
        this.tilsynsordning = tilsynsordning;
        this.arbeidstid = arbeidstid;
        this.uttak = uttak;
        this.omsorg = omsorg;
        this.lovbestemtFerie = lovbestemtFerie;
        this.bosteder = bosteder;
        this.utenlandsopphold = utenlandsopphold;
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
        if(søknadsperiodeList == null || søknadsperiodeList.size() != 1) {
            return null;
        }
        return søknadsperiodeList.get(0);
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
