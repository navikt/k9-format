package no.nav.k9.søknad.ytelse;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Valid
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface Ytelse {

    String OMSORGSPENGER_UTBETALING = "OMP_UT";
    String PLEIEPENGER_SYKT_BARN = "PLEIEPENGER_SYKT_BARN";

    Ytelse.Type getType();

    YtelseValidator getValidator();

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
