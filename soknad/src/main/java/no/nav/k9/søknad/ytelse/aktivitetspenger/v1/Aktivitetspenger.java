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
import java.util.Objects;

public class Aktivitetspenger implements Ytelse {

    @Valid
    @NotNull
    @LukketPeriode
    @JsonProperty("søknadsperiode")
    private Periode søknadsperiode;

    @Valid
    @NotNull
    @JsonProperty(value = "forutgåendeBosteder", required = true)
    private Bosteder forutgåendeBosteder;

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

    public Bosteder getForutgåendeBosteder() {
        return forutgåendeBosteder;
    }

    public Aktivitetspenger medSøknadsperiode(Periode søknadsperiode) {
        this.søknadsperiode = Objects.requireNonNull(søknadsperiode, "søknadsperiode");
        return this;
    }

    public Ytelse medForutgåendeBosteder(Bosteder bosteder) {
        this.forutgåendeBosteder = Objects.requireNonNull(bosteder, "bosteder");
        return this;
    }


}
