package no.nav.k9.ettersendelse;

import no.nav.k9.søknad.JsonUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

public final class EttersendelseSerDes {
    private EttersendelseSerDes() {
    }

    public static String serialize(Ettersendelse ettersendelse) {
        return JsonUtils.toString(ettersendelse);
    }

    public static Ettersendelse deserialize(String ettersendelse) {
        return JsonUtils.fromString(ettersendelse, Ettersendelse.class);
    }

    public static Ettersendelse deserialize(JsonNode node) {
        try {
            return JsonUtils.getJsonMapper().treeToValue(node, Ettersendelse.class);
        } catch (JacksonException e) {
            throw new IllegalArgumentException("Kunne ikke konvertere til Ettersendelse.class", e);
        }
    }
}
