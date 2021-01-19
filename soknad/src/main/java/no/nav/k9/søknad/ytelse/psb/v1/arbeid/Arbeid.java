package no.nav.k9.søknad.ytelse.psb.v1.arbeid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Arbeid {

    //TODO legge inn støtte for SN FL

    @Valid
    @NotNull
    @JsonProperty(value = "arbedstaker")
    private List<Arbeidstaker> arbeidstaker;

    @JsonCreator
    public Arbeid(@JsonProperty(value = "arbeidstaker") @Valid List<Arbeidstaker> arbeidstaker) {
        this.arbeidstaker = arbeidstaker;
    }

    public List<Arbeidstaker> getArbeidstaker() {
        return arbeidstaker;
    }

    public void setArbeidstaker(List<Arbeidstaker> arbeidstaker) {
        this.arbeidstaker = arbeidstaker;
    }


}
