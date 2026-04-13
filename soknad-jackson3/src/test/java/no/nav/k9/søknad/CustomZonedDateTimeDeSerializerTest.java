package no.nav.k9.søknad;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CustomZonedDateTimeDeSerializerTest {

    @Test
    void skal_parse_tidspunkt() {
        ZoneId lokalTidssone = ZoneId.of("+02:00");
        LocalDateTime tidspunktUtenMillis = LocalDateTime.of(2026, 4, 9, 12, 7, 33);
        Assertions.assertThat(CustomZonedDateTimeDeSerializerJackson3.parseDateTime("2026-04-09T12:07:33+02:00")).isEqualTo(ZonedDateTime.ofLocal(tidspunktUtenMillis, lokalTidssone, null));
        Assertions.assertThat(CustomZonedDateTimeDeSerializerJackson3.parseDateTime("2026-04-09T12:07:33.000+02:00")).isEqualTo(ZonedDateTime.ofLocal(tidspunktUtenMillis, lokalTidssone, null));
    }
}
