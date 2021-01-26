package no.nav.k9.søknad;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import no.nav.k9.søknad.felles.Versjon;


public class VersjonTest {
    @Test
    public void gylidgSemanticVersjon() {
        assertThat(Versjon.of("1.20.330").erGyldig()).isTrue();
    }

    @Test
    public void gylidigSemanticVersjonMedPrereleaseOgMetadata() {
        assertThat(Versjon.of("1.20.330-alpha+sha").erGyldig()).isFalse();
    }

    @Test
    public void manglerPatch() {
        assertThat(Versjon.of("1.2").erGyldig()).isFalse();
    }

    @Test
    public void ugyldigFormat() {
        assertThat(Versjon.of("1.2.f").erGyldig()).isFalse();
    }
}
