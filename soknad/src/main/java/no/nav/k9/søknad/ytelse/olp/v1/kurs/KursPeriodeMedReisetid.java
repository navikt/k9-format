package no.nav.k9.søknad.ytelse.olp.v1.kurs;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo")
    private LocalDate avreise;

    @JsonProperty(value = "hjemkomst", required = true)
    @NotNull
    @Valid
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Oslo")
    private LocalDate hjemkomst;

    @JsonProperty(value = "begrunnelseReisetidTil")
    @Size(max = 4000)
    @Pattern(regexp = "^[\\p{Pd}\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}§]*$", message = "[${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String begrunnelseReisetidTil;

    @JsonProperty(value = "begrunnelseReisetidHjem")
    @Size(max = 4000)
    @Pattern(regexp = "^[\\p{Pd}\\p{Graph}\\p{Space}\\p{Sc}\\p{L}\\p{M}\\p{N}§]*$", message = "[${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    private String begrunnelseReisetidHjem;

    public KursPeriodeMedReisetid() {
    }

    public KursPeriodeMedReisetid(Periode periode, LocalDate avreise, LocalDate hjemkomst, String begrunnelseReisetidTil, String begrunnelseReisetidHjem) {
        this.periode = periode;
        this.avreise = avreise;
        this.hjemkomst = hjemkomst;
        this.begrunnelseReisetidTil = begrunnelseReisetidTil;
        this.begrunnelseReisetidHjem = begrunnelseReisetidHjem;
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

    public String getBegrunnelseReisetidTil() {
        return begrunnelseReisetidTil;
    }

    public String getBegrunnelseReisetidHjem() {
        return begrunnelseReisetidHjem;
    }
}
