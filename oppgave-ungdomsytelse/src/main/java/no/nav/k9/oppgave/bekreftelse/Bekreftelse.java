package no.nav.k9.oppgave.bekreftelse;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Valid;
import no.nav.k9.oppgave.bekreftelse.ung.inntekt.InntektBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.EndretFomDatoBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.EndretTomDatoBekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

@Valid
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(name = Bekreftelse.UNG_ENDRET_FOM_DATO, value = EndretFomDatoBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.UNG_ENDRET_TOM_DATO, value = EndretTomDatoBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.UNG_AVVIK_REGISTERINNTEKT, value = InntektBekreftelse.class),
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public interface Bekreftelse {

    String UNG_ENDRET_FOM_DATO = "UNG_ENDRET_FOM_DATO";
    String UNG_ENDRET_TOM_DATO = "UNG_ENDRET_TOM_DATO";
    String UNG_AVVIK_REGISTERINNTEKT = "UNG_AVVIK_REGISTERINNTEKT";

    /**
     * Unik id for oppgaven som blir bekreftet
     */
    UUID getOppgaveId();

    Bekreftelse.Type getType();

    /**
     * Data brukt til utledning av bekreftelse.
     */
    DataBruktTilUtledning getDataBruktTilUtledning();

    Bekreftelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning);

    String getUttalelseFraBruker();

    boolean harBrukerGodtattEndringen();



    enum Type {
        UNG_ENDRET_FOM_DATO(Bekreftelse.UNG_ENDRET_FOM_DATO),
        UNG_ENDRET_TOM_DATO(Bekreftelse.UNG_ENDRET_TOM_DATO),
        UNG_AVVIK_REGISTERINNTEKT(Bekreftelse.UNG_AVVIK_REGISTERINNTEKT);


        @JsonValue
        private final String kode;

        Type(String kode) {
            this.kode = kode;
        }

        public String kode() {
            return kode;
        }
    }

}
