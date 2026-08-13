package no.nav.k9.søknad.ytelse.aktivitetspenger.v1;

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

public class Aktivitetspenger implements Ytelse {


    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonProperty(value = "søktFraDatoer", required = true)
    @NotNull
    private List<@Valid @NotNull LocalDate> søktFraDatoer = new ArrayList<>();

    @Valid
    @JsonProperty(value = "forutgåendeBosteder", required = true)
    private Bosteder forutgåendeBosteder = new Bosteder();

    @Valid
    @JsonProperty(value = "inntekter", required = false)
    private OppgittInntekt inntekter;

    @JsonProperty(value = "erBosattITrondheim", required = false)
    private Boolean erBosattITrondheim;

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
        final var fom = søktFraDatoer
                .stream()
                .min(LocalDate::compareTo)
                .orElseThrow();

        return new Periode(fom, TidUtils.TIDENES_ENDE); // Deltakelse har ingen sluttdato
    }

    public Bosteder getForutgåendeBosteder() {
        return forutgåendeBosteder;
    }

    public OppgittInntekt getInntekter() {
        return inntekter;
    }

    public List<LocalDate> getStartdatoer() {
        return søktFraDatoer;
    }

    public Aktivitetspenger medStartdatoer(List<LocalDate> startdatoer) {
        this.søktFraDatoer.addAll(Objects.requireNonNull(startdatoer, "startdatoer"));
        return this;
    }

    public Aktivitetspenger medStartdato(LocalDate startdato) {
        this.søktFraDatoer.add(Objects.requireNonNull(startdato, "startdato"));
        return this;
    }

    public Aktivitetspenger medForutgåendeBosteder(Bosteder bosteder) {
        this.forutgåendeBosteder = Objects.requireNonNull(bosteder, "bosteder");
        return this;
    }

    public Aktivitetspenger medInntekter(OppgittInntekt inntekter) {
        this.inntekter = Objects.requireNonNull(inntekter, "inntekter");
        return this;
    }

    public Boolean getErBosattITrondheim() {
        return erBosattITrondheim;
    }

    public Aktivitetspenger medErBosattITrondheim(Boolean erBosattITrondheim) {
        this.erBosattITrondheim = erBosattITrondheim;
        return this;
    }

}
