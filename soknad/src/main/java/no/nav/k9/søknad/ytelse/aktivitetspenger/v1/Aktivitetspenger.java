package no.nav.k9.søknad.ytelse.aktivitetspenger.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.felles.validering.periode.LukketPeriode;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

import java.util.List;

public class Aktivitetspenger implements Ytelse {


    @Valid
    @NotNull
    @LukketPeriode
    @JsonProperty("søknadsperiode")
    private Periode søknadsperiode;

    @Override
    public Type getType() {
        return Type.AKTIVITETSPENGER;
    }

    @Override
    public YtelseValidator getValidator(Versjon versjon) {
        return new YtelseValidator() {
            @Override
            public List<Feil> valider(Ytelse søknad) {
                return List.of();
            }
        };
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
        return søknadsperiode;
    }

}
