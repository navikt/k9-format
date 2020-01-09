package no.nav.k9.soknad;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.TimeZone;

public final class JsonUtils {

    private static final ObjectMapper objectMapper = createObjectMapper();

    private JsonUtils() { }

    public static String toString(Object object) {
        try {
            return objectMapper.writer(new PlatformIndependentPrettyPrinter()).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Feil ved serialisering av objekt.", e);
        }
    }

    public static <T> T fromString(String s, Class<T> clazz) {
        try {
            return objectMapper.readValue(s, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    static ObjectNode toObjectNode(Object object) {
        return (ObjectNode) objectMapper.valueToTree(object);
    }

    private static final ObjectMapper createObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
                .setTimeZone(TimeZone.getTimeZone("Europe/Oslo"))
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                ;

        return objectMapper;
    }

    private static final class PlatformIndependentPrettyPrinter extends DefaultPrettyPrinter {
        PlatformIndependentPrettyPrinter() {
            this._objectIndenter = new DefaultIndenter("  ", "\n");
        }

        @Override
        public DefaultPrettyPrinter createInstance() {
            return new DefaultPrettyPrinter(this);
        }
    }
}
