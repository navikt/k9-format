package no.nav.k9.oppgave.bekreftelse.ung.inntekt;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class InntektBekreftelse implements Bekreftelse {

    @JsonProperty("oppgaveReferanse")
    private final UUID oppgaveReferanse;

    @JsonProperty("harUttalelse")
    private final boolean harUttalelse;

    @JsonProperty("uttalelseFraBruker")
    private final String uttalelseFraBruker;

    @JsonProperty("dataBruktTilUtledning")
    private DataBruktTilUtledning dataBruktTilUtledning;


    @JsonCreator
    public InntektBekreftelse(@JsonProperty("oppgaveReferanse") UUID oppgaveReferanse,
                              @JsonProperty(value = "harUttalelse") boolean harUttalelse,
                              @JsonProperty(value = "uttalelseFraBruker") String uttalelseFraBruker) {
        this.uttalelseFraBruker = uttalelseFraBruker;
        this.harUttalelse = harUttalelse;
        this.oppgaveReferanse = oppgaveReferanse;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public UUID getOppgaveReferanse() {
        return oppgaveReferanse;
    }

    @Override
    public Type getType() {
        return Type.UNG_AVVIK_REGISTERINNTEKT;
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

    public static final class Builder {
        private String uttalelseFraBruker;
        private boolean harUttalelse;
        private UUID oppgaveReferanse;

        private Builder() {
        }

        public Builder medUttalelseFraBruker(String uttalelseFraBruker) {
            this.uttalelseFraBruker = uttalelseFraBruker;
            return this;
        }

        public Builder medHarUttalelse(boolean harUttalelse) {
            this.harUttalelse = harUttalelse;
            return this;
        }

        public Builder medOppgaveReferanse(UUID oppgaveReferanse) {
            this.oppgaveReferanse = oppgaveReferanse;
            return this;
        }

        public InntektBekreftelse build() {
            return new InntektBekreftelse(oppgaveReferanse, harUttalelse, uttalelseFraBruker);
        }
    }


}
