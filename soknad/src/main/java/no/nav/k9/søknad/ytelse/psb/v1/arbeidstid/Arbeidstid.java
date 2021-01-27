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
        this.arbeidstakerList = (arbeidstakerList == null) ? emptyList() : unmodifiableList(arbeidstakerList);
        this.frilanserArbeidstidInfo = frilanserArbeidstidInfo;
        this.selvstendigNæringsdrivendeArbeidstidInfo = selvstendigNæringsdrivendeArbeidstidInfo;
    }

    public List<Arbeidstaker> getArbeidstakerList() {
        return arbeidstakerList;
    }

    public void leggeTilArbeidstaker(Arbeidstaker arbeidstaker, ArbeidstidInfo arbeidstidInfo) {
        Objects.requireNonNull(arbeidstaker);
        Objects.requireNonNull(arbeidstidInfo);
        arbeidstaker.setArbeidstidInfo(arbeidstidInfo);
        leggeTilArbeidstaker(List.of(arbeidstaker));
    }

    public void leggeTilArbeidstaker(List<Arbeidstaker> arbeidstakerList) {
        Objects.requireNonNull(arbeidstakerList);
        var temp = new ArrayList<>(arbeidstakerList);
        temp.addAll(this.getArbeidstakerList());
        this.arbeidstakerList = unmodifiableList(temp);
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
