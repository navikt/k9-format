package no.nav.k9.søknad.ytelse.aktivitetspenger.v1.medlemskap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.konstant.Patterns;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;
import no.nav.k9.søknad.felles.validering.periode.GyldigePerioderMap;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.unmodifiableMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Utenlandsopphold(
        @GyldigePerioderMap(krevFomDato = true, krevTomDato = true, payload = {AvbrytendeValideringsfeil.class})
        Map<@NotNull Periode, @Valid @NotNull UtenlandsoppholdPeriodeInfo> perioder
) {

    public Utenlandsopphold {
        perioder = unmodifiableMap(perioder == null ? new TreeMap<>() : new TreeMap<>(perioder));
    }

    public static Utenlandsopphold tomt() {
        return new Utenlandsopphold(null);
    }

    @JsonIgnore
    @AssertTrue(message = "[ugyldigPeriodeInterval] Perioder for utenlandsopphold kan ikke overlappe")
    public boolean isHarIngenOverlappendePerioder() {
        LocalDate forrigeTilOgMed = null;
        for (Periode periode : perioder.keySet()) {
            if (periode == null || periode.getFraOgMed() == null || periode.getTilOgMed() == null) {
                continue;
            }
            if (forrigeTilOgMed != null && !forrigeTilOgMed.isBefore(periode.getFraOgMed())) {
                return false;
            }
            forrigeTilOgMed = periode.getTilOgMed();
        }
        return true;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UtenlandsoppholdPeriodeInfo(
            @NotNull
            @Valid
            Landkode land,

            @NotNull
            Boolean jobbetIPerioden,

            @Size(max = 50)
            @Pattern(regexp = Patterns.BEGRENSET_TEKST, message = "[ugyldigSyntaks] matcher ikke tillatt pattern '{regexp}'")
            String utenlandskNasjonalId
    ) {

        @Override
        public String toString() {
            return "UtenlandsoppholdPeriodeInfo{land=" + land + ", jobbetIPerioden=" + jobbetIPerioden + "}";
        }
    }
}
