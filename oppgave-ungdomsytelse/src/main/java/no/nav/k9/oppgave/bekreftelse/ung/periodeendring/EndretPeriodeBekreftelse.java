package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.konstant.Patterns;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.util.UUID;

public class EndretPeriodeBekreftelse implements Bekreftelse{

    @NotNull
    @JsonProperty("oppgaveReferanse")
    private UUID oppgaveReferanse;

    @NotNull
    @JsonProperty("nyPeriode")
    private Periode nyPeriode;

    @NotNull
    @JsonProperty("harUttalelse")
    private boolean harUttalelse;

    @JsonProperty("uttalelseFraBruker")
    @Pattern(regexp = Patterns.FRITEKST, message = "[ugyldigSyntaks] matcher ikke tillatt pattern [{regexp}]")
    @Size(max = 4000)
    private String uttalelseFraBruker;

    @JsonProperty("dataBruktTilUtledning")
    private DataBruktTilUtledning dataBruktTilUtledning;

    @JsonCreator
    public EndretPeriodeBekreftelse(
            @JsonProperty("oppgaveReferanse") UUID oppgaveReferanse,
            @JsonProperty("nyPeriode") Periode nyPeriode,
            @JsonProperty("harUttalelse") boolean harUttalelse) {
        this.oppgaveReferanse = oppgaveReferanse;
        this.nyPeriode = nyPeriode;
        this.harUttalelse = harUttalelse;
    }

    public Periode getNyPeriode() {
        return nyPeriode;
    }

    @Override
    public UUID getOppgaveReferanse() {
        return oppgaveReferanse;
    }

    @Override
    public Bekreftelse.Type getType() {
        return Bekreftelse.Type.UNG_ENDRET_PERIODE;
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