package no.nav.k9.søknad.ytelse.psb.v1;

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
import no.nav.k9.søknad.felles.LovbestemtFerie;
import no.nav.k9.søknad.felles.aktivitet.ArbeidAktivitet;
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
    @NotNull
    @JsonProperty(value = "søknadsperiode", required = true)
    private Periode søknadsperiode;

    @Valid
    @JsonProperty(value = "arbeidAktivitet")
    private ArbeidAktivitet arbeidAktivitet;

    @Valid
    @JsonProperty(value = "søknadInfo", required = true)
    private SøknadInfo søknadInfo;

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
    @NotNull
    @JsonProperty(value = "uttak", required = true)
    private Uttak uttak;

    public PleiepengerSyktBarn() {
    }
    
    @JsonCreator
    public PleiepengerSyktBarn(@JsonProperty(value = "søknadsperiode", required = true) @NotNull @Valid Periode søknadsperiode,
                               @JsonProperty(value = "søknadInfo", required = true) @Valid SøknadInfo søknadInfo,
                               @JsonProperty(value = "barn", required = true) @NotNull @Valid Barn barn,
                               @JsonProperty(value = "arbeidAktivitet") @Valid ArbeidAktivitet aktivitet,
                               @JsonProperty(value = "beredskap") @Valid Beredskap beredskap,
                               @JsonProperty(value = "nattevåk") @Valid Nattevåk nattevåk,
                               @JsonProperty(value = "tilsynsordning") @Valid Tilsynsordning tilsynsordning,
                               @JsonProperty(value = "arbeidstid") @Valid Arbeidstid arbeidstid,
                               @JsonProperty(value = "uttak", required = true) @Valid @NotNull Uttak uttak,
                               @JsonProperty(value = "lovbestemtFerie") @Valid LovbestemtFerie lovbestemtFerie,
                               @JsonProperty(value = "bosteder") @Valid @NotNull Bosteder bosteder,
                               @JsonProperty(value = "utenlandsopphold") @Valid @NotNull Utenlandsopphold utenlandsopphold) {
        this.søknadsperiode = Objects.requireNonNull(søknadsperiode, "søknadsperiode");
        this.søknadInfo = søknadInfo;
        this.barn = Objects.requireNonNull(barn, "barn");
        this.arbeidAktivitet = aktivitet;
        this.beredskap = beredskap;
        this.nattevåk = nattevåk;
        this.tilsynsordning = tilsynsordning;
        this.arbeidstid = arbeidstid;
        this.uttak = uttak;
        this.lovbestemtFerie = lovbestemtFerie;
        this.bosteder = bosteder;
        this.utenlandsopphold = utenlandsopphold;
    }

    public Barn getBarn() {
        return barn;
    }

    public void setBarn(Barn barn) {
        this.barn = barn;
    }

    @Override
    public Periode getSøknadsperiode() {
        return søknadsperiode;
    }

    public void setSøknadsperiode(Periode søknadsperiode) {
        this.søknadsperiode = søknadsperiode;
    }

    public ArbeidAktivitet getArbeidAktivitet() {
        return arbeidAktivitet;
    }

    public void setArbeidAktivitet(ArbeidAktivitet arbeidAktivitet) {
        this.arbeidAktivitet = arbeidAktivitet;
    }

    public SøknadInfo getSøknadInfo() {
        return søknadInfo;
    }

    public void setSøknadInfo(SøknadInfo søknadInfo) {
        this.søknadInfo = søknadInfo;
    }

    public Bosteder getBosteder() {
        return bosteder;
    }

    public void setBosteder(Bosteder bosteder) {
        this.bosteder = bosteder;
    }

    public Utenlandsopphold getUtenlandsopphold() {
        return utenlandsopphold;
    }

    public void setUtenlandsopphold(Utenlandsopphold utenlandsopphold) {
        this.utenlandsopphold = utenlandsopphold;
    }

    public Beredskap getBeredskap() {
        return beredskap;
    }

    public void setBeredskap(Beredskap beredskap) {
        this.beredskap = beredskap;
    }

    public Nattevåk getNattevåk() {
        return nattevåk;
    }

    public void setNattevåk(Nattevåk nattevåk) {
        this.nattevåk = nattevåk;
    }

    public Tilsynsordning getTilsynsordning() {
        return tilsynsordning;
    }

    public void setTilsynsordning(Tilsynsordning tilsynsordning) {
        this.tilsynsordning = tilsynsordning;
    }

    public LovbestemtFerie getLovbestemtFerie() {
        return lovbestemtFerie;
    }

    public void setLovbestemtFerie(LovbestemtFerie lovbestemtFerie) {
        this.lovbestemtFerie = lovbestemtFerie;
    }

    public Arbeidstid getArbeidstid() {
        return arbeidstid;
    }

    public void setArbeidstid(Arbeidstid arbeidstid) {
        this.arbeidstid = arbeidstid;
    }

    public Uttak getUttak() {
        return uttak;
    }

    public void setUttak(Uttak uttak) {
        this.uttak = uttak;
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
            : new PeriodeValidator().validerIkkeTillattOverlapp(utenlandsopphold.perioder, "utenlandsopphold.perioder");
    }

    @Size(max=0, message="${validatedValue}")
    private List<Feil> getValiderAngittBosteder() {
        return bosteder == null
            ? List.of()
            : new PeriodeValidator().validerIkkeTillattOverlapp(bosteder.perioder, "bosteder.perioder");
    }

}
