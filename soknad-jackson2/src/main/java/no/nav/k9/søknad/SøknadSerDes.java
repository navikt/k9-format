package no.nav.k9.søknad;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public final class SøknadSerDes {
    private SøknadSerDes() {
    }

    public static String serialize(Søknad søknad) {
        return JsonUtils.toString(søknad);
    }

    public static Søknad deserialize(String søknad) {
        return JsonUtils.fromString(søknad, Søknad.class);
    }

    public static Søknad deserialize(ObjectNode node) {
        try {
            return JsonUtils.getObjectMapper().treeToValue(node, Søknad.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke konvertere til Søknad.class", e);
        }
    }

}
