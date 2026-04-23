package no.nav.k9.søknad;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

public class JsonUtilsJackson2Test {

    @Test
    public void reserialiseringSkalAlltidGiSammeJson() {
        final String testdataverdi = "foobar";

        final Testdata testdata = new Testdata();
        testdata.setFelt(testdataverdi);

        final String json = JsonUtils.toString(testdata);
        assertThat(json).contains(testdataverdi);

        final Testdata testdataDeserialisert = JsonUtils.fromString(json, Testdata.class);
        assertThat(testdataDeserialisert.getFelt()).isEqualTo(testdataverdi);

        final String reserialisertJson = JsonUtils.toString(testdata);
        assertThat(reserialisertJson).isEqualTo(json);
    }

    @Test
    public void ukjentePropertiesSkalGiException() {
        final Testdata testdata = new Testdata();
        final ObjectNode objectNode = JsonUtilsJackson2.toObjectNode(testdata);

        final String feltnavn = "nyttFeltnavn";
        final String feltverdi = "nyFeltverdi";
        objectNode.put(feltnavn, feltverdi);

        final String json = JsonUtils.toString(objectNode);
        assertThat(json).contains(feltnavn);
        assertThat(json).contains(feltverdi);

        try {
            JsonUtils.fromString(json, Testdata.class);
            Assertions.fail("Forventer UnrecognizedPropertyException. Bakgrunnen for dette er at k9-sak skal få feil ved behandling av ukjente properties, som igjen medfører at behandlingen forsøkes på nytt ved et senere tidspunkt (når k9-sak har blitt utvidet med støtte for det nye feltet).");
        } catch (RuntimeException e) {
            // Ignore.
        }
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "\"2024-08-14T10:05:56.111111Z\"",
            "\"2024-08-14T10:05:56.111Z\"",
            "\"2024-08-14T10:05:56Z\""
    })
    public void skal_kunne_deserialisere_zoneddatetime(String dato) {
        Assertions.assertThatNoException().isThrownBy(() -> deserialiserZonedDatetime(dato));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-08-14T10:05:56.111111111Z",
            "2024-08-14T10:05:56.11111111Z",
            "2024-08-14T10:05:56.1111111Z",
            "2024-08-14T10:05:56.111111Z",
            "2024-08-14T10:05:56.11111Z",
            "2024-08-14T10:05:56.1111Z",
            "2024-08-14T10:05:56.111Z",
            "2024-08-14T10:05:56.11Z",
            "2024-08-14T10:05:56.1Z",
            "2024-08-14T10:05:56Z",
            "2024-08-14T10:05:56.111+02:00",
            "2024-08-14T10:05:56.111+02:00[Europe/Oslo]",
    })
    public void deserialisering_av_zdt_skal_ikke_feile(String dato) {
        assertThatNoException().isThrownBy(() -> ZonedDateTime.parse(dato, CustomZonedDateTimeDeSerializer.zonedDateTimeFormatter));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-08-14T10:05:56.1111111111Z",
            "2024-08-14T10:05Z"
    })
    public void deserialisering_av_zdt_skal_feile(String dato) {
        assertThatThrownBy(() -> ZonedDateTime.parse(dato, CustomZonedDateTimeDeSerializer.zonedDateTimeFormatter))
                .isInstanceOf(DateTimeParseException.class);
    }

    private static ZonedDateTime deserialiserZonedDatetime(String s) {
        return JsonUtils.fromString(s, ZonedDateTime.class);
    }


    private static class Testdata {

        private String felt;

        public void setFelt(String felt) {
            this.felt = felt;
        }

        public String getFelt() {
            return felt;
        }
    }

    public static class TimeHolder {
        private LocalDateTime tid;
        private ZonedDateTime zonedDateTime;
        private LocalDate localDate;
        private Duration duration;

        public LocalDateTime getTid() {
            return tid;
        }

        public void setTid(LocalDateTime tid) {
            this.tid = tid;
        }

        public ZonedDateTime getZonedDateTime() {
            return zonedDateTime;
        }

        public void setZonedDateTime(ZonedDateTime zonedDateTime) {
            this.zonedDateTime = zonedDateTime;
        }

        public LocalDate getLocalDate() {
            return localDate;
        }

        public void setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }
    }


    @Test
    void serialize_localdatetime() {
        TimeHolder holder = new TimeHolder();
        holder.setTid(LocalDateTime.of(2025, 6, 15, 14, 30, 0));
        String json = JsonUtils.toString(holder);
        assertThat(json).contains("\"2025-06-15T14:30:00\"");
    }

    @Test
    void deserialize_localdatetime() {
        String json = """
                { "tid": "2025-06-15T14:30:00" }
                """;
        TimeHolder result = JsonUtils.fromString(json, TimeHolder.class);
        assertThat(result.getTid()).isEqualTo(LocalDateTime.of(2025, 6, 15, 14, 30, 0));
    }

    @Test
    void serialize_localdatetime_with_nanos() {
        TimeHolder holder = new TimeHolder();
        holder.setTid(LocalDateTime.of(2025, 6, 15, 14, 30, 0, 123456000));
        String json = JsonUtils.toString(holder);
        assertThat(json).contains("\"2025-06-15T14:30:00.123456\"");
    }

    @Test
    void deserialize_localdatetime_with_nanos() {
        String json = """
                { "tid": "2025-06-15T14:30:00.123456" }
                """;
        TimeHolder result = JsonUtils.fromString(json, TimeHolder.class);
        assertThat(result.getTid()).isEqualTo(LocalDateTime.of(2025, 6, 15, 14, 30, 0, 123456000));
    }

    @Test
    void serialize_zonedDateTime_utc() {
        TimeHolder holder = new TimeHolder();
        holder.setZonedDateTime(ZonedDateTime.of(2025, 6, 15, 10, 30, 0, 0, ZoneId.of("UTC")));
        String json = JsonUtils.toString(holder);
        assertThat(json).contains("\"2025-06-15T10:30:00Z\"");
    }

    @Test
    void deserialize_zonedDateTime_utc() {
        String json = """
                { "zonedDateTime": "2025-06-15T10:30:00Z" }
                """;
        TimeHolder result = JsonUtils.fromString(json, TimeHolder.class);
        assertThat(result.getZonedDateTime()).isEqualTo(ZonedDateTime.of(2025, 6, 15, 10, 30, 0, 0, ZoneId.of("UTC")));
    }

    @Test
    void serialize_zonedDateTime_with_offset() {
        TimeHolder holder = new TimeHolder();
        holder.setZonedDateTime(ZonedDateTime.of(2025, 6, 15, 12, 30, 0, 0, ZoneId.of("Europe/Oslo")));
        String json = JsonUtils.toString(holder);
        assertThat(json).contains("\"2025-06-15T10:30:00Z\"");
    }

    @Test
    void deserialize_zonedDateTime_with_offset() {
        String json = """
                { "zonedDateTime": "2025-06-15T12:30:00+02:00" }
                """;
        TimeHolder result = JsonUtils.fromString(json, TimeHolder.class);
        assertThat(result.getZonedDateTime().toInstant())
                .isEqualTo(ZonedDateTime.of(2025, 6, 15, 10, 30, 0, 0, ZoneId.of("UTC")).toInstant());
    }

    @Test
    void serialize_zonedDateTime_with_millis() {
        TimeHolder holder = new TimeHolder();
        holder.setZonedDateTime(ZonedDateTime.of(2025, 6, 15, 10, 30, 0, 123000000, ZoneId.of("UTC")));
        String json = JsonUtils.toString(holder);
        assertThat(json).contains("\"2025-06-15T10:30:00.123Z\"");
    }

    @Test
    void deserialize_zonedDateTime_with_millis() {
        String json = """
                { "zonedDateTime": "2025-06-15T10:30:00.123Z" }
                """;
        TimeHolder result = JsonUtils.fromString(json, TimeHolder.class);
        assertThat(result.getZonedDateTime().getNano()).isEqualTo(123000000);
    }

    @Test
    void serialize_localDate() {
        TimeHolder holder = new TimeHolder();
        holder.setLocalDate(LocalDate.of(2025, 6, 15));
        String json = JsonUtils.toString(holder);
        assertThat(json).contains("\"2025-06-15\"");
    }

    @Test
    void deserialize_localDate() {
        String json = """
                { "localDate": "2025-06-15" }
                """;
        TimeHolder result = JsonUtils.fromString(json, TimeHolder.class);
        assertThat(result.getLocalDate()).isEqualTo(LocalDate.of(2025, 6, 15));
    }

    @Test
    void serialize_duration_iso() {
        TimeHolder holder = new TimeHolder();
        holder.setDuration(Duration.ofHours(7).plusMinutes(30));
        String json = JsonUtils.toString(holder);
        assertThat(json).contains("\"PT7H30M\"");
    }

    @Test
    void deserialize_duration_iso() {
        String json = """
                { "duration": "PT7H30M" }
                """;
        TimeHolder result = JsonUtils.fromString(json, TimeHolder.class);
        assertThat(result.getDuration()).isEqualTo(Duration.ofHours(7).plusMinutes(30));
    }

    @Test
    void serialize_duration_seconds() {
        TimeHolder holder = new TimeHolder();
        holder.setDuration(Duration.ofHours(1));
        String json = JsonUtils.toString(holder);
        assertThat(json).contains("\"PT1H\"");
    }

    @Test
    void deserialize_duration_seconds() {
        String json = """
                { "duration": "PT3600S" }
                """;
        TimeHolder result = JsonUtils.fromString(json, TimeHolder.class);
        assertThat(result.getDuration()).isEqualTo(Duration.ofHours(1));
    }

    @Test
    void serialize_and_deserialize_roundtrip() {
        TimeHolder original = new TimeHolder();
        original.setTid(LocalDateTime.of(2025, 3, 20, 8, 15, 30));
        original.setZonedDateTime(ZonedDateTime.of(2025, 3, 20, 14, 0, 0, 0, ZoneId.of("UTC")));
        original.setLocalDate(LocalDate.of(2025, 3, 20));
        original.setDuration(Duration.ofHours(2).plusMinutes(45));
        String json = JsonUtils.toString(original);
        TimeHolder deserialized = JsonUtils.fromString(json, TimeHolder.class);
        assertThat(deserialized.getTid()).isEqualTo(original.getTid());
        assertThat(deserialized.getZonedDateTime().toInstant()).isEqualTo(original.getZonedDateTime().toInstant());
        assertThat(deserialized.getLocalDate()).isEqualTo(original.getLocalDate());
        assertThat(deserialized.getDuration()).isEqualTo(original.getDuration());
    }

    public enum TestEnum {
        OPSJON_A,
        OPSJON_B
    }

    public static class EnumHolder {

        private TestEnum verdi;

        public TestEnum getVerdi() {
            return verdi;
        }

        public void setVerdi(TestEnum verdi) {
            this.verdi = verdi;
        }
    }

    @Test
    void serialisering_av_enum() {
        EnumHolder enumHolder = new EnumHolder();
        enumHolder.setVerdi(TestEnum.OPSJON_A);
        String string = JsonUtils.toString(enumHolder);
        assertThat(string).isEqualTo("""
                {
                  "verdi" : "OPSJON_A"
                }""");
    }

    @Test
    void deserialisering_av_enum() {
        EnumHolder deserialisert = JsonUtils.fromString("""
                {
                  "verdi" : "OPSJON_A"
                }""", EnumHolder.class);
        assertThat(deserialisert.getVerdi()).isEqualTo(TestEnum.OPSJON_A);
    }

    @Test
    void deserialisering_av_ukjent_enumverdi() {
        try {
            JsonUtils.fromString("""
                    {
                      "verdi" : "OPSJON_C"
            
                    }""", EnumHolder.class);
            throw new AssertionError("Forventer at deserialisering feiler ved ukjent enumverdi");
        } catch (RuntimeException e){
            assertThat(e.getCause()).isInstanceOf(InvalidFormatException.class);
        }
    }
}
