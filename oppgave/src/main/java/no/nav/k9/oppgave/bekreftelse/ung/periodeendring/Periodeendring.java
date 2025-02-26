package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

public class Periodeendring implements Bekreftelse {

    @Override
    public Type getType() {
        return Type.UNG_PERIODEENDRING;
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
