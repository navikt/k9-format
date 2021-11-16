package no.nav.k9.innsyn;

import java.time.ZonedDateTime;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class InnsynHendelse<T> {
    
    @Valid
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    @JsonProperty(value = "oppdateringstidspunkt", required = true)
    private ZonedDateTime oppdateringstidspunkt;
    
    @JsonManagedReference
    @Valid
    @NotNull
    @JsonProperty(value = "data", required = true)
    private T data;
    
    
    protected InnsynHendelse() {
        
    }
    
    public InnsynHendelse(ZonedDateTime oppdateringstidspunkt, T data) {
        this.oppdateringstidspunkt = Objects.requireNonNull(oppdateringstidspunkt);
        this.data =  Objects.requireNonNull(data);
    }
    
    
    public ZonedDateTime getOppdateringstidspunkt() {
        return oppdateringstidspunkt;
    }
    
    public T getData() {
        return data;
    }
}
