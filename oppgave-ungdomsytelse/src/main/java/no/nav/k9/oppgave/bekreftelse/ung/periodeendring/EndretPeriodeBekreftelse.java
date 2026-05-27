package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.konstant.Patterns;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EndretPeriodeBekreftelse(
        @NotNull UUID oppgaveReferanse,
        Periode nyPeriode,
        boolean harUttalelse,
        @Pattern(regexp = Patterns.FRITEKST, message = "[ugyldigSyntaks] matcher ikke tillatt pattern [{regexp}]")
        @Size(max = 4000)
        String uttalelseFraBruker,
        DataBruktTilUtledning dataBruktTilUtledning
) implements Bekreftelse {

    public EndretPeriodeBekreftelse(UUID oppgaveReferanse, Periode nyPeriode, boolean harUttalelse) {
        this(oppgaveReferanse, nyPeriode, harUttalelse, null, null);
    }

    public Periode getNyPeriode() {
        return nyPeriode;
    }

    @Override
    public UUID getOppgaveReferanse() {
        return oppgaveReferanse;
    }

    @JsonIgnore
    @Override
    public Bekreftelse.Type getType() {
        return Bekreftelse.Type.UNG_ENDRET_PERIODE;
    }

    @Override
    public DataBruktTilUtledning getDataBruktTilUtledning() {
        return dataBruktTilUtledning;
    }

    @Override
    // TODO(rydd): Vurder å gi denne metoden et mindre builder-liknende navn (f.eks. kloneMedDataBruktTilUtledning)
    // siden dette i record er en kopimetode ("wither") som returnerer ny instans, ikke en muterende setter.
    public Bekreftelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning) {
        return new EndretPeriodeBekreftelse(oppgaveReferanse, nyPeriode, harUttalelse, uttalelseFraBruker, dataBruktTilUtledning);
    }

    @Override
    public String getUttalelseFraBruker() {
        return uttalelseFraBruker;
    }

    public Bekreftelse medUttalelseFraBruker(String uttalelseFraBruker) {
        return new EndretPeriodeBekreftelse(oppgaveReferanse, nyPeriode, harUttalelse, uttalelseFraBruker, dataBruktTilUtledning);
    }
}
