package no.nav.k9.oppgave.bekreftelse.ung.periodeendring;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.konstant.Patterns;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.time.LocalDate;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EndretSluttdatoBekreftelse(
        UUID oppgaveReferanse,
        LocalDate nySluttdato,
        boolean harUttalelse,
        @Pattern(regexp = Patterns.FRITEKST, message = "[ugyldigSyntaks] matcher ikke tillatt pattern [{regexp}]")
        @Size(max = 4000)
        String uttalelseFraBruker,
        DataBruktTilUtledning dataBruktTilUtledning
) implements Bekreftelse {

    public EndretSluttdatoBekreftelse(UUID oppgaveReferanse, LocalDate nySluttdato, boolean harUttalelse) {
        this(oppgaveReferanse, nySluttdato, harUttalelse, null, null);
    }

    public LocalDate getNySluttdato() {
        return nySluttdato;
    }

    @Override
    public UUID getOppgaveReferanse() {
        return oppgaveReferanse;
    }

    @JsonIgnore
    @Override
    public Type getType() {
        return Type.UNG_ENDRET_SLUTTDATO;
    }

    @Override
    public DataBruktTilUtledning getDataBruktTilUtledning() {
        return dataBruktTilUtledning;
    }

    @Override
    // TODO(rydd): Vurder å gi denne metoden et mindre builder-liknende navn (f.eks. kloneMedDataBruktTilUtledning)
    // siden dette i record er en kopimetode ("wither") som returnerer ny instans, ikke en muterende setter.
    public Bekreftelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning) {
        return new EndretSluttdatoBekreftelse(oppgaveReferanse, nySluttdato, harUttalelse, uttalelseFraBruker, dataBruktTilUtledning);
    }

    @Override
    public String getUttalelseFraBruker() {
        return uttalelseFraBruker;
    }

    public Bekreftelse medUttalelseFraBruker(String uttalelseFraBruker) {
        return new EndretSluttdatoBekreftelse(oppgaveReferanse, nySluttdato, harUttalelse, uttalelseFraBruker, dataBruktTilUtledning);
    }
}
