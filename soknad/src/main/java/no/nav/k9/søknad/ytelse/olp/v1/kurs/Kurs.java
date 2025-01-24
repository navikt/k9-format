package no.nav.k9.søknad.ytelse.olp.v1.kurs;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Kurs {

    @JsonProperty(value = "kursholder", required = true)
    @NotNull
    @Valid
    private Kursholder kursholder;

    @JsonProperty(value = "kursperioder", required = true)
    @NotNull
    @Size(min = 1)
    @Valid
    private List<@NotNull @Valid KursPeriode> kursperioder;

    @JsonProperty(value = "reise", required = true)
    @NotNull
    @Valid
    private Reise reise;

    public Kurs() {
    }

    public Kurs(Kursholder kursholder, List<KursPeriode> kursperioder, Reise reise) {
        this.kursholder = kursholder;
        this.kursperioder = kursperioder;
        this.reise = reise;
    }

    public Kursholder getKursholder() {
        return kursholder;
    }

    public List<KursPeriode> getKursperioder() {
        return new ArrayList<>(kursperioder);
    }
}
