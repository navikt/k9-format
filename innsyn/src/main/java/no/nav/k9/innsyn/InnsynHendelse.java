package no.nav.k9.innsyn;

import java.time.ZonedDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.felles.DtoKonstanter;

/**
 * En hendelse fra k9-sak med nye data som kan brukes i innsyn.
 *
 * @param <T> Innholdet i hendelsen.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class InnsynHendelse<T extends InnsynHendelseData> {
    
    /**
     * Tidspunktet hendelsen har blitt oppdatert. Hvis innsyn har mottatt en
     * hendelse for samme ting med et senere oppdateringstidspunkt så må
     * denne hendelsen ignoreres.
     * 
     * Merk at hva som identifiserer en gitt ting er avhengig av hendelsestype.
     */
    @Valid
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DtoKonstanter.DATO_TID_FORMAT, timezone = DtoKonstanter.TIDSSONE)    @JsonProperty(value = "oppdateringstidspunkt", required = true)
    private ZonedDateTime oppdateringstidspunkt;
    
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
