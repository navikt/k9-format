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

public class EndretStartdatoBekreftelse implements Bekreftelse {

    @JsonProperty("oppgaveReferanse")
    private UUID oppgaveReferanse;

    @JsonProperty("nyStartdato")
    private LocalDate nyStartdato;

    @JsonProperty("harBrukerGodtattEndringen")
    private boolean harBrukerGodtattEndringen;

    @JsonProperty("uttalelseFraBruker")
    @Pattern(regexp = Patterns.FRITEKST, message = "[${validatedValue}] matcher ikke tillatt pattern [{regexp}]")
    @Size(max = 4000)
    private String uttalelseFraBruker;

    @JsonProperty("dataBruktTilUtledning")
    private DataBruktTilUtledning dataBruktTilUtledning;

    @JsonCreator
    public EndretStartdatoBekreftelse(
            @JsonProperty("oppgaveReferanse") UUID oppgaveReferanse,
            @JsonProperty("nyStartdato") LocalDate nyStartdato,
            @JsonProperty("harBrukerGodtattEndringen") boolean harBrukerGodtattEndringen) {
        this.oppgaveReferanse = oppgaveReferanse;
        this.nyStartdato = nyStartdato;
        this.harBrukerGodtattEndringen = harBrukerGodtattEndringen;
    }

    public LocalDate getNyStartdato() {
        return nyStartdato;
    }

    @Override
    public UUID getOppgaveReferanse() {
        return oppgaveReferanse;
    }

    @Override
    public Type getType() {
        return Type.UNG_ENDRET_STARTDATO;
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
    public boolean harBrukerGodtattEndringen() {
        return harBrukerGodtattEndringen;
    }
}
