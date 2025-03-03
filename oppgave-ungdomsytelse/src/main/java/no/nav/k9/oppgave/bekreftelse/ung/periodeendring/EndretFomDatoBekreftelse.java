package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.time.LocalDate;

public class EndretFomDatoBekreftelse implements DatoEndring {

    private LocalDate nyFomDato;
    private boolean harBrukerGodtattEndringen;
    private DataBruktTilUtledning dataBruktTilUtledning;


    public EndretFomDatoBekreftelse(LocalDate nyFomDato, boolean harBrukerGodtattEndringen) {
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
    public boolean harBrukerGodtattEndringen() {
        return harBrukerGodtattEndringen;
    }
}
