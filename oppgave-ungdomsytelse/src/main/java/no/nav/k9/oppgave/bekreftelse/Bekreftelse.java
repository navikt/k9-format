package no.nav.k9.oppgave.bekreftelse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Valid;
import no.nav.k9.oppgave.bekreftelse.ung.inntekt.InntektBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.EndretSluttdatoBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.EndretStartdatoBekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.util.UUID;

@Valid
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(name = Bekreftelse.UNG_ENDRET_STARTDATO, value = EndretStartdatoBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.UNG_ENDRET_SLUTTDATO, value = EndretSluttdatoBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.UNG_AVVIK_REGISTERINNTEKT, value = InntektBekreftelse.class),
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public interface Bekreftelse {

    String UNG_ENDRET_STARTDATO = "UNG_ENDRET_STARTDATO";
    String UNG_ENDRET_SLUTTDATO = "UNG_ENDRET_SLUTTDATO";
    String UNG_AVVIK_REGISTERINNTEKT = "UNG_AVVIK_REGISTERINNTEKT";

    /**
     * Unik id for oppgaven som blir bekreftet
     */
    UUID getOppgaveReferanse();

    Bekreftelse.Type getType();

    /**
     * Data brukt til utledning av bekreftelse.
     */
    DataBruktTilUtledning getDataBruktTilUtledning();

    Bekreftelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning);

    String getUttalelseFraBruker();

    boolean harUttalelse();


    enum Type {
        UNG_ENDRET_STARTDATO(Bekreftelse.UNG_ENDRET_STARTDATO),
        UNG_ENDRET_SLUTTDATO(Bekreftelse.UNG_ENDRET_SLUTTDATO),
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
