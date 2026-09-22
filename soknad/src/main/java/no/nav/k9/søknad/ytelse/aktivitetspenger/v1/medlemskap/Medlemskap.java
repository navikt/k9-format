package no.nav.k9.søknad.ytelse.aktivitetspenger.v1.medlemskap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Medlemskap(
        @NotNull
        Boolean harBoddINorge,
        Boolean harJobbetINorge,
        Boolean harJobbetUtenforNorge,
        @Valid
        Utenlandsopphold utenlandsopphold
) {

    @JsonIgnore
    @AssertTrue(message = "harJobbetUtenforNorge må være satt hvis har bodd i Norge")
    public boolean isHarJobbetUtenforNorgeSattHvisBoddINorge() {
        if (Boolean.TRUE.equals(harBoddINorge)) {
            return harJobbetUtenforNorge != null;
        }
        return true;
    }

    @JsonIgnore
    @AssertTrue(message = "harJobbetINorge må være satt hvis har bodd i utlandet")
    public boolean isHarJobbetINorgeSattHvisIkkeBoddINorge() {
        if (Boolean.FALSE.equals(harBoddINorge)) {
            return harJobbetINorge != null;
        }
        return true;
    }

    @JsonIgnore
    @AssertTrue(message = "harJobbetUtenforNorge må være satt hvis har bodd i utlandet og har jobbet i Norge")
    public boolean isHarJobbetUtenforNorgeSattHvisIkkeBoddINorgeOgJobbetINorge() {
        if (Boolean.FALSE.equals(harBoddINorge) && Boolean.TRUE.equals(harJobbetINorge)) {
            return harJobbetUtenforNorge != null;
        }
        return true;
    }

    @JsonIgnore
    @AssertTrue(message = "utenlandsopphold må være satt hvis har jobbet utenfor Norge, eller hvis ikke har jobbet i Norge")
    public boolean isUtenlandsoppholdSattHvisJobbetUtenforNorgeEllerIkkeJobbetINorge() {
        if (Boolean.TRUE.equals(harJobbetUtenforNorge) || Boolean.FALSE.equals(harJobbetINorge)) {
            return utenlandsopphold != null && !utenlandsopphold.perioder().isEmpty();
        }
        return true;
    }

}
