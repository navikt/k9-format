package no.nav.k9.søknad.ytelse.ung.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.TidUtils;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.felles.validering.periode.GyldigPeriode;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ungdomsytelse implements Ytelse {

    private UngSøknadstype søknadType = UngSøknadstype.DELTAKELSE_SØKNAD;

    @Valid
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonProperty(value = "søknadsperiode", required = true)
    @NotNull
    private List<@NotNull @GyldigPeriode(krevFomDato = true) Periode> søknadsperiode = new ArrayList<>();

    @JsonProperty(value = "inntekt")
    @DecimalMin("0.00")
    @DecimalMax("1000000.00")
    private BigDecimal inntekt;

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

        if (søknadType == UngSøknadstype.DELTAKELSE_SØKNAD) {
            return new Periode(fom, TidUtils.TIDENES_ENDE); // Deltakelse har ingen sluttdato
        }

        final var tom = perioder
                .stream()
                .map(Periode::getTilOgMed)
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo)
                .orElse(null);
        return new Periode(fom, tom);
    }

    public List<Periode> getSøknadsperiodeList() {
        return søknadsperiode == null ? null : søknadsperiode.stream().map(p -> {
            if (p.getTilOgMed() == null) {
                return new Periode(p.getFraOgMed(), TidUtils.TIDENES_ENDE);
            }
            return p;
        }).toList();
    }

    public BigDecimal getInntekt() {
        return inntekt;
    }

    public Ungdomsytelse medInntekt(BigDecimal inntekt) {
        this.inntekt = Objects.requireNonNull(inntekt, "inntekt");
        return this;
    }

    public Ungdomsytelse medSøknadsperiode(List<Periode> søknadsperiodeList) {
        this.søknadsperiode.addAll(Objects.requireNonNull(søknadsperiodeList, "søknadsperiodeList"));
        return this;
    }

    public Ungdomsytelse medSøknadsperiode(Periode søknadsperiode) {
        this.søknadsperiode.add(Objects.requireNonNull(søknadsperiode, "søknadsperiode"));
        return this;
    }

    public UngSøknadstype getSøknadType() {
        return søknadType;
    }

    public Ungdomsytelse medSøknadType(UngSøknadstype søknadType) {
        this.søknadType = Objects.requireNonNull(søknadType, "søknadType");
        return this;
    }
}
