package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.aktivitet.Arbeidstaker;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Arbeidstid {

    @Valid
    @NotNull
    @JsonProperty(value = "arbeidstakerMap")
    private Map<Arbeidstaker, ArbeidstidInfo> arbeidstakerMap;

    @Valid
    @NotNull
    @JsonProperty(value = "frilanserArbeidstidInfo")
    private ArbeidstidInfo frilanserArbeidstidInfo;

    @Valid
    @NotNull
    @JsonProperty(value = "selvstendigNæringsdrivendeArbeidstidInfo")
    private ArbeidstidInfo selvstendigNæringsdrivendeArbeidstidInfo;

    @JsonCreator
    public Arbeidstid(@JsonProperty("arbeidstakerMap") @Valid @NotNull Map<Arbeidstaker, ArbeidstidInfo> arbeidstakerMap,
                      @JsonProperty("frilanserArbeidstidInfo") @Valid @NotNull ArbeidstidInfo frilanserArbeidstidInfo,
                      @JsonProperty(value = "selvstendigNæringsdrivendeArbeidstidInfo") @Valid @NotNull ArbeidstidInfo selvstendigNæringsdrivendeArbeidstidInfo) {
        this.arbeidstakerMap = (arbeidstakerMap == null) ? emptyMap() : unmodifiableMap(arbeidstakerMap);
        this.frilanserArbeidstidInfo = frilanserArbeidstidInfo;
        this.selvstendigNæringsdrivendeArbeidstidInfo = selvstendigNæringsdrivendeArbeidstidInfo;
    }

    public Map<Arbeidstaker, ArbeidstidInfo> getArbeidstakerMap() {
        return arbeidstakerMap;
    }

    public void setArbeidstakerMap(Map<Arbeidstaker, ArbeidstidInfo> arbeidstakerMap) {
        this.arbeidstakerMap = arbeidstakerMap;
    }

    public ArbeidstidInfo getFrilanserArbeidstidInfo() {
        return frilanserArbeidstidInfo;
    }

    public void setFrilanserArbeidstidInfo(ArbeidstidInfo frilanserArbeidstidInfo) {
        this.frilanserArbeidstidInfo = frilanserArbeidstidInfo;
    }

    public ArbeidstidInfo getSelvstendigNæringsdrivendeArbeidstidInfo() {
        return selvstendigNæringsdrivendeArbeidstidInfo;
    }

    public void setSelvstendigNæringsdrivendeArbeidstidInfo(ArbeidstidInfo selvstendigNæringsdrivendeArbeidstidInfo) {
        this.selvstendigNæringsdrivendeArbeidstidInfo = selvstendigNæringsdrivendeArbeidstidInfo;
    }
}
