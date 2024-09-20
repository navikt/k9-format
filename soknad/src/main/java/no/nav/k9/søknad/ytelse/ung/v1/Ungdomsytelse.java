package no.nav.k9.søknad.ytelse.ung.v1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
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

public class Ungdomsytelse implements Ytelse {

    @Valid
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonProperty(value = "søknadsperiode", required = true)
    @NotNull
    private List<@NotNull @LukketPeriode Periode> søknadsperiode = new ArrayList<>();

    @Override
    public Type getType() {
        return Type.UNGDOMSYTELSE;
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
        final List<Periode> perioder = new ArrayList<>(søknadsperiode);

        final var fom = perioder
                .stream()
                .map(Periode::getFraOgMed)
                .min(LocalDate::compareTo)
                .orElseThrow();
        final var tom = perioder
                .stream()
                .map(Periode::getTilOgMed)
                .max(LocalDate::compareTo)
                .orElseThrow();
        return new Periode(fom, tom);
    }

    public Ungdomsytelse medSøknadsperiode(List<Periode> søknadsperiodeList) {
        this.søknadsperiode.addAll(Objects.requireNonNull(søknadsperiodeList, "søknadsperiodeList"));
        return this;
    }

    public Ungdomsytelse medSøknadsperiode(Periode søknadsperiode) {
        this.søknadsperiode.add(Objects.requireNonNull(søknadsperiode, "søknadsperiode"));
        return this;
    }

}
