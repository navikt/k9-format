package no.nav.k9.oppgave.bekreftelse.ung.livsopphold;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.konstant.Patterns;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AndreLivsoppholdsytelserAvklaringBekreftelse(
        UUID oppgaveReferanse,
        boolean harUttalelse,
        @Pattern(regexp = Patterns.FRITEKST, message = "[ugyldigSyntaks] matcher ikke tillatt pattern [{regexp}]")
        @Size(max = 4000)
        String uttalelseFraBruker,
        DataBruktTilUtledning dataBruktTilUtledning
) implements Bekreftelse {

    public AndreLivsoppholdsytelserAvklaringBekreftelse(UUID oppgaveReferanse, boolean harUttalelse, String uttalelseFraBruker) {
        this(oppgaveReferanse, harUttalelse, uttalelseFraBruker, null);
    }

    @JsonIgnore
    @AssertTrue(message = "uttalelseFraBruker må være satt dersom harUttalelse er true")
    public boolean isUttalelseFraBrukerSattHvisHarUttalelse() {
        if (harUttalelse) {
            return uttalelseFraBruker != null && !uttalelseFraBruker.isBlank();
        }
        return true;
    }

    @Override
    public UUID getOppgaveReferanse() {
        return oppgaveReferanse;
    }

    @JsonIgnore
    @Override
    public Type getType() {
        return Type.AVP_ANDRE_LIVSOPPHOLDSYTELSER_AVKLARING;
    }

    @Override
    public boolean harUttalelse() {
        return harUttalelse;
    }

    @Override
    public String getUttalelseFraBruker() {
        return uttalelseFraBruker;
    }

    @Override
    public DataBruktTilUtledning getDataBruktTilUtledning() {
        return dataBruktTilUtledning;
    }

    @Override
    public Bekreftelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning) {
        return new AndreLivsoppholdsytelserAvklaringBekreftelse(oppgaveReferanse, harUttalelse, uttalelseFraBruker, dataBruktTilUtledning);
    }
}
