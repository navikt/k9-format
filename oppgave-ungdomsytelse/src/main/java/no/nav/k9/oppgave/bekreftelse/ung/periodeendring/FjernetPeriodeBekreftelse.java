package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.konstant.Patterns;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.time.LocalDate;
import java.util.UUID;

public class FjernetPeriodeBekreftelse implements Bekreftelse {

    @JsonProperty("oppgaveReferanse")
    private UUID oppgaveReferanse;

    @JsonProperty("fjernetPeriode")
    private Periode fjernetPeriode;

    @JsonProperty("harUttalelse")
    private boolean harUttalelse;

    @JsonProperty("uttalelseFraBruker")
    @Pattern(regexp = Patterns.FRITEKST, message = "[ugyldigSyntaks] matcher ikke tillatt pattern [{regexp}]")
    @Size(max = 4000)
    private String uttalelseFraBruker;

    @JsonProperty("dataBruktTilUtledning")
    private DataBruktTilUtledning dataBruktTilUtledning;

    @JsonCreator
    public FjernetPeriodeBekreftelse(
            @JsonProperty("oppgaveReferanse") UUID oppgaveReferanse,
            @JsonProperty("fjernetPeriode") Periode fjernetPeriode,
            @JsonProperty("harUttalelse") boolean harUttalelse) {
        this.oppgaveReferanse = oppgaveReferanse;
        this.fjernetPeriode = fjernetPeriode;
        this.harUttalelse = harUttalelse;
    }

    public Periode getFjernetPeriode() {
        return fjernetPeriode;
    }

    @Override
    public UUID getOppgaveReferanse() {
        return oppgaveReferanse;
    }

    @Override
    public Type getType() {
        return Type.UNG_FJERNET_PERIODE;
    }

    @Override
    public DataBruktTilUtledning getDataBruktTilUtledning() {
        return dataBruktTilUtledning;
    }

    @Override
    public Bekreftelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning) {
        this.dataBruktTilUtledning = dataBruktTilUtledning;
        return this;
    }

    @Override
    public String getUttalelseFraBruker() {
        return uttalelseFraBruker;
    }

    public Bekreftelse medUttalelseFraBruker(String uttalelseFraBruker) {
        this.uttalelseFraBruker = uttalelseFraBruker;
        return this;
    }

    @Override
    public boolean harUttalelse() {
        return harUttalelse;
    }
}
