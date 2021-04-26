package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.*;

import static java.util.Collections.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Arbeidstid {

    @JsonProperty(value = "arbeidstakerList", required = true)
    @Valid
    @NotNull
    private List<Arbeidstaker> arbeidstakerList = new ArrayList<>();

    //TODO endre til Optional
    @Valid
    @JsonProperty(value = "frilanserArbeidstidInfo")
    private ArbeidstidInfo frilanserArbeidstidInfo;

    //TODO endre til Optional
    @Valid
    @JsonProperty(value = "selvstendigNæringsdrivendeArbeidstidInfo")
    private ArbeidstidInfo selvstendigNæringsdrivendeArbeidstidInfo;

    public Arbeidstid() {
    }

    public List<Arbeidstaker> getArbeidstakerList() {
        return unmodifiableList(arbeidstakerList);
    }

    public Arbeidstid medArbeidstaker(List<Arbeidstaker> arbeidstakerList) {
        this.arbeidstakerList = (arbeidstakerList == null ) ? new ArrayList<>() : new ArrayList<>(arbeidstakerList);
        return this;
    }

    public Arbeidstid leggeTilArbeidstaker(List<Arbeidstaker> arbeidstakerList) {
        this.arbeidstakerList.addAll(Objects.requireNonNull(arbeidstakerList, "Arbeidstid.arbeidstaker"));
        return this;
    }

    public Arbeidstid leggeTilArbeidstaker(Arbeidstaker arbeidstaker) {
        this.arbeidstakerList.add(Objects.requireNonNull(arbeidstaker, "Arbeidstid.arbeidstaker"));
        return this;
    }

    public ArbeidstidInfo getFrilanserArbeidstidInfo() {
        return frilanserArbeidstidInfo;
    }

    public Arbeidstid medFrilanserArbeidstid(ArbeidstidInfo frilanserArbeidstidInfo) {
        this.frilanserArbeidstidInfo = Objects.requireNonNull(frilanserArbeidstidInfo, "Arbeidstid.frilanserArbeidstidInfo");
        return this;
    }

    public ArbeidstidInfo getSelvstendigNæringsdrivendeArbeidstidInfo() {
        return selvstendigNæringsdrivendeArbeidstidInfo;
    }

    public Arbeidstid medSelvstendigNæringsdrivendeArbeidstidInfo(ArbeidstidInfo selvstendigNæringsdrivendeArbeidstidInfo) {
        this.selvstendigNæringsdrivendeArbeidstidInfo = Objects.requireNonNull(selvstendigNæringsdrivendeArbeidstidInfo, "Arbeidstid.selvstendigNæringsdrivendeArbeidstidInfo");
        return this;
    }
}
