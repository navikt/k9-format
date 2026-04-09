package no.nav.k9.søknad;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ValueDeserializer;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class CustomZonedDateTimeDeSerializerJackson3 extends ValueDeserializer<ZonedDateTime> {

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
    public ZonedDateTime deserialize(tools.jackson.core.JsonParser p, tools.jackson.databind.DeserializationContext ctxt) throws JacksonException {
        return ZonedDateTime.parse(p.getValueAsString(), zonedDateTimeFormatter);
    }
}
