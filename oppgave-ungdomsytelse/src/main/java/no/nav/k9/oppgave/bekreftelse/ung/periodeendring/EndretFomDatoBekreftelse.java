package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.time.LocalDate;
import java.util.UUID;

public class EndretFomDatoBekreftelse implements DatoEndring {

    @JsonProperty("oppgaveReferanse")
    private UUID oppgaveReferanse;

    @JsonProperty("nyFomDato")
    private LocalDate nyFomDato;

    @JsonProperty("harBrukerGodtattEndringen")
    private boolean harBrukerGodtattEndringen;

    @JsonProperty("uttalelseFraBruker")
    private String uttalelseFraBruker;

    @JsonProperty("dataBruktTilUtledning")
    private DataBruktTilUtledning dataBruktTilUtledning;

    @JsonCreator
    public EndretFomDatoBekreftelse(
            @JsonProperty("oppgaveReferanse") UUID oppgaveReferanse,
            @JsonProperty("nyFomDato") LocalDate nyFomDato,
            @JsonProperty("harBrukerGodtattEndringen") boolean harBrukerGodtattEndringen) {
        this.oppgaveReferanse = oppgaveReferanse;
        this.nyFomDato = nyFomDato;
        this.harBrukerGodtattEndringen = harBrukerGodtattEndringen;
    }

    public LocalDate getNyFomDato() {
        return nyFomDato;
    }

    @Override
    public Type getType() {
        return Type.UNG_ENDRET_FOM_DATO;
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
    public LocalDate getNyDato() {
        return nyFomDato;
    }

    @Override
    public String getUttalelseFraBruker() {
        return uttalelseFraBruker;
    }

    public Bekreftelse medOppgaveReferanse(UUID oppgaveReferanse) {
        this.oppgaveReferanse = oppgaveReferanse;
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
    public boolean harBrukerGodtattEndringen() {
        return harBrukerGodtattEndringen;
    }

    @Override
    public UUID getOppgaveReferanse() {
        return oppgaveReferanse;
    }
}
