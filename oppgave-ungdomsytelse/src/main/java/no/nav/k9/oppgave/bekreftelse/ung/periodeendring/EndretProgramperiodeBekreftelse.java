package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.time.LocalDate;
import java.util.UUID;

public class EndretProgramperiodeBekreftelse implements Bekreftelse {

    @JsonProperty("oppgaveId")
    private UUID oppgaveId;

    @JsonProperty("nyPeriode")
    private Periode nyPeriode;

    @JsonProperty("harBrukerGodtattEndringen")
    private boolean harBrukerGodtattEndringen;

    @JsonProperty("uttalelseFraBruker")
    private String uttalelseFraBruker;

    @JsonProperty("dataBruktTilUtledning")
    private DataBruktTilUtledning dataBruktTilUtledning;

    @JsonCreator
    public EndretProgramperiodeBekreftelse(
            @JsonProperty("oppgaveId") UUID oppgaveId,
            @JsonProperty("nyPeriode") Periode nyPeriode,
            @JsonProperty("harBrukerGodtattEndringen") boolean harBrukerGodtattEndringen) {
        this.oppgaveId = oppgaveId;
        this.nyPeriode = nyPeriode;
        this.harBrukerGodtattEndringen = harBrukerGodtattEndringen;
    }

    public Periode getNyPeriode() {
        return nyPeriode;
    }

    @Override
    public UUID getOppgaveId() {
        return oppgaveId;
    }

    @Override
    public Type getType() {
        return Type.UNG_ENDRET_PROGRAMPERIODE;
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

    public Bekreftelse medHarBrukerGodtattEndringen(boolean harBrukerGodtattEndringen) {
        this.harBrukerGodtattEndringen = harBrukerGodtattEndringen;
        return this;
    }

    @Override
    public boolean harBrukerGodtattEndringen() {
        return harBrukerGodtattEndringen;
    }
}
