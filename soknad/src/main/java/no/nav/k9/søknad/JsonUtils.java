package no.nav.k9.søknad;

import java.util.List;
import java.util.ServiceLoader;

public class JsonUtils {

    private static JsonUtilsService service;

    static {
        //dersom eksakt 1 tilgjengelig implementasjon, husk denne for raskere tilgang
        ServiceLoader<JsonUtilsService> services = ServiceLoader.load(JsonUtilsService.class);
        if (services.stream().count() == 1) {
            service = services.findFirst().orElseThrow();
        }
    }

    private static JsonUtilsService findService() {
        if (service != null) {
            return service;
        }
        ServiceLoader<JsonUtilsService> services = ServiceLoader.load(JsonUtilsService.class);
        String klasseValgtForTest = System.getProperty("test.json.util.service");
        List<ServiceLoader.Provider<JsonUtilsService>> filtrerte = services.stream()
                .filter(it -> klasseValgtForTest == null || klasseValgtForTest.equals(it.type().getName()))
                .toList();

        if (filtrerte.isEmpty()) {
            throw new IllegalStateException("Ingen implementasjoner av JsonUtilService funnet. Du må importere soknad-jackson2 eller soknad-jackson3 fra k9-format");
        } else if (filtrerte.size() > 1) {
            throw new IllegalStateException("Flere implementasjoner av JsonUtilService funnet. Du må importere bare en av soknad-jackson2 eller soknad-jackson3 fra k9-format");
        } else {
            return filtrerte.getFirst().get();
        }
    }

    public static String toString(Object object) {
        return findService().toString(object);
    }

    public static <T> T fromString(String s, Class<T> clazz) {
        return findService().fromString(s, clazz);
    }

}
