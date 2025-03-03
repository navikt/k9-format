package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.time.LocalDate;

public class EndretTomDatoBekreftelse implements DatoEndring {

    private LocalDate nyTomDato;
    private boolean harBrukerGodtattEndringen;
    private DataBruktTilUtledning dataBruktTilUtledning;

    public EndretTomDatoBekreftelse(LocalDate nyTomDato, boolean harBrukerGodtattEndringen) {
        this.nyTomDato = nyTomDato;
        this.harBrukerGodtattEndringen = harBrukerGodtattEndringen;
    }

    @Override
    public LocalDate getNyDato() {
        return nyTomDato;
    }

    @Override
    public boolean harBrukerGodtattEndringen() {
        return harBrukerGodtattEndringen;
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

}
