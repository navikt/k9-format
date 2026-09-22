package no.nav.k9.konstant;

public final class Patterns {
    public static final String FRITEKST = "^[\\p{L}\\p{M}\\p{N}\\p{P}\\p{S}\\p{Space}]*$";

    /**
     * Begrenset tekstfelt: vanlige latinske bokstaver, tall og tegn, uten linjeskift.
     *
     * @see <a href="https://github.com/navikt/sif-team/blob/main/adr/felt-input-validering.md">ADR: Brukerdata input validering/sanitering</a>
     */
    public static final String BEGRENSET_TEKST = "^[[\\p{IsLatin}&&[\\p{Lu}\\p{Ll}]]\\p{Nd}\\p{No}\\p{M}\\p{P}\\p{S}\\p{Zs}]*$";

    private Patterns() {
    }
}
