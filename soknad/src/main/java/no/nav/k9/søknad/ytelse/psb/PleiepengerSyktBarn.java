package no.nav.k9.søknad.ytelse.psb;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.k9.søknad.felles.LovbestemtFerie;
import no.nav.k9.søknad.felles.opptjening.Opptjening;
import no.nav.k9.søknad.felles.opptjening.arbeidstaker.Arbeidstaker;
import no.nav.k9.søknad.felles.personopplysninger.Barn;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.psb.tilsyn.Tilsynsordning;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(Ytelse.PLEIEPENGER_SYKT_BARN)
public class PleiepengerSyktBarn implements Ytelse {

    @Valid
    @JsonProperty(value = "beredskap")
    private Beredskap beredskap;

    @Valid
    @JsonProperty(value = "nattevåk")
    private Nattevåk nattevåk;

    @Valid
    @NotNull
    @JsonProperty(value = "tilsynsordning", required = true)
    private Tilsynsordning tilsynsordning;

    @Valid
    @JsonProperty(value = "lovbestemtFerie")
    private LovbestemtFerie lovbestemtFerie;

    @Valid
    @JsonProperty(value = "barn")
    private Barn barn;

    @Valid
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "perioder", required = true)
    private Map<Periode, SøknadsperiodeInfo> perioder;

    @Valid
    @JsonProperty(value = "arbeid")
    private Opptjening opptjening;

    @Valid
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "arbeidstaker")
    private List<Arbeidstaker> arbeidstaker;

    @JsonCreator
    public PleiepengerSyktBarn(@JsonProperty(value = "perioder", required = true) @Valid Map<Periode, SøknadsperiodeInfo> perioder,
                               @JsonProperty(value = "barn") @Valid Barn barn,
                               @JsonProperty(value = "beredskap") @Valid Beredskap beredskap,
                               @JsonProperty(value = "nattevåk") @Valid Nattevåk nattevåk,
                               @JsonProperty(value = "tilsynsordning", required = true) @NotNull @Valid Tilsynsordning tilsynsordning,
                               @JsonProperty(value = "opptjening") @Valid Opptjening opptjening,
                               @JsonProperty(value = "arbeidstaker") @Valid List<Arbeidstaker> arbeidstaker,
                               @JsonProperty(value = "lovbestemtFerie") @Valid LovbestemtFerie lovbestemtFerie) {
        this.perioder = perioder;
        this.barn = barn;
        this.beredskap = beredskap;
        this.nattevåk = nattevåk;
        this.tilsynsordning = tilsynsordning;
        this.opptjening = opptjening;
        this.arbeidstaker = arbeidstaker;
        this.lovbestemtFerie = lovbestemtFerie;
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

    public Opptjening getOpptjening() {
        return opptjening;
    }

    public void setOpptjening(Opptjening opptjening) {
        this.opptjening = opptjening;
    }

    public List<Arbeidstaker> getArbeidstaker() {
        return arbeidstaker;
    }

    public void setArbeidstaker(List<Arbeidstaker> arbeidstaker) {
        this.arbeidstaker = arbeidstaker;
    }

    public Map<Periode, SøknadsperiodeInfo> getPerioder() {
        if (perioder == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(perioder);
    }

    public void setPerioder(Map<Periode, SøknadsperiodeInfo> perioder) {
        this.perioder = perioder;
    }

    public Barn getBarn(){
        return this.barn;
    }

    public void setBarn(Barn barn) {
        this.barn = barn;
    }

    @Override
    public Type getType() {
        return Type.PLEIEPENGER_SYKT_BARN;
    }

    @Override
    public YtelseValidator getValidator() {
        return new PleiepengerSyktBarnValidator();
    }

}
