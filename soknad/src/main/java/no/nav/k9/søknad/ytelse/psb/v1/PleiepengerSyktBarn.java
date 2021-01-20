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
import no.nav.k9.søknad.ytelse.psb.v1.arbeid.Arbeid;
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
    @JsonProperty(value = "flereOmsorgspersoner")
    private Boolean flereOmsorgspersoner;

    @Valid
    @JsonProperty(value = "relasjonTilBarnet")
    private String relasjonTilBarnet;

    @Valid
    @JsonProperty(value = "samtykketOmsorgForBarnet")
    private Boolean samtykketOmsorgForBarnet;

    @Valid
    @JsonProperty(value = "beskrivelseAvOmsorgsrollen")
    private String beskrivelseAvOmsorgsrollen;

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
    @JsonProperty(value = "arbeid")
    private Arbeid arbeid;

    @Valid
    @NotNull
    @JsonProperty(value = "uttak", required = true)
    private Uttak uttak;

    public PleiepengerSyktBarn() {
    }
    
    @JsonCreator
    public PleiepengerSyktBarn(@JsonProperty(value = "søknadsperiode", required = true) @NotNull @Valid Periode søknadsperiode,
                               @JsonProperty(value = "barn", required = true) @NotNull @Valid Barn barn,
                               @JsonProperty(value = "arbeidAktivitet") @Valid ArbeidAktivitet aktivitet,
                               @JsonProperty(value = "beredskap") @Valid Beredskap beredskap,
                               @JsonProperty(value = "nattevåk") @Valid Nattevåk nattevåk,
                               @JsonProperty(value = "tilsynsordning") @Valid Tilsynsordning tilsynsordning,
                               @JsonProperty(value = "arbeid") @Valid Arbeid arbeid,
                               @JsonProperty(value = "uttak", required = true) @Valid @NotNull Uttak uttak,
                               @JsonProperty(value = "lovbestemtFerie") @Valid LovbestemtFerie lovbestemtFerie,
                               @JsonProperty(value = "bosteder") @Valid @NotNull Bosteder bosteder,
                               @JsonProperty(value = "utenlandsopphold") @Valid @NotNull Utenlandsopphold utenlandsopphold,
                               @JsonProperty(value = "flereOmsorgspersoner") @Valid Boolean flereOmsorgspersoner,
                               @JsonProperty(value = "relasjonTilBarnet") @Valid String relasjonTilBarnet,
                               @JsonProperty(value = "samtykketOmsorgForBarnet") @Valid Boolean samtykketOmsorgForBarnet,
                               @JsonProperty(value = "beskrivelseAvOmsorgsrollen") @Valid String beskrivelseAvOmsorgsrollen) {
        this.søknadsperiode = Objects.requireNonNull(søknadsperiode, "søknadsperiode");
        this.barn = Objects.requireNonNull(barn, "barn");
        this.arbeidAktivitet = aktivitet;
        this.flereOmsorgspersoner = flereOmsorgspersoner;
        this.relasjonTilBarnet = relasjonTilBarnet;
        this.samtykketOmsorgForBarnet = samtykketOmsorgForBarnet;
        this.beskrivelseAvOmsorgsrollen = beskrivelseAvOmsorgsrollen;
        this.beredskap = beredskap;
        this.nattevåk = nattevåk;
        this.tilsynsordning = tilsynsordning;
        this.arbeid = arbeid;
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

    public Boolean getFlereOmsorgspersoner() {
        return flereOmsorgspersoner;
    }

    public void setFlereOmsorgspersoner(Boolean flereOmsorgspersoner) {
        this.flereOmsorgspersoner = flereOmsorgspersoner;
    }

    public String getRelasjonTilBarnet() {
        return relasjonTilBarnet;
    }

    public void setRelasjonTilBarnet(String relasjonTilBarnet) {
        this.relasjonTilBarnet = relasjonTilBarnet;
    }

    public Boolean getSamtykketOmsorgForBarnet() {
        return samtykketOmsorgForBarnet;
    }

    public void setSamtykketOmsorgForBarnet(Boolean samtykketOmsorgForBarnet) {
        this.samtykketOmsorgForBarnet = samtykketOmsorgForBarnet;
    }

    public String getBeskrivelseAvOmsorgsrollen() {
        return beskrivelseAvOmsorgsrollen;
    }

    public void setBeskrivelseAvOmsorgsrollen(String beskrivelseAvOmsorgsrollen) {
        this.beskrivelseAvOmsorgsrollen = beskrivelseAvOmsorgsrollen;
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

    public Arbeid getArbeid() {
        return arbeid;
    }

    public void setArbeid(Arbeid arbeid) {
        this.arbeid = arbeid;
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
