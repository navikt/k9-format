package no.nav.k9.søknad.felles.aktivitet;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import no.nav.k9.søknad.felles.type.NorskIdentitetsnummer;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPeriode;
import static no.nav.k9.søknad.felles.type.Periode.Utils.leggTilPerioder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Arbeidstaker {

    @JsonProperty(value = "norskIdentitetsnummer")
    @Valid
    public final NorskIdentitetsnummer norskIdentitetsnummer;

    @JsonProperty(value = "organisasjonsnummer")
    @Valid
    public final Organisasjonsnummer organisasjonsnummer;

    @JsonCreator
    public Arbeidstaker(@JsonProperty(value = "norskIdentitetsnummer") NorskIdentitetsnummer norskIdentitetsnummer,
                         @JsonProperty(value = "organisasjonsnummer") Organisasjonsnummer organisasjonsnummer) {
        this.norskIdentitetsnummer = norskIdentitetsnummer;
        this.organisasjonsnummer = organisasjonsnummer;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private NorskIdentitetsnummer norskIdentitetsnummer;
        private Organisasjonsnummer organisasjonsnummer;

        private Builder() {
        }

        public Builder organisasjonsnummer(Organisasjonsnummer organisasjonsnummer) {
            this.organisasjonsnummer = organisasjonsnummer;
            return this;
        }

        public Builder norskIdentitetsnummer(NorskIdentitetsnummer norskIdentitetsnummer) {
            this.norskIdentitetsnummer = norskIdentitetsnummer;
            return this;
        }

        public Arbeidstaker build() {
            return new Arbeidstaker(
                norskIdentitetsnummer,
                organisasjonsnummer);
        }
    }
}
