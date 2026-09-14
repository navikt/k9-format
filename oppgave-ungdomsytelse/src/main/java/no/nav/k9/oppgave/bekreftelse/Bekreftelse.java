package no.nav.k9.oppgave.bekreftelse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Valid;
import no.nav.k9.oppgave.bekreftelse.ung.aktivitet.AktivitetAvklaringBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.bistand.BistandsbehovAvklaringBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.bosatt.BostedAvklaringBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.livsopphold.AndreLivsoppholdsytelserAvklaringBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.inntekt.InntektBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.opphor.OpphørVedMaksdatoBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.EndretPeriodeBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.EndretSluttdatoBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.EndretStartdatoBekreftelse;
import no.nav.k9.oppgave.bekreftelse.ung.periodeendring.FjernetPeriodeBekreftelse;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;

import java.util.UUID;

@Valid
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(name = Bekreftelse.UNG_ENDRET_STARTDATO, value = EndretStartdatoBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.UNG_ENDRET_SLUTTDATO, value = EndretSluttdatoBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.UNG_ENDRET_PERIODE, value = EndretPeriodeBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.UNG_FJERNET_PERIODE, value = FjernetPeriodeBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.UNG_AVVIK_REGISTERINNTEKT, value = InntektBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.AVP_BOSTED_AVKLARING, value = BostedAvklaringBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.AVP_BISTANDSBEHOV_AVKLARING, value = BistandsbehovAvklaringBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.AVP_ANDRE_LIVSOPPHOLDSYTELSER_AVKLARING, value = AndreLivsoppholdsytelserAvklaringBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.AVP_AKTIVITET_AVKLARING, value = AktivitetAvklaringBekreftelse.class),
        @JsonSubTypes.Type(name = Bekreftelse.UNG_OPPHOR_VED_MAKSDATO, value = OpphørVedMaksdatoBekreftelse.class),
})
public interface Bekreftelse {

    String UNG_ENDRET_STARTDATO = "UNG_ENDRET_STARTDATO";
    String UNG_ENDRET_SLUTTDATO = "UNG_ENDRET_SLUTTDATO";
    String UNG_ENDRET_PERIODE = "UNG_ENDRET_PERIODE";
    String UNG_FJERNET_PERIODE = "UNG_FJERNET_PERIODE";
    String UNG_AVVIK_REGISTERINNTEKT = "UNG_AVVIK_REGISTERINNTEKT";
    String AVP_BOSTED_AVKLARING = "AVP_BOSTED_AVKLARING";
    String AVP_BISTANDSBEHOV_AVKLARING = "AVP_BISTAND_AVKLARING";
    String AVP_ANDRE_LIVSOPPHOLDSYTELSER_AVKLARING = "AVP_ANDRE_LIVSOPPHOLDSYTELSER_AVKLARING";
    String AVP_AKTIVITET_AVKLARING = "AVP_AKTIVITET_AVKLARING";
    String UNG_OPPHOR_VED_MAKSDATO = "UNG_OPPHOR_VED_MAKSDATO";

    /**
     * Unik id for oppgaven som blir bekreftet
     */
    UUID getOppgaveReferanse();

    @JsonIgnore
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
        UNG_ENDRET_PERIODE(Bekreftelse.UNG_ENDRET_PERIODE),
        UNG_FJERNET_PERIODE(Bekreftelse.UNG_FJERNET_PERIODE),
        UNG_AVVIK_REGISTERINNTEKT(Bekreftelse.UNG_AVVIK_REGISTERINNTEKT),
        AVP_BOSTED_AVKLARING(Bekreftelse.AVP_BOSTED_AVKLARING),
        AVP_BISTANDSBEHOV_AVKLARING(Bekreftelse.AVP_BISTANDSBEHOV_AVKLARING),
        AVP_ANDRE_LIVSOPPHOLDSYTELSER_AVKLARING(Bekreftelse.AVP_ANDRE_LIVSOPPHOLDSYTELSER_AVKLARING),
        AVP_AKTIVITET_AVKLARING(Bekreftelse.AVP_AKTIVITET_AVKLARING),
        UNG_OPPHOR_VED_MAKSDATO(Bekreftelse.UNG_OPPHOR_VED_MAKSDATO);


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
