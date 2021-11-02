package no.nav.k9.søknad.ytelse.pls.v1;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.type.Periode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class FraværPeriode {

    @Valid
    @NotNull
    @JsonProperty(value = "periode", required = true)
    private final Periode periode;

    @Valid
    @NotNull
    @JsonProperty(value = "aktivitet", required = true)
    private final Aktivitet aktivitet;

    @Valid
    @JsonProperty("delvisFravær")
    private final DelvisFravær delvisFravær;

    @JsonCreator
    public FraværPeriode(
            @JsonProperty(value = "periode", required = true) @Valid Periode periode,
            @JsonProperty(value = "aktivitet", required = true) @Valid Aktivitet aktivitet,
            @JsonProperty(value = "arbeidstid") @Valid DelvisFravær delvisFravær) {
        this.periode = periode;
        this.aktivitet = aktivitet;
        this.delvisFravær = delvisFravær;
    }

    public Periode getPeriode() {
        return periode;
    }

    public Aktivitet getAktivitet() {
        return aktivitet;
    }

    public DelvisFravær getDelvisFravær() {
        return delvisFravær;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FraværPeriode that = (FraværPeriode) o;
        return Objects.equals(periode, that.periode) && Objects.equals(aktivitet, that.aktivitet) && Objects.equals(delvisFravær, that.delvisFravær);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periode, aktivitet, delvisFravær);
    }

    @Override
    public String toString() {
        return "FraværPeriode{" +
                "periode=" + periode +
                ", aktivitet=" + aktivitet +
                (delvisFravær != null ? ", delvisFravær=" + delvisFravær : "") +
                '}';
    }
}
