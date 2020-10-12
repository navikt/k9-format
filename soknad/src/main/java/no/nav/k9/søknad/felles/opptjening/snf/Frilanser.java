package no.nav.k9.søknad.felles.opptjening.snf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Frilanser {

    @JsonProperty("startdato")
    @NotNull
    public final LocalDate startdato;

    @JsonProperty("jobberFortsattSomFrilans")
    public final Boolean jobberFortsattSomFrilans;

    public static Frilanser.Builder builder() {
        return new Frilanser.Builder();
    }

    @JsonCreator
    public Frilanser(
            @JsonProperty("startdato") LocalDate startdato,
            @JsonProperty("jobberFortsattSomFrilans") Boolean jobberFortsattSomFrilans
    ) {
        this.startdato = startdato;
        this.jobberFortsattSomFrilans = jobberFortsattSomFrilans;
    }

    public static final class Builder {
        private LocalDate startdato;
        private Boolean jobberFortsattSomFrilans;

        private Builder() {
        }

        public Builder startdato(LocalDate startdato) {
            this.startdato = startdato;
            return this;
        }

        public Frilanser.Builder jobberFortsattSomFrilans(Boolean jobberFortsattSomFrilans) {
            this.jobberFortsattSomFrilans = jobberFortsattSomFrilans;
            return this;
        }

        public Frilanser build() {
            return new Frilanser(startdato, jobberFortsattSomFrilans);
        }
    }
}
