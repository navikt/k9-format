package no.nav.k9.oppgave;

import no.nav.k9.søknad.felles.type.Periode;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static no.nav.k9.oppgave.util.OppgaveUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SerializeAndDeserializeTest {

    @Test
    public void oppgave_bekreft_endret_programperiode_serdes_test() {
        var oppgaveBekreftelse = lagOppgaveBekreftelse(bekreftelseEndretProgramperiode(UUID.randomUUID(), new Periode(LocalDate.now(), LocalDate.now().plusDays(1)), true));

        var serializedOppgave = OppgaveBekreftelse.SerDes.serialize(oppgaveBekreftelse);
        var deserilizedOppgave = OppgaveBekreftelse.SerDes.deserialize(serializedOppgave);

        assertEquals(OppgaveBekreftelse.SerDes.serialize(deserilizedOppgave), serializedOppgave);
    }

    @Test
    public void oppgave_bekreft_avvik_registerinntekt_serdes_test() {
        var oppgaveBekreftelse = lagOppgaveBekreftelse(bekreftelseAvvikRegisterinntekt(true, null));

        var serializedOppgave = OppgaveBekreftelse.SerDes.serialize(oppgaveBekreftelse);
        var deserilizedOppgave = OppgaveBekreftelse.SerDes.deserialize(serializedOppgave);

        assertEquals(OppgaveBekreftelse.SerDes.serialize(deserilizedOppgave), serializedOppgave);
    }

    @Test
    public void oppgave_bekreft_avvik_registerinntekt_med_uttalelse_serdes_test() {
        var oppgaveBekreftelse = lagOppgaveBekreftelse(bekreftelseAvvikRegisterinntekt(false, "en uttalelse"));

        var serializedOppgave = OppgaveBekreftelse.SerDes.serialize(oppgaveBekreftelse);
        var deserilizedOppgave = OppgaveBekreftelse.SerDes.deserialize(serializedOppgave);

        assertEquals(OppgaveBekreftelse.SerDes.serialize(deserilizedOppgave), serializedOppgave);
    }

}
