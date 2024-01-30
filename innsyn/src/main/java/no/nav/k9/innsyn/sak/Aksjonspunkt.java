package no.nav.k9.innsyn.sak;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record Aksjonspunkt(
        @JsonProperty("type") @Valid @NotNull Type type,
        @JsonProperty("venteårsak") @Valid @NotNull Venteårsak venteårsak
) {

    /**
     * @see <a href="https://github.com/navikt/k9-sak/blob/fcaffc46d11cb5cc10738ad5cc34d981b0fbcff1/kodeverk/src/main/java/no/nav/k9/kodeverk/behandling/aksjonspunkt/Vente%C3%A5rsak.java">Venteårsak i k9-sak</a>
     */
    public enum Venteårsak {
        UDEFINERT, // Ikke definert
        ANNET, // Annet
        AVV_DOK, // Annen dokumentasjon

        // Inntektsmelding
        AVV_IM_MOT_AAREG, // Venter på inntektsmelding fra arbeidsgiver som stemmer med Aareg
        AVV_IM_MOT_SØKNAD_AT, // Venter på inntektsmelding fra arbeidsgiver etter mottatt søknad som arbeidstaker
        VENT_OPDT_INNTEKTSMELDING, // Venter på oppdatert inntektsmelding
        VENT_PÅ_NY_INNTEKTSMELDING_MED_GYLDIG_ARB_ID, // Venter på ny inntektsmelding med arbeidsforholdId som stemmer med Aareg
        VENTER_PÅ_ETTERLYST_INNTEKTSMELDINGER, // Venter på inntektsmeldinger etter etterlysning
        VENTER_PÅ_ETTERLYST_INNTEKTSMELDINGER_MED_VARSEL, // Venter på inntektsmeldinger etter etterlysning med varsel om mulig avslag
        INNTEKTSMELDING, // Inntektsmelding
        // Inntektsmelding

        // Medisinske opplysninger
        LEGEERKLÆRING, // Legeerklæring fra riktig lege
        MEDISINSKE_OPPLYSNINGER,
    }

    // TODO:  Tilpass aksjonspunktene i k9-sak
    public enum Type {
        VENT_ETTERLYST_INNTEKTSMELDING,
        VENT_ETTERLYST_MEDISINSKE_OPPLYSNINGER,
        VENT_ANNET
    }
}
