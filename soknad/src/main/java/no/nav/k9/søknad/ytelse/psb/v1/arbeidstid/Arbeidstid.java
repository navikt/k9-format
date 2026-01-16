package no.nav.k9.søknad.ytelse.psb.v1.arbeidstid;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Arbeidstid {

    @JsonProperty(value = "arbeidstakerList", required = true)
    @NotNull
    private List<@NotNull @Valid Arbeidstaker> arbeidstakerList = new ArrayList<>();

    @Valid
    @JsonProperty(value = "frilanserArbeidstidInfo")
    private ArbeidstidInfo frilanserArbeidstidInfo;

    @Valid
    @JsonProperty(value = "selvstendigNæringsdrivendeArbeidstidInfo")
    private ArbeidstidInfo selvstendigNæringsdrivendeArbeidstidInfo;

    public Arbeidstid() {
    }

    public List<Arbeidstaker> getArbeidstakerList() {
        return unmodifiableList(arbeidstakerList);
    }

    public Arbeidstid medArbeidstaker(List<Arbeidstaker> arbeidstakerList) {
        this.arbeidstakerList = Objects.requireNonNull(arbeidstakerList, "arbeidstakerList");
        return this;
    }

    public Arbeidstid leggeTilArbeidstaker(List<Arbeidstaker> arbeidstakerList) {
        Objects.requireNonNull(arbeidstakerList, "arbeidstakerList");
        this.arbeidstakerList.addAll(arbeidstakerList);
        return this;
    }

    public Arbeidstid leggeTilArbeidstaker(Arbeidstaker arbeidstaker) {
        this.arbeidstakerList.add(Objects.requireNonNull(arbeidstaker, "arbeidstaker"));
        return this;
    }

    public Optional<ArbeidstidInfo> getFrilanserArbeidstidInfo() {
        return Optional.ofNullable(frilanserArbeidstidInfo);
    }

    public Arbeidstid medFrilanserArbeidstid(ArbeidstidInfo frilanserArbeidstidInfo) {
        this.frilanserArbeidstidInfo = Objects.requireNonNull(frilanserArbeidstidInfo, "Arbeidstid.frilanserArbeidstidInfo");
        return this;
    }

    public Optional<ArbeidstidInfo> getSelvstendigNæringsdrivendeArbeidstidInfo() {
        return Optional.ofNullable(selvstendigNæringsdrivendeArbeidstidInfo);
    }

    public Arbeidstid medSelvstendigNæringsdrivendeArbeidstidInfo(ArbeidstidInfo selvstendigNæringsdrivendeArbeidstidInfo) {
        this.selvstendigNæringsdrivendeArbeidstidInfo = Objects.requireNonNull(selvstendigNæringsdrivendeArbeidstidInfo, "Arbeidstid.selvstendigNæringsdrivendeArbeidstidInfo");
        return this;
    }
}
