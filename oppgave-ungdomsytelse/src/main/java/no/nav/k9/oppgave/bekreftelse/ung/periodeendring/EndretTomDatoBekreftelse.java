package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.time.LocalDate;

public class EndretTomDatoBekreftelse implements Bekreftelse {

    private LocalDate nyTomDato;

    public EndretTomDatoBekreftelse(LocalDate nyTomDato) {
        this.nyTomDato = nyTomDato;
    }

    public LocalDate getNyTomDato() {
        return nyTomDato;
    }

    @Override
    public Type getType() {
        return Type.UNG_ENDRET_FOM_DATO;
    }

    @Override
    public DataBruktTilUtledning getDataBruktTilUtledning() {
        return new DataBruktTilUtledning();
    }

    @Override
    public Bekreftelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning) {
        return this;
    }

}
