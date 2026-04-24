package no.nav.k9.søknad;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.ZonedDateTime;

public final class JsonUtilsJackson3 implements JsonUtilsService {

    private static final JsonMapper defaultJsonMapper = createDefaultJsonMapperBuilder().build();

    private static final JsonMapper prettyPrintJsonMapper = createDefaultJsonMapperBuilder()
            .defaultPrettyPrinter(new PlatformIndependentPrettyPrinter())
            .enable(SerializationFeature.INDENT_OUTPUT)
            .build();

    @Override
    public  String toString(Object object) {
        return toString(object, prettyPrintJsonMapper);
    }

    @Override
    public  <T> T fromString(String s, Class<T> clazz) {
        try {
            return defaultJsonMapper.readValue(s, clazz);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toString(Object object, JsonMapper jsonMapper) {
        try {
            return jsonMapper.writeValueAsString(object);
        } catch (JacksonException e) {
            throw new RuntimeException("Feil ved serialisering av objekt.", e);
        }
    }

    private static JsonMapper.Builder createDefaultJsonMapperBuilder(){
        JsonMapper.Builder builder = DefaultJson3Mapper.getCopyFromDefaultJsonMapper();
        builder.addModule(new SimpleModule().addDeserializer(ZonedDateTime.class, new CustomZonedDateTimeDeSerializerJackson3()));
        return builder;
    }

    public static JsonMapper getJsonMapper() {
        return defaultJsonMapper;
    }

    static JsonNode toObjectNode(Object object) {
        return defaultJsonMapper.valueToTree(object);
    }

    public static final class PlatformIndependentPrettyPrinter extends tools.jackson.core.util.DefaultPrettyPrinter {
        PlatformIndependentPrettyPrinter() {
            this._objectIndenter = new tools.jackson.core.util.DefaultIndenter("  ", "\n");
        }

        @Override
        public tools.jackson.core.util.DefaultPrettyPrinter createInstance() {
            return new PlatformIndependentPrettyPrinter();
        }
    }

}
