package no.nav.k9.ettersendelse;

public enum Ytelse {
    PLEIEPENGER_SYKT_BARN,

    PLEIEPENGER_LIVETS_SLUTTFASE,

    /** @deprecated av OMP_UT, OMP_UTV_KS, OMP_UTV_MA. */
    @Deprecated(forRemoval = true)
    OMSORGSPENGER,

    /** Omsorgspenger utbetaling ytelse. */
    OMP_UT,

    /** Omsorgspenger utvidet rett - kronisk syke eller funksjonshemming. */
    OMP_UTV_KS,

    /** Omsorgspenger utvidet rett - midlertidig alene. */
    OMP_UTV_MA,

    /** Omsorgspenger alene om omsorg. */
    OMP_UTV_AO,

    /** Omsorgspenger dele omsorgsdager*/
    OMP_DELE_DAGER,
    ;
}
