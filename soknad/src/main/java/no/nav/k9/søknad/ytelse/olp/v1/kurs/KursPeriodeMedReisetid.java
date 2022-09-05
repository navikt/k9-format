package no.nav.k9.søknad.ytelse.olp.v1.kurs;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class KursPeriodeMedReisetid {

    @JsonProperty(value = "periode", required = true)
    @NotNull
    @Valid
    private Periode periode;

    @JsonProperty(value = "avreise", required = true)
    @NotNull
    @Valid
    private LocalDate avreise;

    @JsonProperty(value = "hjemkomst", required = true)
    @NotNull
    @Valid
    private LocalDate hjemkomst;

    public KursPeriodeMedReisetid() {
    }

    public KursPeriodeMedReisetid(Periode periode, LocalDate avreise, LocalDate hjemkomst) {
        this.periode = periode;
        this.avreise = avreise;
        this.hjemkomst = hjemkomst;
    }

    public Periode getPeriode() {
        return periode;
    }

    public LocalDate getAvreise() {
        return avreise;
    }

    public LocalDate getHjemkomst() {
        return hjemkomst;
    }
}
