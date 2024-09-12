package no.nav.k9.søknad.ytelse.ung.v1;

import java.util.Collections;
import java.util.List;

import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

class UngYtelseValidator extends YtelseValidator {

    private final String YTELSE_FELT = "ytelse.";

    @Override
    public List<Feil> valider(Ytelse ytelse) {
        return Collections.emptyList();
    }

    @Override
    public List<Feil> valider(Ytelse ytelse, List<Periode> gyldigeEndringsperioder) {
        return Collections.emptyList();
    }

    @Override
    public void forsikreValidert(Ytelse ytelse) {

    }

    public void forsikreValidert(Ytelse ytelse, List<Periode> gyldigeEndringsperioder) {

    }



}
