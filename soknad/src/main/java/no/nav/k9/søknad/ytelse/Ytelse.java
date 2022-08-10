package no.nav.k9.søknad.ytelse;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerAleneOmsorg;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerKroniskSyktBarn;
import no.nav.k9.søknad.ytelse.omsorgspenger.utvidetrett.v1.OmsorgspengerMidlertidigAlene;
import no.nav.k9.søknad.ytelse.omsorgspenger.v1.OmsorgspengerUtbetaling;
import no.nav.k9.søknad.ytelse.olp.v1.Opplæringspenger;
import no.nav.k9.søknad.ytelse.pls.v1.PleipengerLivetsSluttfase;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;

@Valid
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(name = Ytelse.OMSORGSPENGER_UTBETALING, value = OmsorgspengerUtbetaling.class),
        @JsonSubTypes.Type(name = Ytelse.OMSORGSPENGER_UTVIDETRETT_KRONISK_SYKT, value = OmsorgspengerKroniskSyktBarn.class),
        @JsonSubTypes.Type(name = Ytelse.OMSORGSPENGER_UTVIDETRETT_MIDLERTIDIG_ALENE, value = OmsorgspengerMidlertidigAlene.class),
        @JsonSubTypes.Type(name = Ytelse.OMSORGSPENGER_UTVIDETRETT_ALENE_OMSORG, value = OmsorgspengerAleneOmsorg.class),
        @JsonSubTypes.Type(name = Ytelse.PLEIEPENGER_SYKT_BARN, value = PleiepengerSyktBarn.class),
        @JsonSubTypes.Type(name = Ytelse.OPPLÆRINGSPENGER, value = Opplæringspenger.class),
        @JsonSubTypes.Type(name = Ytelse.PLEIEPENGER_LIVETS_SLUTTFASE, value = PleipengerLivetsSluttfase.class),
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public interface Ytelse {

    String OMSORGSPENGER_UTBETALING = "OMP_UT";
    String OMSORGSPENGER_UTVIDETRETT_KRONISK_SYKT = "OMP_UTV_KS";
    String OMSORGSPENGER_UTVIDETRETT_MIDLERTIDIG_ALENE = "OMP_UTV_MA";
    String OMSORGSPENGER_UTVIDETRETT_ALENE_OMSORG = "OMP_UTV_AO";
    String OPPLÆRINGSPENGER = "OPPLÆRINGSPENGER";
    String PLEIEPENGER_SYKT_BARN = "PLEIEPENGER_SYKT_BARN";
    String PLEIEPENGER_LIVETS_SLUTTFASE = "PLEIEPENGER_LIVETS_SLUTTFASE";

    Ytelse.Type getType();

    YtelseValidator getValidator();

    /**
     * @return andre berørte, kjente identifiserte personer (enn søker) - f.eks. barn, ektefelle, verge etc. som er involveres i denne saken.
     */
    List<Person> getBerørtePersoner();

    /** Pleietrengende person omtalt i søknaden (eks. barn/nærstående) når denne er subjektet bruker søker på bakgrunn av. */
    Person getPleietrengende();

    /** Annen part omtalt i søknaden (eks. ektefelle/samboer) når denne er subjektet bruker søker på bakgrunn av.*/
    Person getAnnenPart();

    enum Type {
        OMSORGSPENGER_UTBETALING(Ytelse.OMSORGSPENGER_UTBETALING),
        PLEIEPENGER_SYKT_BARN(Ytelse.PLEIEPENGER_SYKT_BARN),
        OPPLÆRINGSPENGER(Ytelse.OPPLÆRINGSPENGER),
        PLEIEPENGER_LIVETS_SLUTTFASE(Ytelse.PLEIEPENGER_LIVETS_SLUTTFASE),
        OMSORGSPENGER_UTVIDETRETT_KRONISK_SYKT_BARN(Ytelse.OMSORGSPENGER_UTVIDETRETT_KRONISK_SYKT),
        OMSORGSPENGER_UTVIDETRETT_MIDLERTIDIG_ALENE(Ytelse.OMSORGSPENGER_UTVIDETRETT_MIDLERTIDIG_ALENE),
        OMSORGSPENGER_UTVIDETRETT_ALENE_OMSORG(Ytelse.OMSORGSPENGER_UTVIDETRETT_ALENE_OMSORG),
        ;

        @JsonValue
        private final String kode;

        Type(String kode) {
            this.kode = kode;
        }

        public String kode() {
            return kode;
        }
    }

    Periode getSøknadsperiode();

}
