package no.nav.k9.innsyn;

import java.util.Objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(InnsynHendelseData.OMSORG)
public class Omsorg implements InnsynHendelseData {

    @JsonProperty(value = "søkerAktørId", required = true)
    @Valid
    @NotNull
    @Size(max = 20)
    @Pattern(regexp = "^\\d+$", message = "søkerAktørId [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String søkerAktørId;
    
    @JsonProperty(value = "pleietrengendeAktørId", required = true)
    @Valid
    @NotNull
    @Size(max = 20)
    @Pattern(regexp = "^\\d+$", message = "pleietrengendeAktørId [${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String pleietrengendeAktørId;
    
    @JsonProperty(value = "harOmsorgen", required = true)
    @Valid
    @NotNull
    private boolean harOmsorgen;
    
    
    protected Omsorg() {
        
    }
    
    public Omsorg(String søkerAktørId, String pleietrengendeAktørId, boolean harOmsorgen) {
        this.søkerAktørId = Objects.requireNonNull(søkerAktørId, "søkerAktørId");
        this.pleietrengendeAktørId = Objects.requireNonNull(pleietrengendeAktørId, "pleietrengendeAktørId");
        if (søkerAktørId.equals(pleietrengendeAktørId)) {
            throw new IllegalArgumentException("søkerAktørId kan ikke være lik pleietrengendeAktørId");
        }
        this.harOmsorgen = harOmsorgen;
    }
    
    
    public String getSøkerAktørId() {
        return søkerAktørId;
    }
    
    public String getPleietrengendeAktørId() {
        return pleietrengendeAktørId;
    }
    
    public boolean isHarOmsorgen() {
        return harOmsorgen;
    }
}
