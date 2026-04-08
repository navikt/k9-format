package no.nav.k9.oppgave;

import com.fasterxml.jackson.databind.node.ObjectNode;
import no.nav.k9.søknad.JsonUtils;

import java.io.IOException;

public final class OppgaveBekreftelseSerDes {
    private OppgaveBekreftelseSerDes() {
    }

    public static String serialize(OppgaveBekreftelse oppgave) {
        return JsonUtils.toString(oppgave);
    }

    public static OppgaveBekreftelse deserialize(String oppgave) {
        return JsonUtils.fromString(oppgave, OppgaveBekreftelse.class);
    }

    public static OppgaveBekreftelse deserialize(ObjectNode node) {
        try {
            return JsonUtils.getObjectMapper().treeToValue(node, OppgaveBekreftelse.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke konvertere til Oppgave.class", e);
        }
    }

}