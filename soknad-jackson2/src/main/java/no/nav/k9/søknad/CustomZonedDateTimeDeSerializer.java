package no.nav.k9.søknad;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class CustomZonedDateTimeDeSerializer extends JsonDeserializer<ZonedDateTime> {

    static final DateTimeFormatter zonedDateTimeFormatter = new DateTimeFormatterBuilder()
            // 2024-08-14T10:05:56
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart()
            // 2024-08-14T10:05:56.111
            .appendFraction(ChronoField.MILLI_OF_SECOND, 0, 9, true)
            .optionalEnd()
            // 2024-08-14T10:05:56.111+02:00
            .appendPattern("ZZZZZ")
            .optionalStart()
            // 2024-08-14T10:05:56.111+02:00[Europe/Oslo]
            .appendLiteral('[')
            .appendZoneRegionId()
            .appendLiteral(']')
            .optionalEnd()
            .toFormatter();

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = p.getValueAsString();
        return ZonedDateTime.parse(value, zonedDateTimeFormatter);
    }
}
