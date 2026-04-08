package no.nav.k9.oppgave;

import no.nav.k9.søknad.JsonUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

public final class OppgaveBekreftelseSerDes {
    private OppgaveBekreftelseSerDes() {
    }

    public static String serialize(OppgaveBekreftelse oppgave) {
        return JsonUtils.toString(oppgave);
    }

    public static OppgaveBekreftelse deserialize(String oppgave) {
        return JsonUtils.fromString(oppgave, OppgaveBekreftelse.class);
    }

    public static OppgaveBekreftelse deserialize(JsonNode node) {
        try {
            return JsonUtils.getJsonMapper().treeToValue(node, OppgaveBekreftelse.class);
        } catch (JacksonException e) {
            throw new IllegalArgumentException("Kunne ikke konvertere til Oppgave.class", e);
        }
    }

}