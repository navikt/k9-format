package no.nav.k9.søknad;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

public final class SøknadSerDes {
    private SøknadSerDes() {
    }

    public static String serialize(Søknad søknad) {
        return JsonUtils.toString(søknad);
    }

    public static Søknad deserialize(String søknad) {
        return JsonUtils.fromString(søknad, Søknad.class);
    }

    public static Søknad deserialize(JsonNode node) {
        try {
            return JsonUtils.getJsonMapper().treeToValue(node, Søknad.class);
        } catch (JacksonException e) {
            throw new IllegalArgumentException("Kunne ikke konvertere til Søknad.class", e);
        }
    }

}
