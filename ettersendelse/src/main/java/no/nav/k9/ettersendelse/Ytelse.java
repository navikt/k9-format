package no.nav.k9.ettersendelse;

public enum Ytelse {
    PLEIEPENGER_SYKT_BARN,
    
    /** @deprecated av OMP_UT, OMP_UTV_KS, OMP_UTV_MA. */
    @Deprecated(forRemoval = true)
    OMSORGSPENGER,
    
    /** Omsorgspenger utbetaling ytelse. */
    OMP_UT,
    
    /** Omsorgspenger utvidet rett - kronisk syke eller funksjonshemming. */
    OMP_UTV_KS,
    
    /** Omsorgspenger utvidet rett - midlertidig alene. */
    OMP_UTV_MA,

    /** Omsorgspenger dele omsorgsdager*/
    OMP_DELE_DAGER
}
