package no.nav.k9.soknad;

import no.nav.k9.soknad.felles.Versjon;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class VersjonTest {
    @Test
    public void gylidgSemanticVersjon() {
        assertTrue(Versjon.of("1.20.330").erGyldig());
    }

    @Test
    public void gylidigSemanticVersjonMedPrereleaseOgMetadata() {
        assertFalse(Versjon.of("1.20.330-alpha+sha").erGyldig());
    }

    @Test
    public void manglerPatch() {
        assertFalse(Versjon.of("1.2").erGyldig());
    }

    @Test
    public void ugyldigFormat() {
        assertFalse(Versjon.of("1.2.f").erGyldig());
    }
}
