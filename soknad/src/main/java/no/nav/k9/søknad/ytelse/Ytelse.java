package no.nav.k9.søknad.ytelse;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;

import no.nav.k9.søknad.ytelse.omsorgspenger.v1.OmsorgspengerUtbetaling;
import no.nav.k9.søknad.ytelse.psb.v1.PleiepengerSyktBarn;

@Valid
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(name = "OMP_UT", value = OmsorgspengerUtbetaling.class),
        @JsonSubTypes.Type(name = "PLEIEPENGER_SYKT_BARN", value = PleiepengerSyktBarn.class),
})
public interface Ytelse {

    String OMSORGSPENGER_UTBETALING = "OMP_UT";
    String PLEIEPENGER_SYKT_BARN = "PLEIEPENGER_SYKT_BARN";

    Ytelse.Type getType();

    YtelseValidator getValidator();
    
    /** @return andre berørte, kjente identifiserte personer (enn søker) - f.eks. barn, ektefelle, verge etc. som er involveres i denne saken.*/
    List<Person> getBerørtePersoner();

    enum Type {
        OMSORGSPENGER_UTBETALING(Ytelse.OMSORGSPENGER_UTBETALING),
        PLEIEPENGER_SYKT_BARN(Ytelse.PLEIEPENGER_SYKT_BARN);

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
