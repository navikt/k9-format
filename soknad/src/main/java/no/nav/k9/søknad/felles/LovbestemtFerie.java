package no.nav.k9.søknad.felles;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.søknad.felles.type.Periode;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class LovbestemtFerie {

    @JsonProperty(value="perioder", required = true)
    @Valid
    @NotNull
    @NotEmpty
    private List<Periode> perioder;

    @JsonCreator
    public LovbestemtFerie(
            @JsonProperty(value = "perioder", required = true) @Valid @NotNull @NotEmpty
            List<Periode> perioder) {
        this.perioder = perioder;
    }

    public List<Periode> getPerioder() {
        return perioder;
    }

    public void setPerioder(List<Periode> perioder) {
        this.perioder = perioder;
    }
}
