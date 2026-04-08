package no.nav.k9.ettersendelse;

import com.fasterxml.jackson.databind.node.ObjectNode;
import no.nav.k9.søknad.JsonUtils;

import java.io.IOException;

public final class EttersendelseSerDes {
    private EttersendelseSerDes() {
    }

    public static String serialize(Ettersendelse ettersendelse) {
        return JsonUtils.toString(ettersendelse);
    }

    public static Ettersendelse deserialize(String ettersendelse) {
        return JsonUtils.fromString(ettersendelse, Ettersendelse.class);
    }

    public static Ettersendelse deserialize(ObjectNode node) {
        try {
            return JsonUtils.getObjectMapper().treeToValue(node, Ettersendelse.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke konvertere til Ettersendelse.class", e);
        }
    }
}
