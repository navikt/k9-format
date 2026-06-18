package no.nav.k9.oppgave.bekreftelse.ung.opphor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import no.nav.k9.konstant.Patterns;
import no.nav.k9.oppgave.bekreftelse.Bekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Bekreftelse fra bruker ved opphør nær maksdato i ungdomsprogramytelsen.
 * Bruker kan bekrefte at de har lest varselet, eventuelt med en kommentar.
 *
 * <p>Merk: {@code @JsonIgnoreProperties("type")} er nødvendig fordi {@link Bekreftelse}-interfacet
 * bruker {@code @JsonTypeInfo(As.PROPERTY)}, og Jackson 2 lekker typediscriminatoren inn i
 * record-deserialiseringen i stedet for å konsumere den sentralt. Jackson 3 håndterer dette korrekt
 * uten annotasjonen.
 */
@com.fasterxml.jackson.annotation.JsonIgnoreProperties("type")
public record OpphørVedMaksdatoBekreftelse(
        UUID oppgaveReferanse,
        LocalDate sluttdato,
        boolean harUttalelse,
        @Pattern(regexp = Patterns.FRITEKST, message = "[ugyldigSyntaks] matcher ikke tillatt pattern [{regexp}]")
        @Size(max = 4000)
        String uttalelseFraBruker,
        DataBruktTilUtledning dataBruktTilUtledning
) implements Bekreftelse {

    public OpphørVedMaksdatoBekreftelse(UUID oppgaveReferanse, LocalDate sluttdato, boolean harUttalelse) {
        this(oppgaveReferanse, sluttdato, harUttalelse, null, null);
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

    public LocalDate getSluttdato() {
        return sluttdato;
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
    public Type getType() {
        return Type.UNG_OPPHOR_VED_MAKSDATO;
    }

    @Override
    // TODO(rydd): Vurder å gi denne metoden et mindre builder-liknende navn (f.eks. kloneMedDataBruktTilUtledning)
    // siden dette i record er en kopimetode ("wither") som returnerer ny instans, ikke en muterende setter.
    public Bekreftelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning) {
        return new OpphørVedMaksdatoBekreftelse(oppgaveReferanse, sluttdato, harUttalelse, uttalelseFraBruker, dataBruktTilUtledning);
    }
}
