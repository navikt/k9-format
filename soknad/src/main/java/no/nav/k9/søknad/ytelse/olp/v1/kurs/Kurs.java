package no.nav.k9.søknad.ytelse.olp.v1.kurs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @JsonProperty(value = "formål", required = true)
    @NotNull
    @Valid
    private String formål;

    @JsonProperty(value = "kursperioder", required = true)
    @NotNull
    @Size(min = 1)
    @Valid
    private List<KursPeriodeMedReisetid> kursperioder;

    public Kurs() {
    }

    public Kurs(Kursholder kursholder, String formålMedKurset, List<KursPeriodeMedReisetid> kursperioder) {
        this.kursholder = kursholder;
        this.formål = formålMedKurset;
        this.kursperioder = kursperioder;
    }

    public Kursholder getKursholder() {
        return kursholder;
    }

    public String getFormål() {
        return formål;
    }

    public List<KursPeriodeMedReisetid> getKursperioder() {
        return new ArrayList<>(kursperioder);
    }
}
