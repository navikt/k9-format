package no.nav.k9.søknad.felles.opptjeningAktivitet;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.util.ClassUtil;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Frilanser {

    @JsonProperty(value = "startdato")
    @NotNull
    private LocalDate startdato;

    @JsonProperty(value = "sluttdato")
    private LocalDate sluttdato;

    @JsonProperty(value = "jobberFortsattSomFrilans")
    private Boolean jobberFortsattSomFrilans;

    /**@deprecated brukt ctor.*/
    @Deprecated(forRemoval = true)
    public static Frilanser.Builder builder() {
        return new Frilanser.Builder();
    }

    public Frilanser() {}

    @JsonCreator
    public Frilanser(
                     @JsonProperty("startdato") LocalDate startdato,
                     @JsonProperty("sluttdato") LocalDate sluttdato,
                     @JsonProperty("jobberFortsattSomFrilans") Boolean jobberFortsattSomFrilans) {
        this.startdato = startdato;
        this.sluttdato = sluttdato;
        this.jobberFortsattSomFrilans = jobberFortsattSomFrilans;
    }

    public Frilanser medStartDato(LocalDate startdato) {
        this.startdato = startdato;
        return this;
    }

    public Frilanser medSluttDato(LocalDate sluttdato) {
        this.sluttdato = sluttdato;
        return this;
    }

    public Frilanser medJobberFortsattSomFrilans(Boolean jobberFortsattSomFrilans) {
        this.jobberFortsattSomFrilans = jobberFortsattSomFrilans;
        return this;
    }
    
    public LocalDate getStartdato() {
        return startdato;
    }
    public LocalDate getSluttdato() { return sluttdato; }
    public Boolean getJobberFortsattSomFrilans() {
        return jobberFortsattSomFrilans;
    }

    /**@deprecated brukt ctor.*/
    @Deprecated(forRemoval = true)
    public static final class Builder {
        private LocalDate startdato;
        private LocalDate sluttdato;
        private Boolean jobberFortsattSomFrilans;


        private Builder() {
        }

        public Builder startdato(LocalDate startdato) {
            this.startdato = startdato;
            return this;
        }

        public Builder sluttdato(LocalDate sluttdato) {
            this.sluttdato = sluttdato;
            return this;
        }

        public Frilanser.Builder jobberFortsattSomFrilans(Boolean jobberFortsattSomFrilans) {
            this.jobberFortsattSomFrilans = jobberFortsattSomFrilans;
            return this;
        }

        public Frilanser build() {
            return new Frilanser(startdato, sluttdato, jobberFortsattSomFrilans);
        }
    }
}
