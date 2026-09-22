package no.nav.k9.søknad.ytelse.aktivitetspenger.v1.medlemskap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.konstant.Patterns;
import no.nav.k9.søknad.felles.type.Landkode;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.validering.AvbrytendeValideringsfeil;
import no.nav.k9.søknad.felles.validering.periode.GyldigePerioderMap;
import no.nav.k9.søknad.felles.validering.periode.IngenOverlappendePerioder;

import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.unmodifiableMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Utenlandsopphold(
        @GyldigePerioderMap(krevFomDato = true, krevTomDato = true, payload = {AvbrytendeValideringsfeil.class})
        @IngenOverlappendePerioder
        Map<@NotNull Periode, @Valid @NotNull UtenlandsoppholdPeriodeInfo> perioder
) {

    public Utenlandsopphold {
        perioder = unmodifiableMap(perioder == null ? new TreeMap<>() : new TreeMap<>(perioder));
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
