package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.time.LocalDate;
import java.util.UUID;

public class EndretTomDatoBekreftelse implements DatoEndring {

    @JsonProperty("oppgaveId")
    private UUID oppgaveId;

    @JsonProperty("nyTomDato")
    private LocalDate nyTomDato;

    @JsonProperty("harBrukerGodtattEndringen")
    private boolean harBrukerGodtattEndringen;

    @JsonProperty("uttalelseFraBruker")
    private String uttalelseFraBruker;

    @JsonProperty("dataBruktTilUtledning")
    private DataBruktTilUtledning dataBruktTilUtledning;

    @JsonCreator
    public EndretTomDatoBekreftelse(
            @JsonProperty("oppgaveId") UUID oppgaveId,
            @JsonProperty("nyTomDato") LocalDate nyFomDato,
            @JsonProperty("harBrukerGodtattEndringen") boolean harBrukerGodtattEndringen) {
        this.oppgaveId = oppgaveId;
        this.nyTomDato = nyFomDato;
        this.harBrukerGodtattEndringen = harBrukerGodtattEndringen;
    }

    @Override
    public LocalDate getNyDato() {
        return nyTomDato;
    }

    @Override
    public String getUttalelseFraBruker() {
        return uttalelseFraBruker;
    }

    @Override
    public boolean harBrukerGodtattEndringen() {
        return harBrukerGodtattEndringen;
    }

    @Override
    public Type getType() {
        return Type.UNG_ENDRET_TOM_DATO;
    }

    @Override
    public DataBruktTilUtledning getDataBruktTilUtledning() {
        return dataBruktTilUtledning;
    }

    public Bekreftelse medOppgaveId(UUID oppgaveId) {
        this.oppgaveId = oppgaveId;
        return this;
    }

    public Bekreftelse medUttalelseFraBruker(String uttalelseFraBruker) {
        this.uttalelseFraBruker = uttalelseFraBruker;
        return this;
    }

    public Bekreftelse medHarBrukerGodtattEndringen(boolean harBrukerGodtattEndringen) {
        this.harBrukerGodtattEndringen = harBrukerGodtattEndringen;
        return this;
    }

    @Override
    public Bekreftelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning) {
        this.dataBruktTilUtledning = dataBruktTilUtledning;
        return this;
    }

    @Override
    public UUID getOppgaveId() {
        return oppgaveId;
    }
}
