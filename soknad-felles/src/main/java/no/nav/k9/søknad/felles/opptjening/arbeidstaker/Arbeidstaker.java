package no.nav.k9.søknad.omsorgspenger.utbetaling.arbeidstaker;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.k9.søknad.felles.opptjening.Aktivitet;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonTypeName(Aktivitet.ARBEIDSTAKER)
public class Arbeidstaker implements Aktivitet {

    @Override
    public Type getType() {
        return Type.ARBEIDSTAKER;
    }
}
