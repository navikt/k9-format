package no.nav.k9.søknad.ytelse;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.k9.søknad.felles.type.Person;

@Valid
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
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
}
