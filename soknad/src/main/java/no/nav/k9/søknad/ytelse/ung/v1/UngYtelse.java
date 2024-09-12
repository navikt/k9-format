package no.nav.k9.søknad.ytelse.ung.v1;

import java.util.List;

import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

class UngYtelse implements Ytelse {
    @Override
    public Type getType() {
        return Type.UNG;
    }

    @Override
    public YtelseValidator getValidator(Versjon versjon) {
        return new UngYtelseValidator();
    }

    @Override
    public DataBruktTilUtledning getDataBruktTilUtledning() {
        return new DataBruktTilUtledning();
    }

    @Override
    public Ytelse medDataBruktTilUtledning(DataBruktTilUtledning dataBruktTilUtledning) {
        return this;
    }

    @Override
    public List<Person> getBerørtePersoner() {
        return List.of();
    }

    @Override
    public Person getPleietrengende() {
        return null;
    }

    @Override
    public Person getAnnenPart() {
        return null;
    }

    @Override
    public Periode getSøknadsperiode() {
        return null;
    }
}
