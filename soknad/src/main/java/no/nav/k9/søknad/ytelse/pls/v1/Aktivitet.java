package no.nav.k9.søknad.ytelse.pls.v1;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.k9.søknad.felles.fravær.AktivitetFravær;
import no.nav.k9.søknad.felles.type.Organisasjonsnummer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class Aktivitet {

    @Valid
    @NotNull
    @JsonProperty(value = "aktivitetType", required = true)
    private AktivitetFravær aktivitetType;

    @JsonProperty(value = "arbeidsgiverOrgNr")
    @Valid
    private Organisasjonsnummer arbeidsgiverOrgNr;

    public Aktivitet(
            @JsonProperty(value = "aktivitetType", required = true) @Valid AktivitetFravær aktivitetType,
            @JsonProperty(value = "arbeidsgiverOrgNr") @Valid Organisasjonsnummer arbeidsgiverOrgNr) {
        this.aktivitetType = aktivitetType;
        this.arbeidsgiverOrgNr = arbeidsgiverOrgNr;
    }

    @AssertFalse(message="OrgNr er påkrevet for arbeidstaker")
    private boolean isManglerOrgNr() {
        return aktivitetType == AktivitetFravær.ARBEIDSTAKER && arbeidsgiverOrgNr == null;
    }

    public AktivitetFravær getAktivitetType() {
        return aktivitetType;
    }

    public Organisasjonsnummer getArbeidsgiverOrgNr() {
        return arbeidsgiverOrgNr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aktivitet aktivitet = (Aktivitet) o;
        return aktivitetType == aktivitet.aktivitetType && Objects.equals(arbeidsgiverOrgNr, aktivitet.arbeidsgiverOrgNr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aktivitetType, arbeidsgiverOrgNr);
    }

    @Override
    public String toString() {
        return "Aktivitet{" +
                "type=" + aktivitetType +
                (arbeidsgiverOrgNr != null ? ", orgNr=MASKERT" : "") +
                '}';
    }
}
