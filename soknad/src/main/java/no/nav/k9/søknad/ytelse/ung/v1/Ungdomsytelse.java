package no.nav.k9.søknad.ytelse.ung.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import no.nav.k9.søknad.TidUtils;
import no.nav.k9.søknad.felles.Feil;
import no.nav.k9.søknad.felles.Versjon;
import no.nav.k9.søknad.felles.type.Periode;
import no.nav.k9.søknad.felles.type.Person;
import no.nav.k9.søknad.ytelse.DataBruktTilUtledning;
import no.nav.k9.søknad.ytelse.Ytelse;
import no.nav.k9.søknad.ytelse.YtelseValidator;
import no.nav.k9.søknad.ytelse.ung.v1.inntekt.OppgittInntekt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Ungdomsytelse implements Ytelse {

    @Valid
    @JsonProperty(value = "søknadType", required = true)
    private UngSøknadstype søknadType = UngSøknadstype.DELTAKELSE_SØKNAD;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonProperty(value = "søktFraDatoer", required = true)
    @NotNull
    private List<@Valid @NotNull LocalDate> søktFraDatoer = new ArrayList<>();

    @Valid
    @JsonProperty(value = "inntekter", required = false)
    private OppgittInntekt inntekter;

    @JsonProperty(value = "deltakelseId")
    private UUID deltakelseId;

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
        if (søknadType.equals(UngSøknadstype.RAPPORTERING_SØKNAD)) {
            return inntekter.getMinMaksPeriode();
        } else if (søknadType.equals(UngSøknadstype.DELTAKELSE_SØKNAD)) {
            final var fom = søktFraDatoer
                    .stream()
                    .min(LocalDate::compareTo)
                    .orElseThrow();

            return new Periode(fom, TidUtils.TIDENES_ENDE); // Deltakelse har ingen sluttdato
        }
        throw new IllegalStateException("Kunne ikke finne periode for søknadtype " + søknadType);

    }

    public OppgittInntekt getInntekter() {
        return inntekter;
    }

    public List<LocalDate> getStartdatoer() {
        return søktFraDatoer;
    }

    public Ungdomsytelse medStartdatoer(List<LocalDate> startdatoer) {
        this.søktFraDatoer.addAll(Objects.requireNonNull(startdatoer, "startdatoer"));
        return this;
    }

    public Ungdomsytelse medStartdato(LocalDate startdato) {
        this.søktFraDatoer.add(Objects.requireNonNull(startdato, "startdato"));
        return this;
    }

    public Ungdomsytelse medInntekter(OppgittInntekt inntekter) {
        this.inntekter = Objects.requireNonNull(inntekter, "inntekter");
        return this;
    }

    public Ungdomsytelse medSøknadType(UngSøknadstype søknadType) {
        this.søknadType = Objects.requireNonNull(søknadType, "søknadType");
        return this;
    }


    public UngSøknadstype getSøknadType() {
        return søknadType;
    }

    public UUID getDeltakelseId() {
        return deltakelseId;
    }

    public Ungdomsytelse medDeltakelseId(UUID deltakelseId) {
        this.deltakelseId = Objects.requireNonNull(deltakelseId, "deltakelseId");
        return this;
    }
}
