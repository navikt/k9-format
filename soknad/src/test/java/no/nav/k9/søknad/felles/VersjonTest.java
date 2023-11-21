package no.nav.k9.søknad.felles;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class VersjonTest {

    @Test
    void sammenligneVersjoner() {
        assertThat(Versjon.of("2.0.0")).isGreaterThan(Versjon.of("1.9.9"));
        assertThat(Versjon.of("1.2.0")).isGreaterThan(Versjon.of("1.1.9"));
        assertThat(Versjon.of("1.1.2")).isGreaterThan(Versjon.of("1.1.1"));
        assertThat(Versjon.of("1.1.1")).isEqualTo(Versjon.of("1.1.1"));
    }
}
