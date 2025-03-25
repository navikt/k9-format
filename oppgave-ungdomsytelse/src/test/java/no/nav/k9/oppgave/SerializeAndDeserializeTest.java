package no.nav.k9.oppgave;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static no.nav.k9.oppgave.util.OppgaveUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SerializeAndDeserializeTest {

    @Test
    public void oppgave_bekreft_endret_startdato_serdes_test() {
        var oppgaveBekreftelse = lagOppgaveBekreftelse(bekreftelseEndretStartdato(UUID.randomUUID(), LocalDate.now(), true));

        var serializedOppgave = OppgaveBekreftelse.SerDes.serialize(oppgaveBekreftelse);
        var deserilizedOppgave = OppgaveBekreftelse.SerDes.deserialize(serializedOppgave);

        assertEquals(OppgaveBekreftelse.SerDes.serialize(deserilizedOppgave), serializedOppgave);
    }

    @Test
    public void oppgave_bekreft_endret_sluttdato_serdes_test() {
        var oppgaveBekreftelse = lagOppgaveBekreftelse(bekreftelseEndretSluttdatodato(UUID.randomUUID(), LocalDate.now(), true));

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
