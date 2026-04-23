package no.nav.k9.oppgave.bekreftelse.ung.bosatt;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.konstant.Patterns;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class BostedAvklaringBekreftelse implements Bekreftelse {

    @JsonProperty("oppgaveReferanse")
    private final UUID oppgaveReferanse;

    @JsonProperty("harUttalelse")
    private final boolean harUttalelse;

    @JsonProperty("uttalelseFraBruker")
    @Pattern(regexp = Patterns.FRITEKST, message = "[ugyldigSyntaks]  matcher ikke tillatt pattern [{regexp}]")
    @Size(max = 4000)
    private final String uttalelseFraBruker;

    @JsonProperty("dataBruktTilUtledning")
    private DataBruktTilUtledning dataBruktTilUtledning;

    @JsonCreator
    public BostedAvklaringBekreftelse(@JsonProperty("oppgaveReferanse") UUID oppgaveReferanse,
                                      @JsonProperty(value = "harUttalelse") boolean harUttalelse,
                                      @JsonProperty(value = "uttalelseFraBruker") String uttalelseFraBruker) {
        this.oppgaveReferanse = oppgaveReferanse;
        this.harUttalelse = harUttalelse;
        this.uttalelseFraBruker = uttalelseFraBruker;
    }

    @Override
    public UUID getOppgaveReferanse() {
        return oppgaveReferanse;
    }

    @Override
    public Type getType() {
        return Type.UNG_BOSTED_AVKLARING;
    }

    @Override
    public DataBruktTilUtledning getDataBruktTilUtledning() {
        return dataBruktTilUtledning;
    }

    @Override
    public Bekreftelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning) {
        this.dataBruktTilUtledning = dataBruktTilUtledning;
        return this;
    }

    @Override
    public String getUttalelseFraBruker() {
        return uttalelseFraBruker;
    }

    @Override
    public boolean harUttalelse() {
        return harUttalelse;
    }
}
