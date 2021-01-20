package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.aktivitet.Arbeidstaker;
import no.nav.k9.søknad.felles.aktivitet.Frilanser;
import no.nav.k9.søknad.felles.aktivitet.SelvstendigNæringsdrivende;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Arbeidstid {

    @Valid
    @NotNull
    @JsonProperty(value = "arbedstakerMap")
    private Map<Arbeidstaker, ArbeidstidInfo> arbeidstakerMap;

    @Valid
    @NotNull
    @Size(max = 1)
    @JsonProperty(value = "frilanserMap")
    private Map<Frilanser, ArbeidstidInfo> frilanserMap;

    @Valid
    @NotNull
    @Size(max = 1)
    @JsonProperty(value = "selvstendigNæringsdrivendeMap")
    private Map<SelvstendigNæringsdrivende, ArbeidstidInfo> selvstendigNæringsdrivendeMap;

    @JsonCreator
    public Arbeidstid(@JsonProperty("arbedstakerMap") @Valid @NotNull Map<Arbeidstaker, ArbeidstidInfo> arbeidstakerMap,
                      @JsonProperty("frilanserMap") @Valid @NotNull @Size(max = 1) Map<Frilanser, ArbeidstidInfo> frilanserMap,
                      @JsonProperty(value = "selvstendigNæringsdrivendeMap") @Valid @NotNull @Size(max = 1) Map<SelvstendigNæringsdrivende, ArbeidstidInfo> selvstendigNæringsdrivendeMap) {
        this.arbeidstakerMap = arbeidstakerMap;
        this.frilanserMap = frilanserMap;
        this.selvstendigNæringsdrivendeMap = selvstendigNæringsdrivendeMap;
    }

    public Map<Arbeidstaker, ArbeidstidInfo> getArbeidstakerMap() {
        return arbeidstakerMap;
    }

    public void setArbeidstakerMap(Map<Arbeidstaker, ArbeidstidInfo> arbeidstakerMap) {
        this.arbeidstakerMap = arbeidstakerMap;
    }

    public Map<Frilanser, ArbeidstidInfo> getFrilanserMap() {
        return frilanserMap;
    }

    public void setFrilanserMap(Map<Frilanser, ArbeidstidInfo> frilanserMap) {
        this.frilanserMap = frilanserMap;
    }

    public Map<SelvstendigNæringsdrivende, ArbeidstidInfo> getSelvstendigNæringsdrivendeMap() {
        return selvstendigNæringsdrivendeMap;
    }

    public void setSelvstendigNæringsdrivendeMap(Map<SelvstendigNæringsdrivende, ArbeidstidInfo> selvstendigNæringsdrivendeMap) {
        this.selvstendigNæringsdrivendeMap = selvstendigNæringsdrivendeMap;
    }
}
