package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.aktivitet.Arbeidstaker;

import javax.validation.Valid;
import java.util.*;

import static java.util.Collections.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Arbeidstid {

    @Valid
    @JsonProperty(value = "arbeidstakerList", required = true)
    private List<Arbeidstaker> arbeidstakerList;

    @Valid
    @JsonProperty(value = "frilanserArbeidstidInfo", required = true)
    private ArbeidstidInfo frilanserArbeidstidInfo;

    @Valid
    @JsonProperty(value = "selvstendigNæringsdrivendeArbeidstidInfo", required = true)
    private ArbeidstidInfo selvstendigNæringsdrivendeArbeidstidInfo;

    @JsonCreator
    public Arbeidstid(@JsonProperty(value = "arbeidstakerList", required = true) @Valid List<Arbeidstaker> arbeidstakerList,
                      @JsonProperty(value = "frilanserArbeidstidInfo", required = true) @Valid ArbeidstidInfo frilanserArbeidstidInfo,
                      @JsonProperty(value = "selvstendigNæringsdrivendeArbeidstidInfo", required = true) @Valid ArbeidstidInfo selvstendigNæringsdrivendeArbeidstidInfo) {
        this.arbeidstakerList = new ArrayList<>(arbeidstakerList);
        this.frilanserArbeidstidInfo = frilanserArbeidstidInfo;
        this.selvstendigNæringsdrivendeArbeidstidInfo = selvstendigNæringsdrivendeArbeidstidInfo;
    }

    public Arbeidstid() {
    }

    public List<Arbeidstaker> getArbeidstakerList() {
        return unmodifiableList(arbeidstakerList);
    }

    public Arbeidstid medArbeidstakerList(List<Arbeidstaker> arbeidstakerList) {
        this.arbeidstakerList = new ArrayList<>(arbeidstakerList);
        return this;
    }

    public Arbeidstid leggeTilArbeidstaker(List<Arbeidstaker> arbeidstakerList) {
        this.arbeidstakerList.addAll(arbeidstakerList);
        return this;
    }

    public Arbeidstid leggeTilArbeidstaker(Arbeidstaker arbeidstaker) {
        this.arbeidstakerList.add(arbeidstaker);
        return this;
    }

    public ArbeidstidInfo getFrilanserArbeidstidInfo() {
        return frilanserArbeidstidInfo;
    }

    public Arbeidstid medFrilanserArbeidstid(ArbeidstidInfo frilanserArbeidstidInfo) {
        this.frilanserArbeidstidInfo = frilanserArbeidstidInfo;
        return this;
    }

    public ArbeidstidInfo getSelvstendigNæringsdrivendeArbeidstidInfo() {
        return selvstendigNæringsdrivendeArbeidstidInfo;
    }

    public Arbeidstid medSelvstendigNæringsdrivendeArbeidstidInfo(ArbeidstidInfo selvstendigNæringsdrivendeArbeidstidInfo) {
        this.selvstendigNæringsdrivendeArbeidstidInfo = selvstendigNæringsdrivendeArbeidstidInfo;
        return this;
    }
}
