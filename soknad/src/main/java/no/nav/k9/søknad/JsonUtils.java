package no.nav.k9.søknad;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import no.nav.k9.søknad.felles.DtoKonstanter;
import no.nav.k9.søknad.ytelse.omsorgspenger.v1.OmsorgspengerUtbetaling;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public final class JsonUtils {

    private static final ObjectMapper objectMapper = createObjectMapper();

    private JsonUtils() {
    }

    public static String toString(Object object) {
        return toString(object, objectMapper);
    }

    /**
     * Tillater å override objectmapper. Nødvendig i en overgangsfase mens Kodeverdi objekter i k9sak
     * overføres til @JsonValue
     */
    public static String toString(Object object, ObjectMapper objectMapper) {
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
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(ZonedDateTime.class, new CustomZonedDateTimeDeSerializer());


        final ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(javaTimeModule)
                .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
                .setTimeZone(TimeZone.getTimeZone("UTC"))
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
                .enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

        objectMapper.registerSubtypes(OmsorgspengerUtbetaling.class, PleiepengerSyktBarn.class);
        objectMapper.setDateFormat(new SimpleDateFormat(DtoKonstanter.DATO_TID_FORMAT));

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
